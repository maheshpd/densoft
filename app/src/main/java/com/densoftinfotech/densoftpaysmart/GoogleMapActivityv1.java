package com.densoftinfotech.densoftpaysmart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.densoftinfotech.densoftpaysmart.adapter.GoogleMapAdapter;
import com.densoftinfotech.densoftpaysmart.app_utilities.CircularImageView;
import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.app_utilities.InternetUtils;
import com.densoftinfotech.densoftpaysmart.app_utilities.LinearLayoutManagerWrapper;
import com.densoftinfotech.densoftpaysmart.location_utilities.DirectionJSONParser;
import com.densoftinfotech.densoftpaysmart.location_utilities.LocationTrackerService;
import com.densoftinfotech.densoftpaysmart.model.FirebaseLiveLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.ui.IconGenerator;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Observable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GoogleMapActivityv1 extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap googlemap;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    Button btn_search;
    ImageView search, cancel;
    AutoCompleteTextView actv_search;
    ArrayList<String> firebaseList_search = new ArrayList<>();
    ArrayAdapter<String> adapter_search;
    SharedPreferences preferences;
    String distance_duration = "";

    int pos = 0;
    HashMap<String, Marker> markerMap = new HashMap<>();
    LatLng latLng_office = null;

    GoogleMapAdapter googleMapAdapter;
    ArrayList<FirebaseLiveLocation> firebaseLiveLocations = new ArrayList<>();
    RecyclerView recycler_view;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        btn_search = findViewById(R.id.btn_search);
        actv_search = findViewById(R.id.actv_search);
        search = findViewById(R.id.search1);
        cancel = findViewById(R.id.cancel);
        recycler_view = findViewById(R.id.recycler_view);

        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(GoogleMapActivityv1.this);

        if (preferences.contains("customerid")) {
            databaseReference = firebaseDatabase.getReference(Constants.firebase_database_name + "/" + preferences.getInt("customerid", 0));
        }

        LocalBroadcastManager.getInstance(GoogleMapActivityv1.this).registerReceiver(rec1, new IntentFilter("notifymap"));


        layoutManager = new LinearLayoutManager(GoogleMapActivityv1.this);
        recycler_view.setLayoutManager(layoutManager);
        googleMapAdapter = new GoogleMapAdapter(GoogleMapActivityv1.this, firebaseLiveLocations);
        recycler_view.setAdapter(googleMapAdapter);
        /*DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;*/


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googlemap = googleMap;
        googleMap.setMaxZoomPreference(30);

        latLng_office = new LatLng(Constants.current_loc_latitude, Constants.current_loc_longitude);
        setOfficeMarker();

        adddata();

        /*Query query = databaseReference.orderByChild("staff_id").equalTo("87");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("datasnap query ", dataSnapshot.toString());
                FirebaseLiveLocation firebaseLiveLocation = dataSnapshot.getValue(FirebaseLiveLocation.class);
                firebaseLiveLocations.add(firebaseLiveLocation);
                googleMapAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng_office, 16.0f));
    }

    private void adddata() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                firebaseList_search.clear();
                firebaseList_search.add("View All");
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    if (children != null) {
                        FirebaseLiveLocation firebaseLiveLocation = children.getValue(FirebaseLiveLocation.class);
                        firebaseList_search.add(firebaseLiveLocation.getStaff_name() + " (Staff id: " + firebaseLiveLocation.getStaff_id() + ")");
                        setMarker(children, "");
                    }

                }
                autocomplete();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                firebaseLiveLocations.clear();
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    if (children != null) {
                        FirebaseLiveLocation firebaseLiveLocation = children.getValue(FirebaseLiveLocation.class);
                        firebaseLiveLocations.add(firebaseLiveLocation);
                    }

                }
                googleMapAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/



        databaseReference.limitToLast(150).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FirebaseLiveLocation liveLocation = dataSnapshot.getValue(FirebaseLiveLocation.class);
                Log.d("value added of staff ", liveLocation.getStaff_id() + "");

                firebaseLiveLocations.add(liveLocation);
                googleMapAdapter.notifyItemInserted(firebaseLiveLocations.size()-1);

                /*UpdateListBackground updateListBackground = new UpdateListBackground();
                updateListBackground.execute();*/

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FirebaseLiveLocation liveLocation = dataSnapshot.getValue(FirebaseLiveLocation.class);
                Log.d("value changed of staff ", liveLocation.getStaff_id() + "");
                for (int i = 0; i < firebaseLiveLocations.size(); i++) {
                    if (firebaseLiveLocations.get(i).getStaff_id() == liveLocation.getStaff_id()) {

                        firebaseLiveLocations.remove(i);
                        firebaseLiveLocations.add(i, liveLocation);
                        googleMapAdapter.notifyItemChanged(i);

                        /*firebaseLiveLocations.remove(i);
                        firebaseLiveLocations.add(0, liveLocation);
                        googleMapAdapter.notifyItemMoved(i, 0);*/

                       /* UpdateListBackground updateListBackground = new UpdateListBackground();
                        updateListBackground.execute(firebaseLiveLocations);*/
                        break;
                    }
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


    }

    private void autocomplete() {
        actv_search = findViewById(R.id.actv_search);
        adapter_search = new ArrayAdapter<String>(GoogleMapActivityv1.this, R.layout.autocomplete_layout, R.id.actv_text, firebaseList_search);
        actv_search.setThreshold(100);
        actv_search.setAdapter(adapter_search);
        actv_search.setTextColor(Color.BLACK);
        actv_search.setTextAppearance(GoogleMapActivityv1.this, android.R.style.TextAppearance_Small);


        actv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actv_search.showDropDown();
            }
        });

        actv_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //searchLocation(googleMap, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        actv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String queryString = (String) parent.getItemAtPosition(position);
                searchLocation(queryString);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actv_search.setText("");
                //searchLocation(googleMap, "");
            }
        });
    }

    public void searchLocation(String querystring) {
        //googlemap.clear();
        if (querystring.equalsIgnoreCase("View All")) {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {

                                    if (dataSnapshot.exists()) {
                                        LatLng latLng1;
                                        setOfficeMarker();

                                        for (DataSnapshot children : dataSnapshot.getChildren()) {
                                            FirebaseLiveLocation firebaseLiveLocation = children.getValue(FirebaseLiveLocation.class);
                                            if (firebaseLiveLocation != null && firebaseLiveLocation.getAllow_tracking() == 1) {
                                                if (firebaseLiveLocation.getLatitude() != 0 && firebaseLiveLocation.getLongitude() != 0) {
                                                    latLng1 = new LatLng(firebaseLiveLocation.getLatitude(), firebaseLiveLocation.getLongitude());
                                                    //draw_routes(latLng_office, latLng1, firebaseLiveLocation.getTransport_mode(), String.valueOf(firebaseLiveLocation.getStaff_id()));
                                                }

                                            }
                                        }

                                    } else {
                                        Log.d("does not ", "exist");
                                    }


                                }
                            },
                            10000);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            if (querystring.split(":").length > 1) {
                databaseReference.child(querystring.split(":")[1].trim().split("\\)")[0]).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        /*new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {*/

                        if (dataSnapshot.exists()) {
                            FirebaseLiveLocation fll = dataSnapshot.getValue(FirebaseLiveLocation.class);
                            if (fll != null) {

                                if (fll.getStaff_id() == Integer.parseInt(querystring.split(":")[1].trim().split("\\)")[0])) {
                                    setOfficeMarker();
                                    LatLng latLng = new LatLng(fll.getLatitude(), fll.getLongitude());
                                    setMarker(dataSnapshot, "");
                                    //draw_routes(latLng_office, latLng, fll.getTransport_mode(), String.valueOf(fll.getStaff_id()));
                                    googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f));
                                }
                            }
                        }
                                    /*}
                                },
                                5000);*/
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

    }


    private void setOfficeMarker() {
        IconGenerator iconFactory = new IconGenerator(GoogleMapActivityv1.this);
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView("Admin", "Office Location", "")))
                .position(latLng_office)
                .snippet("Admin")
                .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());


        if (!markerMap.containsKey("Admin")) {
            markerMap.put("Admin", googlemap.addMarker(markerOptions));
        } else {
            markerMap.get("Admin").setPosition(latLng_office);
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markerMap.values()) {
            builder.include(marker.getPosition());
        }

        googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng_office, 16.0f));

    }

    private void setMarker(DataSnapshot dataSnapshot, String distance_duration) {
        // When a location update is received, put or update
        // its value in markerMap, which contains all the markers
        // for locations received, so that we can build the
        // boundaries required to show them all on the map at once


        String key = dataSnapshot.getKey();
        HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();

        //Log.d("map val ", value + "");
        double lat = Double.parseDouble(value.get("latitude").toString());
        double lng = Double.parseDouble(value.get("longitude").toString());
        LatLng latLng = new LatLng(lat, lng);
        IconGenerator iconFactory = new IconGenerator(GoogleMapActivityv1.this);

        MarkerOptions markerOptions = null;

        Log.d("distance new ", distance_duration);

        if (distance_duration.split(",").length > 1) {
            markerOptions = new MarkerOptions().
                    icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView((value.get("staff_id").toString() + "\nEstimated distance = " + distance_duration.split(",")[0] + "\nEstimated time = "
                                    + distance_duration.split(",")[1]),
                            value.get("staff_name").toString(), value.get("photo_url").toString())))
                    .position(latLng)
                    .snippet(value.get("staff_id").toString())
                    .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        } else {
            markerOptions = new MarkerOptions().
                    icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView((value.get("staff_id").toString()),
                            value.get("staff_name").toString(), value.get("photo_url").toString())))
                    .position(latLng)
                    .snippet(value.get("staff_id").toString())
                    .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
        }

        LatLng location = new LatLng(lat, lng);
        if (!markerMap.containsKey(key)) {
            markerMap.put(key, googlemap.addMarker(markerOptions));
        } else {
            Objects.requireNonNull(markerMap.get(key)).setPosition(location);
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markerMap.values()) {
            builder.include(marker.getPosition());
        }

        //draw route on marker click
        /*googlemap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if(marker!=null){
                    Log.d("marker details ", marker.getSnippet()  +"");

                    if(!marker.getSnippet().equalsIgnoreCase("Admin")){
                        databaseReference.child(marker.getSnippet()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                FirebaseLiveLocation firebaseLiveLocation = dataSnapshot.getValue(FirebaseLiveLocation.class);

                                if(firebaseLiveLocation!=null){
                                    draw_routes(latLng_office, marker.getPosition(), firebaseLiveLocation.getTransport_mode(),
                                            marker.getSnippet());

                                    //marker.getSnippet() - had been set to staff id
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                }


                return false;
            }
        });*/

        //Log.d("key for marker ", key + "" );
    }

    private Bitmap getMarkerBitmapFromView(String staff_id, String staff_name, String photo_url) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_mapmarker_layout, null);
        CircularImageView markerImageView = customMarkerView.findViewById(R.id.iv_marker);
        TextView tv_nameofstaff = customMarkerView.findViewById(R.id.tv_nameofstaff);
        TextView tv_staffid = customMarkerView.findViewById(R.id.tv_staffid);
        markerImageView.setImageResource(R.mipmap.map_marker);
        tv_nameofstaff.setText(staff_name + "");
        tv_staffid.setText("Staff id: " + staff_id);


        /*Log.d("photo url ", photo_url );
        if(!photo_url.trim().equals("")) {
            Picasso.get()
                    .load(photo_url)
                    .error(R.mipmap.map_marker)
                    .into(markerImageView);
            *//*Picasso.Builder builder = new Picasso.Builder(GoogleMapActivityv1.this);
            builder.listener(new Picasso.Listener()
            {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                {
                    exception.printStackTrace();
                }
            });
            builder.build().load(photo_url).memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE).into(markerImageView);*//*
        }*/

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

    private void draw_routes(LatLng origin, LatLng dest, String mode_transport, String staff_id) {

        if (InternetUtils.getInstance(GoogleMapActivityv1.this).available()) {
            String url = getDirectionsUrl(origin, dest, mode_transport);

            DownloadTask downloadTask = new DownloadTask(staff_id);
            downloadTask.execute(url);
        } else {
            Toast.makeText(GoogleMapActivityv1.this, getResources().getString(R.string.msg_alert_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest, String mode_transport) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        String key = "key=" + getResources().getString(R.string.map_api_key);

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + key + "&" + sensor + "&" + "mode=" + mode_transport;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        String staff_id = "";

        public DownloadTask(String staff_id) {
            this.staff_id = staff_id;
        }

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service

            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask(staff_id);

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        String staff_id = "";

        public ParserTask(String staff_id) {
            this.staff_id = staff_id;
        }

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionJSONParser parser = new DirectionJSONParser();
                routes = parser.parse(jObject);

                distance_duration = parser.getDirectionPolylines(jObject);

                Log.d("distance duration ", distance_duration + "");

                /*databaseReference.child(staff_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            FirebaseLiveLocation firebaseLiveLocation = dataSnapshot.getValue(FirebaseLiveLocation.class);
                            if (firebaseLiveLocation != null) {
                                setMarker(dataSnapshot, distance_duration);

                                firebaseLiveLocations.get(pos).setEstimated_distance(distance_duration);
                                Intent i1 = new Intent("estimated");
                                i1.putExtra("pos", pos);
                                LocalBroadcastManager.getInstance(GoogleMapActivityv1.this).sendBroadcast(i1);
                                *//*Toast toast = Toast.makeText(GoogleMapActivityv1.this, distance_duration, Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();*//*

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            //draw routes
            /*ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);

                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.RED);
                Log.d("line options ", lineOptions + "");
            }

            // Drawing polyline in the Google Map for the i-th route

            if (lineOptions != null) {
                googlemap.addPolyline(lineOptions);
            }*/
        }
    }

    protected BroadcastReceiver rec1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.hasExtra("querystring")) {
                searchLocation(Objects.requireNonNull(intent.getStringExtra("querystring")));
                actv_search.setText(intent.getStringExtra("querystring"));
                /*pos = intent.getIntExtra("pos", 0);
                try {
                    LatLng latLng = new LatLng(intent.getDoubleExtra("lat", 0), intent.getDoubleExtra("long",0));
                    draw_routes(latLng_office, latLng, intent.getStringExtra("mode"), String.valueOf(intent.getIntExtra("staffid", 0)));
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
        }
    };

    /*protected class UpdateListBackground extends AsyncTask<ArrayList<FirebaseLiveLocation>, Void, Void>{
        @Override
        protected Void doInBackground(ArrayList<FirebaseLiveLocation>... arrayLists) {
            if(googleMapAdapter!=null && arrayLists!=null){
                googleMapAdapter.updateNewList(arrayLists[0]);
            }
            return null;
        }

    }*/

}
