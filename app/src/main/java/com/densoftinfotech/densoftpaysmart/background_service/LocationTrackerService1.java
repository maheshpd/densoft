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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.densoftinfotech.densoftpaysmart.DensoftApp;
import com.densoftinfotech.densoftpaysmart.MainActivity;
import com.densoftinfotech.densoftpaysmart.R;
import com.densoftinfotech.densoftpaysmart.app_utilities.AutoCounter;
import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.app_utilities.DateUtils;
import com.densoftinfotech.densoftpaysmart.app_utilities.InternetUtils;
import com.densoftinfotech.densoftpaysmart.helper.TrackHelper;


import com.densoftinfotech.densoftpaysmart.location_utilities.MapConstants;
import com.densoftinfotech.densoftpaysmart.location_utilities.UserLocation;
import com.densoftinfotech.densoftpaysmart.model.FirebaseLiveLocation;
import com.densoftinfotech.densoftpaysmart.model.LocalTrack;
import com.densoftinfotech.densoftpaysmart.model.LocationHistoryModel;
import com.densoftinfotech.densoftpaysmart.model.NotificationReceived;
import com.densoftinfotech.densoftpaysmart.sqlitedatabase.DatabaseHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.ConsoleHandler;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import io.realm.Realm;


import static com.densoftinfotech.densoftpaysmart.app_utilities.Constants.staffDetails;
import static com.densoftinfotech.densoftpaysmart.app_utilities.DateUtils.getTime;
import static com.densoftinfotech.densoftpaysmart.sqlitedatabase.DatabaseHelper.DATE;
import static com.densoftinfotech.densoftpaysmart.sqlitedatabase.DatabaseHelper.TABLE_TRACK;

public class LocationTrackerService1 extends Service {

    private static final String TAG = LocationTrackerService1.class.getSimpleName();
    private static String channel_id = "nidhikamath";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.US);
    private TimerTask scanTask;
    private final Handler handler = new Handler();
    private Timer t = new Timer();
    private String stop = "stop";
    private int temp_no = 0;
    private int i = 0;
    Context context;
    UserLocation location;

    Realm realm;
    TrackHelper helper;

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
        context = this;
        context = this.getBaseContext();
        Log.d("called ", "onCreate");

        realm = Realm.getDefaultInstance();
        helper = new TrackHelper(realm);
        helper.selectDB();


        location = new UserLocation(context);

        registerReceiver(stopReceiver, new IntentFilter(stop));
        buildNotification();
        loginToFirebase();
//        start_timer_for_location_history(this);
        startTimeOneMinute();


    }

    private void buildNotification() {

        if (DateUtils.within_office_hours()) {
            Log.d("called ", "build notification enable");
            enable_tracking();
        } else {
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
            Log.d("called ", "received stop broadcast");
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
                    add = obj.getAddressLine(0); //Address386, SVS Rd, Sane Guruji Premises, opposite Siddhi Vinayak Temple, Dadar West, Prabhadevi, Mumbai, Maharashtra 400028, India
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


    private void startTimeOneMinute() {

        Timer timerObj = new Timer();
        timerObj.scheduleAtFixedRate(new MyTimerTask(), 300, 60000);

    }


    private void start_timer_for_location_history(Context context) {
        scanTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {


                        if (DateUtils.within_office_hours()) {
                            UserLocation location = new UserLocation(context);
                            temp_no = AutoCounter.getCounterPlusOne();

                            HashMap<String, Object> firebaselive = new HashMap<>();

                            firebaselive.put("timestamp", System.currentTimeMillis());
                            firebaselive.put("latitude", location.getLatitude());
                            firebaselive.put("longitude", location.getLongitude());
                            firebaselive.put("address", location.getAddress_fromLatLng(location.getLatitude(), location.getLongitude()));
                            firebaselive.put("current_time", getTime(System.currentTimeMillis()));
                            firebaselive.put("angle", 0);
//                            firebaseValue.put(String.valueOf(temp_no),firebaselive);

                            if (InternetUtils.getInstance(context).available()) {
                                // checkDataToPushExists();
                                getallofflinedata();
                                if (location != null && location.getLatitude() != 0 && location.getLongitude() != 0) {


                                } else {
                                    if (location != null && location.getLatitude() != 0 && location.getLongitude() != 0) {

                                        //addDataToOffline(String.valueOf(temp_no),firebaseValue);
                                        // addNode(historyModel);
                                        Log.d("preference ", "location model no internet " + temp_no);
                                    }
                                }

                            } else {

                            }
                        } else {
                            disable_tracking();
                        }
                    }
                });
            }
        };
        t.schedule(scanTask, 300, MapConstants.update_after_every);

    }


    private void addDataToOffline(Map<String, Object> inputMap, String time, String date) {
        JSONObject jsonObject = new JSONObject(inputMap);
        String jsonString = jsonObject.toString();
        DatabaseHelper.getInstance(context).saveTrackData(time, jsonString, date);

    }


    private void addDataIntoFirebase(HashMap firebaseValuelive, String timerPoint, String date) {


        ref.child(String.valueOf(String.valueOf(preferences.getInt("staffid", 0)))).child(date).child("locationhistory")
                .child(timerPoint).setValue(firebaseValuelive).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                DeleteFromSqlite(Integer.parseInt(timerPoint), Integer.parseInt(date));
                Log.d("preference ", "success location model " + temp_no);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                addDataToOffline(firebaseValuelive, timerPoint, date);
            }
        });
    }

    public void DeleteFromSqlite(int time, int date) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getReadableDatabase();
        String query = "Delete FROM " + DatabaseHelper.TABLE_TRACK + " WHERE keys = " + time + " AND date = " + date;
        Cursor c = db.rawQuery(query, null);
        while (c.moveToNext()) {
            Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
        }
    }

    public void getallofflinedata() {
        Cursor res = DatabaseHelper.getInstance(this).getAllData();
        if (res.getCount() == 0) {
            return;
        }

        while (res.moveToNext()) {
            String key = res.getString(1);
            String value = res.getString(2);
            String date = res.getString(3);

            try {
                Map<String, Object> response = new ObjectMapper().readValue(value, HashMap.class);

                String address = response.get("address").toString();
                String latitude = response.get("latitude").toString();
                String angle = response.get("angle").toString();
                String current_time = response.get("current_time").toString();
                String timestamp = response.get("timestamp").toString();
                String longitude = response.get("longitude").toString();

                HashMap<String, Object> firebaselive = new HashMap<>();
                firebaselive.put("address", address);
                firebaselive.put("angle", angle);
                firebaselive.put("latitude", latitude);
                firebaselive.put("current_time", current_time);
                firebaselive.put("timestamp", timestamp);
                firebaselive.put("longitude", longitude);
                addDataIntoFirebase(firebaselive, key, date);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }


//        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
//        String query = "SELECT * FROM " + DatabaseHelper.TABLE_TRACK  ;
//        Cursor c = db.rawQuery(query, null);
//
//        if (c.moveToFirst()) {
//
//            String jsonValue = c.getString(2);


//            String jsonValue = c.getColumnName(2);
//            try {
//                Map<String, Object> carMap = jsonValue.readValue(new File(
//                        c.getColumnName(2)), new TypeReference<Map<String, Object>>() {
//                });
//
//                System.out.println("address : " + carMap.get("address"));
//                System.out.println("angle : " + carMap.get("angle"));
//                System.out.println("latitude : " + carMap.get("latitude"));
//                System.out.println("longitude : " + carMap.get("longitude"));
//                System.out.println("timestamp : " + carMap.get("timestamp"));
//                System.out.println("current_time : " + carMap.get("current_time"));
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        ObjectMapper mapper = new ObjectMapper();


//        return c;

//        if (c.getCount() > 0) {
//            if (c.moveToFirst()) {
//
//
//            }

//                do {
//                    String cg = c.getColumnName(1);
//
//                } while (c.moveToFirst());
//            }
//        }

//    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            if (DateUtils.within_office_hours()) {
                enable_tracking();
            } else {
                disable_tracking();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enable_tracking() {
        if (preferences.getInt("staffid", 0) != 0) {
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
    }

    private void disable_tracking() {
        if (preferences.getInt("staffid", 0) != 0) {
            HashMap<String, Object> update = new HashMap<>();
            update.put("allow_tracking", 0);
            ref.child(String.valueOf(preferences.getInt("staffid", 0))).updateChildren(update);

            stopForeground(true);
            stopSelf();
        }
    }


    private void addNode(LocationHistoryModel locationHistoryModel) {

//        HashMap<String, Object> share1 = new HashMap<>();
//        HashMap<String, Object> share2 = new HashMap<>();
//        HashMap<String, Object> share3 = new HashMap<>();
//
//        share1.put("data",nodeindex)


        if (preferences != null) {
            editor = preferences.edit();
            Gson gson = new Gson();

            if (preferences.contains("locationmodels") && !preferences.getString("locationmodels", "").trim().equals("")) {
                String locationmodels = preferences.getString("locationmodels", "");
                Type type = new TypeToken<ArrayList<LocationHistoryModel>>() {
                }.getType();
                ArrayList<LocationHistoryModel> userMode = gson.fromJson(locationmodels, type);
                userMode.add(locationHistoryModel);
                String stringput = gson.toJson(userMode);
                editor.putString("locationmodels", stringput);
                editor.apply();
            } else {
                ArrayList<LocationHistoryModel> locationHistoryModels = new ArrayList<>();
                locationHistoryModels.add(locationHistoryModel);
                String stringput = gson.toJson(locationHistoryModels);
                editor.putString("locationmodels", stringput);
                editor.apply();
            }

        }
    }

    private void checkDataToPushExists() {

        try {
            if (preferences.contains("locationmodels") && !preferences.getString("locationmodels", "").trim().equals("")) {

                String loc = preferences.getString("locationmodels", "");
                if (!loc.trim().equals("")) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<LocationHistoryModel>>() {
                    }.getType();
                    ArrayList<LocationHistoryModel> locupdate = gson.fromJson(loc, type);

                    int size = locupdate.size();
                    for (i = (size - 1); i >= 0; i--) {


                        if (locupdate.get(i) != null) {

                            ref.child(String.valueOf(String.valueOf(preferences.getInt("staffid", 0)))).child(String.valueOf(DateUtils.getDate())).child("locationhistory").child(String.valueOf(i)).setValue(locupdate.get(i))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            try {
                                                Log.d("preference ", "success offline location model " + i);

                                                locupdate.set(i, null);

                                                String stringput = gson.toJson(locupdate);
                                                editor.putString("locationmodels", stringput);
                                                editor.apply();
                                                Log.d("preference updated ", preferences.getString("locationmodels", ""));
                                                //add null object on success because the node is already added
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                        } else if (i == 0) {
                            break;
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            if (DateUtils.within_office_hours()) {
                temp_no = AutoCounter.getCounterPlusOne();

                HashMap<String, Object> firebaselive = new HashMap<>();

                firebaselive.put("timestamp", System.currentTimeMillis());
                firebaselive.put("latitude", location.getLatitude());
                firebaselive.put("longitude", location.getLongitude());
                firebaselive.put("address", location.getAddress_fromLatLng(location.getLatitude(), location.getLongitude()));
                firebaselive.put("current_time", getTime(System.currentTimeMillis()));
                firebaselive.put("angle", 0);

                String date = getTime(System.currentTimeMillis());
                String spliteDate = date.replace(":", "");


                if (InternetUtils.getInstance(context).available()) {
                    // checkDataToPushExists();
                    getallofflinedata();

                    if (location != null && location.getLatitude() != 0 && location.getLongitude() != 0) {

                        addDataIntoFirebase(firebaselive, spliteDate, String.valueOf(DateUtils.getDate()));

                    } else {
                        if (location != null && location.getLatitude() != 0 && location.getLongitude() != 0) {

                            addDataToOffline(firebaselive, spliteDate, String.valueOf(DateUtils.getDate()));
                            Log.d("preference ", "location model no internet " + temp_no);
                        }
                    }

                } else {
                    addDataToOffline(firebaselive, spliteDate, String.valueOf(DateUtils.getDate()));
                }
            } else {
                disable_tracking();
            }
        }
    }


}