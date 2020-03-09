package com.densoftinfotech.densoftpaysmart;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.densoftinfotech.densoftpaysmart.app_utilities.AutoCounter;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.app_utilities.DateUtils;
import com.densoftinfotech.densoftpaysmart.background_service.LocationTrackerService;
import com.densoftinfotech.densoftpaysmart.background_service.LocationTrackerService1;
import com.densoftinfotech.densoftpaysmart.location_utilities.MapConstants;
import com.densoftinfotech.densoftpaysmart.location_utilities.UserLocation;
import com.densoftinfotech.densoftpaysmart.model.FirebaseLiveLocation;
import com.densoftinfotech.densoftpaysmart.model.StaffDetails;
import com.densoftinfotech.densoftpaysmart.sqlitedatabase.DatabaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import static com.densoftinfotech.densoftpaysmart.app_utilities.DateUtils.getTime;

public class LiveTrackingActivityv1 extends CommonActivity {

    TextView tv_trackme, tv_stoptracking;
    //Spinner spinner_mode_of_transport;
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private SharedPreferences sharedPreferences;
    private StaffDetails staffDetails;
    private ArrayList<FirebaseLiveLocation> firebaseLiveLocations = new ArrayList<>();
    //private ArrayList<String> modes_transport = new ArrayList<>();
    //ArrayAdapter<String> spinner_modes;
    int permission = 2; //denied = -1 and granted = 0

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_tracking);

        tv_trackme = findViewById(R.id.tv_trackme);
        tv_stoptracking = findViewById(R.id.tv_stoptracking);
        //spinner_mode_of_transport = findViewById(R.id.spinner_mode_of_transport);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LiveTrackingActivityv1.this);
        /*modes_transport.add(getResources().getString(R.string.please_select));
        modes_transport.add("Driving");
        modes_transport.add("Cycling");
        modes_transport.add("Walking");
        spinner_modes = new ArrayAdapter<>(LiveTrackingActivityv1.this, R.layout.autocomplete_layout, R.id.actv_text, modes_transport);
        spinner_modes.setDropDownViewResource(R.layout.autocomplete_layout);
        spinner_mode_of_transport.setAdapter(spinner_modes);*/

        databaseReference = firebaseDatabase.getReference(Constants.firebase_database_name/* + "/" + sharedPreferences.getInt("customerid", 0)*/);
        setTitle(getResources().getString(R.string.travel_claims));
        back();

        staffDetails = getStaffDetails(LiveTrackingActivityv1.this);

        update_table();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager != null) {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(LiveTrackingActivityv1.this, getResources().getString(R.string.enable_location), Toast.LENGTH_SHORT).show();
            }
        }

        permission = ContextCompat.checkSelfPermission(LiveTrackingActivityv1.this, Manifest.permission.ACCESS_FINE_LOCATION);

        /*if (permission == PackageManager.PERMISSION_GRANTED) {
            live_track();
        } else {
            ActivityCompat.requestPermissions(LiveTrackingActivityv1.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
        }*/


        tv_trackme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (staffDetails != null) {
                        /*if (modes_transport.get(spinner_mode_of_transport.getSelectedItemPosition()).equalsIgnoreCase(getResources().getString(R.string.please_select))) {
                            Toast.makeText(LiveTrackingActivityv1.this, getResources().getString(R.string.select_mode_of_transport), Toast.LENGTH_SHORT).show();
                        } else {*/
                            open_alert(1);
                        //}
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
        firebaseLiveLocations = DatabaseHelper.getInstance(LiveTrackingActivityv1.this).get_LiveLocationUpdate(sharedPreferences.getInt("staffid", 0));
        if (firebaseLiveLocations != null && firebaseLiveLocations.size() > 0) {
            if (firebaseLiveLocations.get(0).getAllow_tracking() == 1) {
                tv_trackme.setText(getResources().getString(R.string.live_tracking_started));
            } else {
                tv_trackme.setText(getResources().getString(R.string.resume_tracking));
                //update_firebase();
            }
        }
    }

    private void open_alert(int flag) {
        AlertDialog.Builder alert = new AlertDialog.Builder(LiveTrackingActivityv1.this);

        if (flag == 1) {
            alert.setTitle(getResources().getString(R.string.realtime_tracking));
            alert.setMessage(getResources().getString(R.string.alert_message_for_tracking));

            alert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    tv_trackme.setText(getResources().getString(R.string.live_tracking_started));
                    if (permission == PackageManager.PERMISSION_GRANTED) {
                        live_track();
                    }else {
                        ActivityCompat.requestPermissions(LiveTrackingActivityv1.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
                    }

                }
            });
        } /*else {
            alert.setTitle(getResources().getString(R.string.stop_tracking));
            alert.setMessage(getResources().getString(R.string.alert_message_for_stoptracking));

            alert.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    update_firebase();
                    update_table();
                }
            });
        }*/

        alert.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alert.show();
    }


    private void update_firebase() {
        Map<String, Object> firebaseLiveLocationMap = new HashMap<>();
        firebaseLiveLocationMap.put("allow_tracking", 0);
        databaseReference.child(String.valueOf(sharedPreferences.getInt("staffid", 0))).updateChildren(firebaseLiveLocationMap);
    }

    private void live_track() {

        UserLocation userLocation = new UserLocation(LiveTrackingActivityv1.this);

        databaseReference.child(String.valueOf(sharedPreferences.getInt("staffid", 0))).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> firebaseLiveLocationMap = new HashMap<>();
                firebaseLiveLocationMap.put("staff_id", sharedPreferences.getInt("staffid", 0));
                firebaseLiveLocationMap.put("staff_name", staffDetails.getPName());
                firebaseLiveLocationMap.put("photo_url", staffDetails.getStaffPhoto());
                firebaseLiveLocationMap.put("email", staffDetails.getEmail1());
                firebaseLiveLocationMap.put("mobile", staffDetails.getMobile1());
                firebaseLiveLocationMap.put("latitude", userLocation.getLatitude()); //initial
                firebaseLiveLocationMap.put("longitude", userLocation.getLongitude()); //initial
                firebaseLiveLocationMap.put("address", userLocation.getAddress()); //initial
                firebaseLiveLocationMap.put("workinghours", (MapConstants.workinghour_from + "-" + MapConstants.workinghour_to));
                firebaseLiveLocationMap.put("allow_tracking", 1);
                firebaseLiveLocationMap.put("transport_mode", "driving"/*modes_transport.get(spinner_mode_of_transport.getSelectedItemPosition()).toLowerCase()*/);
                firebaseLiveLocationMap.put("timestamp", System.currentTimeMillis());
                firebaseLiveLocationMap.put("angle", 0);
                HashMap<String, Object> firebaselive = new HashMap<>();
                firebaselive.put("timestamp", System.currentTimeMillis());
                firebaselive.put("latitude", userLocation.getLatitude());
                firebaselive.put("longitude", userLocation.getLongitude());
                firebaselive.put("address", userLocation.getAddress());
                firebaselive.put("current_time", getTime(System.currentTimeMillis()));
                firebaselive.put("angle", 0);


                if (!dataSnapshot.exists()) {
                    //databaseReference.child(String.valueOf(sharedPreferences.getInt("staffid", 0))).setValue(firebaseLiveLocationMap);
                    //databaseReference.child(String.valueOf(sharedPreferences.getInt("staffid", 0))).child(String.valueOf(DateUtils.getDate())).child("locationhistory").child(String.valueOf(AutoCounter.getCounterPlusOne())).updateChildren(firebaselive);

                } else {
                    //if (firebaseLiveLocation != null && (Double.parseDouble(latitude) >= 17.3)) {
                    //databaseReference.child(String.valueOf(sharedPreferences.getInt("staffid", 0))).updateChildren(firebaseLiveLocationMap);
                    //databaseReference.child(String.valueOf(sharedPreferences.getInt("staffid", 0))).child(String.valueOf(DateUtils.getDate())).child("locationhistory").child(String.valueOf(AutoCounter.getCounterPlusOne())).updateChildren(firebaselive);
                    //}

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        startService(new Intent(LiveTrackingActivityv1.this, LocationTrackerService1.class));


    }

    //try {
//        SQLiteDatabase db = getReadableDatabase();
//        String query = "SELECT * FROM " + TABLE_NOTIFICATION + " WHERE " + DELETED + " =0";
//        Cursor c = db.rawQuery(query, null);
//        if (c.moveToFirst()) {
//            do {
//                JSONObject obj = new JSONObject();
//                try {
//                    obj.put("id", c.getLong(0));
//                    obj.put("title", c.getString(1));
//                    obj.put("description", c.getString(2));
//                    obj.put("big_picture", c.getString(3));
//                    obj.put("deleted", c.getString(4));
//                    //Log.d("obj_notification", "obj " + obj.toString());
//                    list.add(new NotificationReceived(obj));
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                    //Log.e("error1", "error1" + e);
//                }
//            } while (c.moveToNext());
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//    }




    @Override
    public void onResume() {
        super.onResume();
        if (sharedPreferences != null && sharedPreferences.contains("staffid"))
            DatabaseHelper.getInstance(LiveTrackingActivityv1.this).get_LiveLocationUpdate(sharedPreferences.getInt("staffid", 0));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE && grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            live_track();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.google_map, menu);

        MenuItem item = menu.getItem(0);

        if (sharedPreferences != null && sharedPreferences.contains("deletestaffid")) {
            if (sharedPreferences.getString("deletestaffid", "").equalsIgnoreCase("5448") || sharedPreferences.getString("deletestaffid", "").equalsIgnoreCase("5499")) {
                item.setVisible(true);
            } else {
                item.setVisible(false);
            }
        } else {
            item.setVisible(false);
        }

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.google_map:
                Intent i = new Intent(LiveTrackingActivityv1.this, GoogleMapActivityv1.class);
                startActivity(i);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
