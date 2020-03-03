package com.densoftinfotech.densoftpaysmart.background_service;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.densoftinfotech.densoftpaysmart.R;
import com.densoftinfotech.densoftpaysmart.app_utilities.AutoCounter;
import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.app_utilities.DateUtils;
import com.densoftinfotech.densoftpaysmart.app_utilities.InternetUtils;
import com.densoftinfotech.densoftpaysmart.location_utilities.MapConstants;
import com.densoftinfotech.densoftpaysmart.location_utilities.UserLocation;
import com.densoftinfotech.densoftpaysmart.model.FirebaseLiveLocation;
import com.densoftinfotech.densoftpaysmart.model.LocationHistoryModel;
import com.densoftinfotech.densoftpaysmart.sqlitedatabase.DatabaseHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import static com.densoftinfotech.densoftpaysmart.app_utilities.Constants.staffDetails;
import static com.densoftinfotech.densoftpaysmart.app_utilities.DateUtils.getTime;

public class LocationTrackerService extends Service {

    private static final String TAG = LocationTrackerService.class.getSimpleName();
    private static String channel_id = "nidhikamath";
    SharedPreferences preferences;
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
    TimerTask scanTask;
    final Handler handler = new Handler();
    Timer t = new Timer();
    String stop = "stop";

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.firebase_database_name/* + "/" + preferences.getInt("customerid", 0) + "/" +
                preferences.getInt("staffid", 0)*/);

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Log.d("called ", "onCreate");
        registerReceiver(stopReceiver, new IntentFilter(stop));
        buildNotification();
        loginToFirebase();
        start_timer_for_location_history(this);
    }

    private void buildNotification() {

        if(DateUtils.within_office_hours()){
            Log.d("called ", "build notification enable");
            enable_tracking();
        }else {
            Log.d("called ", "build notification disable");
            disable_tracking();
        }

    }

    private void startForeground(PendingIntent broadcastIntent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder mBuilder;
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notificationManager.getNotificationChannel(channel_id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(channel_id, "tracking started", importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notificationManager.createNotificationChannel(mChannel);
            }

            mBuilder = new NotificationCompat.Builder(this, channel_id);
            mBuilder.setContentTitle(getString(R.string.app_name))
                    .setContentText(getString(R.string.notification_text))
                    .setOngoing(true)
                    .setContentIntent(broadcastIntent)
                    .setSmallIcon(R.drawable.ic_tracker);
            startForeground(1, mBuilder.build());
        }

    }

    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "received stop broadcast");
            // Stop the service when the notification is tapped
            stopForeground(true);
            //unregisterReceiver(stopReceiver);
            stopSelf();
        }
    };

    private void loginToFirebase() {
        // Functionality coming next step
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {

        // Functionality coming next step
        LocationRequest request = new LocationRequest();
        request.setInterval(MapConstants.location_request_interval);
        request.setFastestInterval(MapConstants.location_request_fastestinterval);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_GRANTED) {
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

                    Location location = locationResult.getLastLocation();
                    if (location != null && preferences.getInt("staffid", 0) != 0) {

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    FirebaseLiveLocation firebaseLiveLocation = dataSnapshot.getValue(FirebaseLiveLocation.class);

                                    if (firebaseLiveLocation != null) {
                                        double d = DateUtils.distance(location.getLatitude(), location.getLongitude(),
                                                firebaseLiveLocation.getLatitude(), firebaseLiveLocation.getLongitude(), 'm');
                                        Log.d("diff ", "in m " + d);

                                        //update after the user travels 150 m from the current distance
                                        if (d > MapConstants.distance_greater_than) {
                                            HashMap<String, Object> firebaselive = new HashMap<>();
                                            firebaselive.put("staff_id", preferences.getInt("staffid", 0));

                                            if (location.getLatitude() != 0) {
                                                firebaselive.put("latitude", location.getLatitude());
                                            }
                                            if (location.getLongitude() != 0) {
                                                firebaselive.put("longitude", location.getLongitude());
                                            }

                                            if (!getAddress(location).equals("")) {
                                                firebaselive.put("address", getAddress(location));
                                            }

                                            firebaselive.put("timestamp", System.currentTimeMillis());
                                            ref.child(String.valueOf(preferences.getInt("staffid", 0))).updateChildren(firebaselive);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                    save_to_db(location);
                }
            }, null);

        }


    }

    private void save_to_db(Location location) {
        ContentValues c = new ContentValues();
        c.put(DatabaseHelper.STAFF_ID, preferences.getInt("staffid", 0));
        c.put(DatabaseHelper.STAFF_NAME, staffDetails.getPName());
        c.put(DatabaseHelper.LATITUDE, location.getLatitude());
        c.put(DatabaseHelper.LONGITUDE, location.getLongitude());
        c.put(DatabaseHelper.ADDRESS, getAddress(location));
        c.put(DatabaseHelper.WORKING_HOUR_FROM, MapConstants.workinghour_from);
        c.put(DatabaseHelper.WORKING_HOUR_TO, MapConstants.workinghour_to);
        c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
        DatabaseHelper.getInstance(this).save_location(c, preferences.getInt("staffid", 0));
    }

    public String getAddress(Location location) {
        String add = "";
        try {
            if (location != null) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
                StringBuilder builder = new StringBuilder();
                try {
                    List<Address> address = geoCoder.getFromLocation(lat, lng, 1);
                    int maxLines = address.get(0).getMaxAddressLineIndex();
                    for (int i = 0; i < maxLines; i++) {
                        String addressStr = address.get(0).getAddressLine(i);
                        Log.e("str ", "str " + addressStr);
                        builder.append(addressStr);
                        builder.append(" ");
                    }

                    Address obj = address.get(0);
                    add = obj.getAddressLine(0);               //Address386, SVS Rd, Sane Guruji Premises, opposite Siddhi Vinayak Temple, Dadar West, Prabhadevi, Mumbai, Maharashtra 400028, India
                    Log.d("Address", " " + add);


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return add;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("stop")) {

                try {

                    if (!DateUtils.within_office_hours()) {
                        //unregisterReceiver(stopReceiver);
                        HashMap<String, Object> update = new HashMap<>();
                        update.put("allow_tracking", 0);
                        ref.child(String.valueOf(preferences.getInt("staffid", 0))).updateChildren(update);
                        stopForeground(true);
                        stopSelf();
                    }
                /*Log.i("Recevied", "stop Foreground Intent ");
                unregisterReceiver(stopReceiver);
                stopForeground(true);
                stopSelf();*/
                    // your start service code
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void start_timer_for_location_history(Context context) {
        scanTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {

                        if(DateUtils.within_office_hours()){
                            UserLocation location = new UserLocation(context);
                            if (InternetUtils.getInstance(context).available()) {
                                Query lastQuery = ref.child(String.valueOf(String.valueOf(preferences.getInt("staffid", 0)))).child(String.valueOf(DateUtils.getDate())).child("locationhistory").child(String.valueOf(AutoCounter.getCounterCurrentVal()));
                                ref.child(String.valueOf(String.valueOf(preferences.getInt("staffid", 0)))).child(String.valueOf(DateUtils.getDate())).child("locationhistory").child(String.valueOf(AutoCounter.getCounterCurrentVal())).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            //for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            LocationHistoryModel locationHistoryModel = dataSnapshot.getValue(LocationHistoryModel.class);
                                            if (locationHistoryModel != null) {
                                                //long time_diff = DateUtils.getDateDiff(System.currentTimeMillis(), locationHistoryModel.getTimestamp(), TimeUnit.MILLISECONDS);

                                                if (location != null && location.getLatitude() != 0 && location.getLongitude() != 0) {
                                                    HashMap<String, Object> firebaselive = new HashMap<>();
                                                    firebaselive.put("timestamp", System.currentTimeMillis());
                                                    firebaselive.put("latitude", location.getLatitude());
                                                    firebaselive.put("longitude", location.getLongitude());
                                                    firebaselive.put("address", location.getAddress_fromLatLng(location.getLatitude(), location.getLongitude()));
                                                    firebaselive.put("current_time", getTime(System.currentTimeMillis()));
                                                    firebaselive.put("angle", 0);
                                                    ref.child(String.valueOf(String.valueOf(preferences.getInt("staffid", 0)))).child(String.valueOf(DateUtils.getDate())).child("locationhistory").child(String.valueOf(AutoCounter.getCounterPlusOne())).setValue(firebaselive)
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {

                                                                }
                                                            });
                                                }
                                            }
                                            //}
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                if (location != null && location.getLatitude() != 0 && location.getLongitude() != 0) {
                                    HashMap<String, Object> firebaselive = new HashMap<>();
                                    firebaselive.put("timestamp", System.currentTimeMillis());
                                    firebaselive.put("latitude", location.getLatitude());
                                    firebaselive.put("longitude", location.getLongitude());
                                    firebaselive.put("address", location.getAddress_fromLatLng(location.getLatitude(), location.getLongitude()));
                                    firebaselive.put("current_time", getTime(System.currentTimeMillis()));
                                    firebaselive.put("angle", 0);

                                }

                            }
                        }else{
                            disable_tracking();
                        }
                    }
                });
            }
        };


        t.schedule(scanTask, 300, MapConstants.update_after_every);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            if (DateUtils.within_office_hours()) {
                enable_tracking();
            }else{
                disable_tracking();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enable_tracking(){
        HashMap<String, Object> update = new HashMap<>();
        update.put("allow_tracking", 1);
        ref.child(String.valueOf(preferences.getInt("staffid", 0))).updateChildren(update);
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
        // Create the persistent notification

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel_id)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_text))
                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.ic_tracker);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(broadcastIntent);
        } else {
            startForeground(1, builder.build());
        }
    }

    private void disable_tracking() {
        HashMap<String, Object> update = new HashMap<>();
        update.put("allow_tracking", 0);
        ref.child(String.valueOf(preferences.getInt("staffid", 0))).updateChildren(update);

        stopForeground(true);
        //unregisterReceiver(stopReceiver);
        stopSelf();
        //unregisterReceiver(stopReceiver);
    }

}