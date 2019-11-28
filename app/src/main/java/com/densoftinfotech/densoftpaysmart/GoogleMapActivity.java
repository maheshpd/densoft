package com.densoftinfotech.densoftpaysmart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.classes.FirebaseLiveLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap googlemap;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    ArrayList<FirebaseLiveLocation> firebaseLiveLocations = new ArrayList<>();
    Button btn_search;

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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googlemap = googleMap;
        get_latlang_firebase(googlemap);
        googlemap.animateCamera(CameraUpdateFactory.zoomTo(16));


        /*LatLng latLng = new LatLng(19.0175853, 72.830392);
        googleMap.addMarker(new MarkerOptions().position(latLng).title("Siddhivinayak Mandir")).showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));*/

    }

    private void get_latlang_firebase(GoogleMap googleMap) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot children : dataSnapshot.getChildren()) {



                        FirebaseLiveLocation firebaseLiveLocation = children.getValue(FirebaseLiveLocation.class);
                        firebaseLiveLocations.add(firebaseLiveLocation);
                        if (firebaseLiveLocation != null) {
                            //Log.d("datasnapshot child ", " " + firebaseLiveLocation.getLatitude() + "  " + firebaseLiveLocation.getLongitude());
                            IconGenerator iconFactory = new IconGenerator(GoogleMapActivity.this);
                            LatLng latLng = new LatLng(Double.parseDouble(firebaseLiveLocation.getLatitude()), Double.parseDouble(firebaseLiveLocation.getLongitude()));

                            addIcon(googleMap, iconFactory, firebaseLiveLocation, new LatLng(Double.parseDouble(firebaseLiveLocation.getLatitude()), Double.parseDouble(firebaseLiveLocation.getLongitude())));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
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
        });
    }

    private void addIcon(GoogleMap googleMap, IconGenerator iconFactory, FirebaseLiveLocation firebaseLiveLocation, LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(firebaseLiveLocation))).
                position(position).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        googleMap.addMarker(markerOptions);

    }

    private Bitmap getMarkerBitmapFromView(FirebaseLiveLocation firebaseLiveLocation) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_mapmarker_layout, null);
        ImageView markerImageView = customMarkerView.findViewById(R.id.iv_marker);
        TextView tv_nameofstaff = customMarkerView.findViewById(R.id.tv_nameofstaff);
        TextView tv_staffid = customMarkerView.findViewById(R.id.tv_staffid);
        markerImageView.setImageResource(R.mipmap.map_marker);
        tv_nameofstaff.setText(firebaseLiveLocation.getStaff_name() + "");
        tv_staffid.setText("Staff id: " + firebaseLiveLocation.getStaff_id());
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

            for(int i = 0; i<firebaseLiveLocations.size(); i++){
                if(firebaseLiveLocations.get(i).getStaff_name().toLowerCase().trim().contains(location.toLowerCase().trim()) ||
                        firebaseLiveLocations.get(i).getStaff_id().toLowerCase().trim().contains(location.toLowerCase().trim())){
                    LatLng latLng = new LatLng(Double.parseDouble(firebaseLiveLocations.get(i).getLatitude()), Double.parseDouble(firebaseLiveLocations.get(i).getLongitude()));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }


        }
    }

}
