package com.densoftinfotech.densoftpaysmart;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.classes.FirebaseLiveLocation;
import com.densoftinfotech.densoftpaysmart.location_utilities.LocationMonitoringService;
import com.densoftinfotech.densoftpaysmart.location_utilities.MapServiceInterface;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap googlemap;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    ArrayList<FirebaseLiveLocation> firebaseLiveLocations = new ArrayList<>();
    Button btn_search;

    MapServiceInterface mapServiceInterface;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        btn_search = findViewById(R.id.btn_search);

        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }

        databaseReference = firebaseDatabase.getReference(Constants.firebase_database_name + "/" + Constants.staffDetailsRoom.getCompanyName());


        Gson gson = new GsonBuilder().serializeNulls().create();
        retrofit = new Retrofit.Builder().baseUrl("https://maps.googleapis.com").addConverterFactory(GsonConverterFactory.create(gson)).build();
        mapServiceInterface = retrofit.create(MapServiceInterface.class);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googlemap = googleMap;
        get_latlang_firebase(googlemap);
        googlemap.animateCamera(CameraUpdateFactory.zoomTo(16));
        
        live_tracking(googlemap);

        /*IconGenerator iconFactory = new IconGenerator(GoogleMapActivity.this);

        LatLng latLng = new LatLng(19.0175853, 72.830392);
        addIcon(googlemap, iconFactory, "Admin", "Office Location", latLng);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));*/

    }

    private void live_tracking(GoogleMap googlemap) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    FirebaseLiveLocation firebaseLiveLocation = children.getValue(FirebaseLiveLocation.class);
                    if(firebaseLiveLocation!=null) {
                        if (firebaseLiveLocation.getAddress() != null && !firebaseLiveLocation.getAddress().isEmpty()){

                        }
                    }
                }

                /*IconGenerator iconFactory = new IconGenerator(GoogleMapActivity.this);

                if (dataSnapshot.exists()) {
                    LatLng latLng1 = new LatLng(19.0175853, 72.830392);
                    addIcon(googlemap, iconFactory, "Admin", "Office Location", latLng1);
                    googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 16.0f));

                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        FirebaseLiveLocation firebaseLiveLocation = children.getValue(FirebaseLiveLocation.class);
                        firebaseLiveLocations.add(firebaseLiveLocation);
                        if (firebaseLiveLocation != null) {
                            //Log.d("datasnapshot child ", " " + firebaseLiveLocation.getLatitude() + "  " + firebaseLiveLocation.getLongitude());

                            addIcon(googlemap, iconFactory, firebaseLiveLocation.getStaff_id(), firebaseLiveLocation.getStaff_name(), new LatLng(Double.parseDouble(firebaseLiveLocation.getLatitude()), Double.parseDouble(firebaseLiveLocation.getLongitude())));
                        }
                    }

                    btn_search.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            searchLocation(googlemap);
                        }
                    });


                } else {
                    Log.d("does not ", "exist");
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void get_latlang_firebase(GoogleMap googleMap) {


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FirebaseLiveLocation firebaseLiveLocation = dataSnapshot.getValue(FirebaseLiveLocation.class);
                LatLng latLng;
                IconGenerator iconFactory = new IconGenerator(GoogleMapActivity.this);
                if (firebaseLiveLocation != null) {
                    latLng = new LatLng(Double.parseDouble(firebaseLiveLocation.getLatitude()), Double.parseDouble(firebaseLiveLocation.getLongitude()));
                    moveMarker(googleMap, iconFactory, firebaseLiveLocation, new LatLng(Double.parseDouble(firebaseLiveLocation.getLatitude()), Double.parseDouble(firebaseLiveLocation.getLongitude())));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                }


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                IconGenerator iconFactory = new IconGenerator(GoogleMapActivity.this);
                googleMap.clear();
                if (dataSnapshot.exists()) {
                    LatLng latLng1 = new LatLng(19.0175853, 72.830392);
                    addIcon(googlemap, iconFactory, "Admin", "Office Location", latLng1);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 16.0f));

                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        FirebaseLiveLocation firebaseLiveLocation = children.getValue(FirebaseLiveLocation.class);
                        firebaseLiveLocations.add(firebaseLiveLocation);
                        if (firebaseLiveLocation != null) {
                            //Log.d("datasnapshot child ", " " + firebaseLiveLocation.getLatitude() + "  " + firebaseLiveLocation.getLongitude());

                            addIcon(googleMap, iconFactory, firebaseLiveLocation.getStaff_id(), firebaseLiveLocation.getStaff_name(), new LatLng(Double.parseDouble(firebaseLiveLocation.getLatitude()), Double.parseDouble(firebaseLiveLocation.getLongitude())));
                        }
                    }

                    btn_search.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            searchLocation(googleMap);
                        }
                    });


                } else {
                    Log.d("does not ", "exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }

    private void moveMarker(GoogleMap googleMap, IconGenerator iconFactory, FirebaseLiveLocation firebaseLiveLocation, LatLng latLng) {

        Marker marker = googleMap.addMarker(new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(firebaseLiveLocation.getStaff_id(), firebaseLiveLocation.getStaff_name()))).
                position(latLng).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV()));

        marker.setPosition(latLng);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


    }

    private void addIcon(GoogleMap googleMap, IconGenerator iconFactory, String staff_id, String staff_name, LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(staff_id, staff_name))).
                position(latLng).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        googleMap.addMarker(markerOptions);
        LatLng latLng1 = new LatLng(19.0175853, 72.830392);

    }

    private Bitmap getMarkerBitmapFromView(String staff_id, String staff_name) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_mapmarker_layout, null);
        ImageView markerImageView = customMarkerView.findViewById(R.id.iv_marker);
        TextView tv_nameofstaff = customMarkerView.findViewById(R.id.tv_nameofstaff);
        TextView tv_staffid = customMarkerView.findViewById(R.id.tv_staffid);
        markerImageView.setImageResource(R.mipmap.map_marker);
        tv_nameofstaff.setText(staff_name + "");
        tv_staffid.setText("Staff id: " + staff_id);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    public void searchLocation(GoogleMap googleMap) {
        EditText locationSearch = findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        Log.d("data is ", firebaseLiveLocations.toString() + "");

        if (firebaseLiveLocations != null) {

            for (int i = 0; i < firebaseLiveLocations.size(); i++) {
                if (firebaseLiveLocations.get(i).getStaff_name().toLowerCase().trim().contains(location.toLowerCase().trim()) ||
                        firebaseLiveLocations.get(i).getStaff_id().toLowerCase().trim().contains(location.toLowerCase().trim())) {
                    LatLng latLng = new LatLng(Double.parseDouble(firebaseLiveLocations.get(i).getLatitude()), Double.parseDouble(firebaseLiveLocations.get(i).getLongitude()));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }


        }
    }

}
