package com.densoftinfotech.densoftpaysmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.app_utilities.DateUtils;
import com.densoftinfotech.densoftpaysmart.classes.FirebaseLiveLocation;
import com.densoftinfotech.densoftpaysmart.location_utilities.UserLocation;
import com.densoftinfotech.densoftpaysmart.map.MapConstants;
import com.densoftinfotech.densoftpaysmart.sqlitedatabase.DatabaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TravelClaimsActivity extends CommonActivity {

    TextView tv_trackme;
    ImageView iv_seemap;
    private UserLocation userLocation;
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_claims);

        tv_trackme = findViewById(R.id.tv_trackme);
        iv_seemap = findViewById(R.id.iv_seemap);

        databaseReference = firebaseDatabase.getReference(Constants.firebase_database_name + "/" + Constants.staffDetailsRoom.getCompanyName()) ;
        setTitle(getResources().getString(R.string.track_me));
        back();

        tv_trackme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check_param_ok()) {
                    add_live_updates_to_firebase();
                }
            }
        });

        iv_seemap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TravelClaimsActivity.this, GoogleMapActivity.class);
                startActivity(i);
            }
        });

    }

    private void add_live_updates_to_firebase() {
        Map<String, Object> firebaseLiveLocationMap = new HashMap<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                firebaseLiveLocationMap.put(Constants.staffid, new FirebaseLiveLocation(Constants.staffid, Constants.staffDetailsRoom.getPName(), String.valueOf(userLocation.getLatitude()),
                        String.valueOf(userLocation.getLongitude()), userLocation.getAddress(), (MapConstants.workinghour_from + "-" + MapConstants.workinghour_to)));
                if(!dataSnapshot.exists()){

                    databaseReference.setValue(firebaseLiveLocationMap);
                }else {

                    databaseReference.updateChildren(firebaseLiveLocationMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        add_data_toSqlite();
    }

    private void add_data_toSqlite() {
        ContentValues c = new ContentValues();
        c.put(DatabaseHelper.STAFF_ID, Constants.staffid);
        c.put(DatabaseHelper.STAFF_NAME, Constants.staffDetailsRoom.getPName());
        c.put(DatabaseHelper.LATITUDE, userLocation.getLatitude());
        c.put(DatabaseHelper.LONGITUDE, userLocation.getLongitude());
        c.put(DatabaseHelper.ADDRESS, userLocation.getAddress());
        c.put(DatabaseHelper.WORKING_HOUR_FROM, MapConstants.workinghour_from);
        c.put(DatabaseHelper.WORKING_HOUR_TO, MapConstants.workinghour_to);
        c.put(DatabaseHelper.SAVEDTIME, DateUtils.getSqliteTime());
        DatabaseHelper.getInstance(TravelClaimsActivity.this).save_location(c);
    }

    private boolean check_param_ok() {
        userLocation = new UserLocation(TravelClaimsActivity.this);
        if (userLocation.isGpsEnabled()) {

            if (userLocation.getLatitude() > 0 && userLocation.getLongitude() > 0 && !userLocation.getAddress().trim().equals("")) {
                return true;
            } else {
                Toast.makeText(TravelClaimsActivity.this, getResources().getString(R.string.unabletofindlocation), Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            userLocation.gpsNotEnabled_Alert();
            return false;

        }
    }
}
