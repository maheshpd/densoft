package com.densoftinfotech.densoftpaysmart.location_utilities;

import android.Manifest;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;

import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.app_utilities.DateUtils;
import com.densoftinfotech.densoftpaysmart.background_service.LocationTrackerService;
import com.densoftinfotech.densoftpaysmart.model.FirebaseLiveLocation;
import com.densoftinfotech.densoftpaysmart.sqlitedatabase.DatabaseHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.PreferenceManager;


public class LocationMonitoringService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = LocationMonitoringService.class.getSimpleName();
    GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest = new LocationRequest();


    public static final String ACTION_LOCATION_BROADCAST = "LocationBroadcast";
    public static final String EXTRA_LATITUDE = "latitude";
    public static final String EXTRA_LONGITUDE = "longitude";
    SharedPreferences sharedPreferences;
    private UserLocation userLocation;
    private DatabaseReference databaseReference;
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm") ;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest.setInterval(Constants.LOCATION_INTERVAL);
        mLocationRequest.setFastestInterval(Constants.FASTEST_LOCATION_INTERVAL);


        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY; //by default
        //PRIORITY_BALANCED_POWER_ACCURACY, PRIORITY_LOW_POWER, PRIORITY_NO_POWER are the other priority modes


        mLocationRequest.setPriority(priority);
        mLocationClient.connect();

        //Make it stick to the notification panel so it is less prone to get cancelled by the Operating System.
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     * LOCATION CALLBACKS
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                //Log.d("LocationMonitoringService ", "== Error On onConnected() Permission not granted");
                //Permission not granted by user so cancel the further execution.

                return;
            }

            LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);

            //Log.d(TAG, "Connected to Google API");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onConnectionSuspended(int i) {
        //Log.d(TAG, "Connection suspended");
    }

    //to get the location change
    @Override
    public void onLocationChanged(Location location) {
        //Log.d(TAG, "Location changed");

        if (location != null) {
            //Log.d(TAG, "== location != null");

            //Send result to activities
            sendMessageToUI(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
        }
    }


    private void sendMessageToUI(String lat, String lng) {

        try {

            //Log.d(TAG, "Sending info...");
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            if (sharedPreferences.contains("staffid")) {

                //condition to track only during office hours
                if (DateUtils.within_office_hours()) {
                    add_data_toSqlite(sharedPreferences.getInt("staffid", 0), sharedPreferences.getInt("customerid", 0));
                    Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
                    intent.putExtra(EXTRA_LATITUDE, lat);
                    intent.putExtra(EXTRA_LONGITUDE, lng);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                }else{
                    try{
                        HashMap<String, Object> update = new HashMap<>();
                        update.put("allow_tracking", 0);
                        databaseReference.child(String.valueOf(sharedPreferences.getInt("staffid", 0))).updateChildren(update);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    try{
                        Intent stopIntent = new Intent(this, LocationTrackerService.class);
                        stopIntent.setAction("stop");
                        startService(stopIntent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void add_data_toSqlite(int staffid, int customerid) {
        userLocation = new UserLocation(this);
        ContentValues c = new ContentValues();
        c.put(DatabaseHelper.STAFF_ID, staffid);
        c.put(DatabaseHelper.LATITUDE, userLocation.getLatitude());
        c.put(DatabaseHelper.LONGITUDE, userLocation.getLongitude());
        c.put(DatabaseHelper.ADDRESS, userLocation.getAddress());
        c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
        DatabaseHelper.getInstance(this).save_location(c, staffid);
        //Log.d("update ", DatabaseHelper.getInstance(this).get_LiveLocationUpdate(staffid) + "");

        //uncomment to add realtime update of location to firebase
        //if (!staffid.trim().equals("") && !company_name.trim().equals("")) {
        add_live_updates_to_firebase(userLocation, staffid, customerid);
        //}
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //Log.d(TAG, "Failed to connect to Google API");

    }


    private void add_live_updates_to_firebase(UserLocation userLocation, int staffid, int customerid) {

        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.firebase_database_name/* + "/" + customerid*/);


        databaseReference.child(String.valueOf(staffid)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    FirebaseLiveLocation firebaseLiveLocation = dataSnapshot.getValue(FirebaseLiveLocation.class);

                    if (firebaseLiveLocation != null && (firebaseLiveLocation.getAllow_tracking() == 1)) {
                        Map<String, Object> firebaseLiveLocationMap = new HashMap<>();
                        firebaseLiveLocationMap.put("staff_id", staffid);
                        firebaseLiveLocationMap.put("latitude", String.valueOf(userLocation.getLatitude()));
                        firebaseLiveLocationMap.put("longitude", String.valueOf(userLocation.getLongitude()));
                        firebaseLiveLocationMap.put("address", userLocation.getAddress());
                        databaseReference.child(String.valueOf(staffid)).updateChildren(firebaseLiveLocationMap);
                        //Log.d("location map ", firebaseLiveLocationMap.toString());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
