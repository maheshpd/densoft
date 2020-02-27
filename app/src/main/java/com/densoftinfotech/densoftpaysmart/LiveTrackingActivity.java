package com.densoftinfotech.densoftpaysmart;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.app_utilities.DateUtils;
import com.densoftinfotech.densoftpaysmart.model.FirebaseLiveLocation;
import com.densoftinfotech.densoftpaysmart.model.StaffDetails;
import com.densoftinfotech.densoftpaysmart.location_utilities.LocationMonitoringService;
import com.densoftinfotech.densoftpaysmart.location_utilities.UserLocation;
import com.densoftinfotech.densoftpaysmart.location_utilities.MapConstants;
import com.densoftinfotech.densoftpaysmart.sqlitedatabase.DatabaseHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LiveTrackingActivity extends CommonActivity {

    TextView tv_trackme, tv_stoptracking;
    Spinner spinner_mode_of_transport;
    private UserLocation userLocation;
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private boolean mAlreadyStartedService = false;
    private LocationRequest mLocationRequest;
    private SharedPreferences sharedPreferences;
    private StaffDetails staffDetails;
    private ArrayList<FirebaseLiveLocation> firebaseLiveLocations = new ArrayList<>();
    private ArrayList<String> modes_transport = new ArrayList<>();
    ArrayAdapter<String> spinner_modes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_tracking);

        tv_trackme = findViewById(R.id.tv_trackme);
        tv_stoptracking = findViewById(R.id.tv_stoptracking);
        spinner_mode_of_transport = findViewById(R.id.spinner_mode_of_transport);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LiveTrackingActivity.this);
        modes_transport.add(getResources().getString(R.string.please_select));
        modes_transport.add("Driving");
        modes_transport.add("Cycling");
        modes_transport.add("Walking");
        spinner_modes = new ArrayAdapter<>(LiveTrackingActivity.this, R.layout.autocomplete_layout, R.id.actv_text, modes_transport);
        spinner_modes.setDropDownViewResource(R.layout.autocomplete_layout);
        spinner_mode_of_transport.setAdapter(spinner_modes);

        databaseReference = firebaseDatabase.getReference(Constants.firebase_database_name + "/" + sharedPreferences.getInt("customerid", 0));
        setTitle(getResources().getString(R.string.travel_claims));
        back();

        staffDetails = getStaffDetails(LiveTrackingActivity.this);

        update_table();

        tv_trackme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_param_ok()) {
                    if (staffDetails != null) {
                        if (modes_transport.get(spinner_mode_of_transport.getSelectedItemPosition()).equalsIgnoreCase(getResources().getString(R.string.please_select))) {
                            Toast.makeText(LiveTrackingActivity.this, getResources().getString(R.string.select_mode_of_transport), Toast.LENGTH_SHORT).show();
                        } else {
                            open_alert(1);
                        }
                    }
                }
            }
        });

        tv_stoptracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_alert(0);
            }
        });

    }

    private void update_table() {
        firebaseLiveLocations = DatabaseHelper.getInstance(LiveTrackingActivity.this).get_LiveLocationUpdate(sharedPreferences.getInt("staffid", 0));
        if (firebaseLiveLocations != null && firebaseLiveLocations.size() > 0) {
            if (firebaseLiveLocations.get(0).getAllow_tracking() == 1) {
                tv_trackme.setText(getResources().getString(R.string.live_tracking_started));
                //live_track();
            } else {
                tv_trackme.setText(getResources().getString(R.string.resume_tracking));
                stopService(new Intent(this, LocationMonitoringService.class));
                mAlreadyStartedService = false;
                update_firebase();
            }
        }
    }

    private void open_alert(int flag) {
        AlertDialog.Builder alert = new AlertDialog.Builder(LiveTrackingActivity.this);

        if (flag == 1) {
            alert.setTitle(getResources().getString(R.string.realtime_tracking));
            alert.setMessage(getResources().getString(R.string.alert_message_for_tracking));

            alert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    tv_trackme.setText(getResources().getString(R.string.live_tracking_started));
                    live_track();

                }
            });
        } else {
            alert.setTitle(getResources().getString(R.string.stop_tracking));
            alert.setMessage(getResources().getString(R.string.alert_message_for_stoptracking));

            alert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    stopService(new Intent(LiveTrackingActivity.this, LocationMonitoringService.class));
                    mAlreadyStartedService = false;
                    update_firebase();
                    add_data_toSqlite(0);
                    update_table();
                }
            });
        }

        alert.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alert.show();
    }

    private void add_live_updates_to_firebase(String latitude, String longitude) {


        /*firebaseLiveLocationMap.put(sharedPreferences.getString("staffid", ""), new FirebaseLiveLocation(staffDetails.getStaffId(), staffDetails.getPName(), latitude,
                longitude, userLocation.getAddress_fromLatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)), (MapConstants.workinghour_from + "-" + MapConstants.workinghour_to)));*/
        databaseReference.child(String.valueOf(sharedPreferences.getInt("staffid", 0))).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> firebaseLiveLocationMap = new HashMap<>();
                firebaseLiveLocationMap.put("staff_id", sharedPreferences.getInt("staffid", 0));
                firebaseLiveLocationMap.put("staff_name", staffDetails.getPName());
                firebaseLiveLocationMap.put("latitude", latitude);
                firebaseLiveLocationMap.put("longitude", longitude);
                firebaseLiveLocationMap.put("address", userLocation.getAddress_fromLatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)));
                firebaseLiveLocationMap.put("workinghours", (MapConstants.workinghour_from + "-" + MapConstants.workinghour_to));
                firebaseLiveLocationMap.put("allow_tracking", 1);
                firebaseLiveLocationMap.put("transport_mode", modes_transport.get(spinner_mode_of_transport.getSelectedItemPosition()).toLowerCase());
                //FirebaseLiveLocation firebaseLiveLocation = dataSnapshot.getValue(FirebaseLiveLocation.class);
                if (!dataSnapshot.exists()) {
                    databaseReference.child(String.valueOf(sharedPreferences.getInt("staffid", 0))).setValue(firebaseLiveLocationMap);

                } else {
                    //if (firebaseLiveLocation != null && (Double.parseDouble(latitude) >= 17.3)) {
                    databaseReference.child(String.valueOf(sharedPreferences.getInt("staffid", 0))).updateChildren(firebaseLiveLocationMap);
                    //}

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        startStep1();

        //add_data_toSqlite();
    }

    private void update_firebase() {
        Map<String, Object> firebaseLiveLocationMap = new HashMap<>();
        firebaseLiveLocationMap.put("allow_tracking", 0);
        databaseReference.child(String.valueOf(sharedPreferences.getInt("staffid", 0))).updateChildren(firebaseLiveLocationMap);
    }

    private void add_data_toSqlite(int flag) {
        userLocation = new UserLocation(LiveTrackingActivity.this);
        ContentValues c = new ContentValues();
        c.put(DatabaseHelper.STAFF_ID, Constants.staffid);
        c.put(DatabaseHelper.STAFF_NAME, staffDetails.getPName());
        c.put(DatabaseHelper.LATITUDE, userLocation.getLatitude());
        c.put(DatabaseHelper.LONGITUDE, userLocation.getLongitude());
        c.put(DatabaseHelper.ADDRESS, userLocation.getAddress());
        c.put(DatabaseHelper.WORKING_HOUR_FROM, MapConstants.workinghour_from);
        c.put(DatabaseHelper.WORKING_HOUR_TO, MapConstants.workinghour_to);
        c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
        if (flag == 1) {
            c.put(DatabaseHelper.ALLOW_TRACKING, 1);
            DatabaseHelper.getInstance(LiveTrackingActivity.this).save_location(c, Constants.staffid);
            add_live_updates_to_firebase(String.valueOf(userLocation.getLatitude()), String.valueOf(userLocation.getLongitude()));

        } else {
            c.put(DatabaseHelper.ALLOW_TRACKING, 0);
            DatabaseHelper.getInstance(LiveTrackingActivity.this).save_location(c, Constants.staffid);
        }


    }

    private boolean check_param_ok() {
        userLocation = new UserLocation(LiveTrackingActivity.this);
        if (userLocation.isGpsEnabled()) {

            if (userLocation.getLatitude() > 0 && userLocation.getLongitude() > 0 && !userLocation.getAddress().trim().equals("")) {
                return true;
            } else {
                Toast.makeText(LiveTrackingActivity.this, getResources().getString(R.string.unabletofindlocation), Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            userLocation.gpsNotEnabled_Alert();
            return false;

        }
    }

    private void live_track() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(Constants.LOCATION_INTERVAL);
        mLocationRequest.setFastestInterval(Constants.FASTEST_LOCATION_INTERVAL);

        add_data_toSqlite(1);

        startService(new Intent(this, LocationMonitoringService.class));

        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        final String latitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE);
                        final String longitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE);
                        if (latitude != null && longitude != null) {
                            //Log.d("Live Location ", "\n Latitude : " + latitude + "\n Longitude: " + longitude);

                            add_live_updates_to_firebase(latitude, longitude);
                        }

                    }
                }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
        );


    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedPreferences != null && sharedPreferences.contains("staffid"))
            DatabaseHelper.getInstance(LiveTrackingActivity.this).get_LiveLocationUpdate(sharedPreferences.getInt("staffid", 0));

    }

    private void startStep1() {
        if (isGooglePlayServicesAvailable()) {
            startStep2(null);

        } else {
            Toast.makeText(getApplicationContext(), R.string.no_google_playservice_available, Toast.LENGTH_LONG).show();
        }
    }

    private Boolean startStep2(DialogInterface dialog) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            promptInternetConnect();
            return false;
        }


        if (dialog != null) {
            dialog.dismiss();
        }

        if (checkPermissions()) {
            startStep3();
        } else {
            requestPermissions();
        }
        return true;
    }

    private void promptInternetConnect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LiveTrackingActivity.this);
        builder.setTitle(R.string.title_alert_no_intenet);
        builder.setMessage(R.string.msg_alert_no_internet);

        String positiveText = getString(R.string.btn_label_refresh);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (startStep2(dialog)) {

                            if (checkPermissions()) {

                                startStep3();
                            } else if (!checkPermissions()) {
                                requestPermissions();
                            }

                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void startStep3() {
        if (!mAlreadyStartedService) {

            Intent intent = new Intent(this, LocationMonitoringService.class);
            startService(intent);

            mAlreadyStartedService = true;

        }
    }

    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }
            return false;
        }
        return true;
    }


    private boolean checkPermissions() {
        int permissionState1 = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        int permissionState2 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        return permissionState1 == PackageManager.PERMISSION_GRANTED && permissionState2 == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermissions() {

        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

        boolean shouldProvideRationale2 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        if (shouldProvideRationale || shouldProvideRationale2) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            ActivityCompat.requestPermissions(LiveTrackingActivity.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");

            ActivityCompat.requestPermissions(LiveTrackingActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {

                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.i(TAG, "Permission granted, updates requested, starting location updates");
                startStep3();

            } else {

                showSnackbar(R.string.permission_denied_explanation,
                        R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.google_map, menu);

        MenuItem item = menu.getItem(0);

        if (sharedPreferences != null && sharedPreferences.contains("deletestaffid")) {
            if (sharedPreferences.getString("deletestaffid", "").equalsIgnoreCase("5448") ||sharedPreferences.getString("deletestaffid", "").equalsIgnoreCase("5499") ) {
                item.setVisible(true);
            } else {
                item.setVisible(false);
            }
        }else {
            item.setVisible(false);
        }


        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.google_map:
                Intent i = new Intent(LiveTrackingActivity.this, GoogleMapActivity.class);
                startActivity(i);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {

        //stopService(new Intent(this, LocationMonitoringService.class));
        //mAlreadyStartedService = false;

        super.onDestroy();
    }
}
