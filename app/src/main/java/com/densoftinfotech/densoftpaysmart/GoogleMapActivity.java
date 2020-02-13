package com.densoftinfotech.densoftpaysmart;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.app_utilities.InternetUtils;
import com.densoftinfotech.densoftpaysmart.model.FirebaseLiveLocation;
import com.densoftinfotech.densoftpaysmart.location_utilities.DirectionJSONParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.ui.IconGenerator;

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

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;

public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap googlemap;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    Button btn_search;
    ImageView search, cancel;
    AutoCompleteTextView actv_search;
    ArrayList<String> firebaseList_search = new ArrayList<>();
    ArrayAdapter<String> adapter_search;
    SharedPreferences preferences;

    HashMap<String, MarkerOptions> markerMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        btn_search = findViewById(R.id.btn_search);
        search = findViewById(R.id.search1);
        cancel = findViewById(R.id.cancel);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(GoogleMapActivity.this);

        if (preferences.contains("company_name")) {
            databaseReference = firebaseDatabase.getReference(Constants.firebase_database_name + "/" + preferences.getString("company_name", "").replace(".", ""));
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googlemap = googleMap;
        googlemap.animateCamera(CameraUpdateFactory.zoomTo(16));

        live_tracking(googlemap);
    }

    private void live_tracking(GoogleMap googlemap) {


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                IconGenerator iconFactory = new IconGenerator(GoogleMapActivity.this);
                googlemap.clear();
                firebaseList_search.clear();
                if (dataSnapshot.exists()) {
                    //LatLng latLng_office = new LatLng(19.0175853, 72.830392); - Siddhivinayak
                    LatLng latLng_office = new LatLng(21.213530, 79.194070);
                    LatLng latLng1;
                    //markerpoints.add(latLng_office);
                    addIcon(googlemap, iconFactory, "Admin", "Office Location", latLng_office, "Admin");
                    googlemap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng_office, 16.0f));

                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        FirebaseLiveLocation firebaseLiveLocation = children.getValue(FirebaseLiveLocation.class);
                        firebaseList_search.add(firebaseLiveLocation.getStaff_name() + " (Staff id: " + firebaseLiveLocation.getStaff_id() + ")");
                        //if (firebaseLiveLocation != null && firebaseLiveLocation.getAllow_tracking()==1) {
                            //Log.d("datasnapshot child ", " " + firebaseLiveLocation.getLatitude() + "  " + firebaseLiveLocation.getLongitude());
                            if(!firebaseLiveLocation.getLatitude().trim().equalsIgnoreCase("") && !firebaseLiveLocation.getLongitude().trim().equalsIgnoreCase("")){
                                latLng1 = new LatLng(Double.parseDouble(firebaseLiveLocation.getLatitude()), Double.parseDouble(firebaseLiveLocation.getLongitude()));
                                //markerpoints.add(latLng1);

                                draw_routes(latLng_office, latLng1, firebaseLiveLocation.getTransport_mode(), firebaseLiveLocation.getStaff_id());
                            }

                        //}
                    }

                    autocomplete(googlemap);

                } else {
                    Log.d("does not ", "exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void draw_routes(LatLng origin, LatLng dest, String mode_transport, String staff_id) {

        if(InternetUtils.getInstance(GoogleMapActivity.this).available()) {
            String url = getDirectionsUrl(origin, dest, mode_transport);

            DownloadTask downloadTask = new DownloadTask(staff_id);
            downloadTask.execute(url);
        }else{
            Toast.makeText(GoogleMapActivity.this, getResources().getString(R.string.msg_alert_no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void addIcon(GoogleMap googleMap, IconGenerator iconFactory, String text, String staff_name, LatLng latLng, String staff_id) {

        //if(!staff_id.equalsIgnoreCase("Admin")) {
            MarkerOptions markerOptions = new MarkerOptions().
                    icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(text, staff_name))).
                    position(latLng).
                    anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

            markerMap.put(staff_id, markerOptions);
            googleMap.addMarker(markerOptions);
        //}

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

    private void autocomplete(final GoogleMap googleMap) {
        actv_search = findViewById(R.id.actv_search);
        adapter_search = new ArrayAdapter<String>(GoogleMapActivity.this, R.layout.autocomplete_layout, R.id.actv_text, firebaseList_search);
        actv_search.setThreshold(0);
        actv_search.setAdapter(adapter_search);
        actv_search.setTextColor(Color.BLACK);
        actv_search.setTextAppearance(GoogleMapActivity.this, android.R.style.TextAppearance_Small);


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
                //getFilter().filter(queryString);
                searchLocation(googleMap, queryString);
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

    public void searchLocation(GoogleMap googleMap, String querystring) {

        databaseReference.child(querystring.split(":")[1].trim().split("\\)")[0]).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    FirebaseLiveLocation fll = dataSnapshot.getValue(FirebaseLiveLocation.class);
                    if (fll != null) {
                        LatLng latLng = new LatLng(Double.parseDouble(fll.getLatitude()), Double.parseDouble(fll.getLongitude()));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

    /**
     * A method to download json data from url
     */
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

                String distance_duration = parser.getDirectionPolylines(jObject);

                Log.d("distance duration ", distance_duration + "");

                databaseReference.child(staff_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            IconGenerator iconFactory = new IconGenerator(GoogleMapActivity.this);
                            FirebaseLiveLocation firebaseLiveLocation = dataSnapshot.getValue(FirebaseLiveLocation.class);
                            if (firebaseLiveLocation != null) {
                                addIcon(googlemap, iconFactory, firebaseLiveLocation.getStaff_id() + "\nEstimated distance = " + distance_duration.split(",")[0] + "\nEstimated time = "
                                                + distance_duration.split(",")[1],
                                        firebaseLiveLocation.getStaff_name(), new LatLng(Double.parseDouble(firebaseLiveLocation.getLatitude()), Double.parseDouble(firebaseLiveLocation.getLongitude())),
                                        firebaseLiveLocation.getStaff_id());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                //Log.d("line options ", jsonData[0] + " routes " + routes + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
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
            }
        }
    }

    /*private void update_marker(GoogleMap googleMap, String id, double lat, double lng, String route_direct) {

        LatLng ll = null;

        if (markerMap.containsKey(id) && marker!=null) {

            // Update the location.
            marker = markerMap.get(id);
            marker.remove();
            markerMap.remove(id); //added

            ll = new LatLng(lat, lng);
            if (lat != 0 && lng != 0 && !route_direct.isEmpty()) {
                MarkerOptions markerOpt = new MarkerOptions()
                        .title(route_direct).position(ll).visible(true);
                marker = googleMap.addMarker(markerOpt);
                markerMap.put(id, marker); //added

            }

        } else {

            ll = new LatLng(lat, lng);
            if (lat != 0 && lng != 0 && !route_direct.isEmpty()) {
                MarkerOptions markerOpt = new MarkerOptions()
                        .title(route_direct).position(ll).visible(true);
                marker = googleMap.addMarker(markerOpt);
                markerMap.put(id, marker);

            }

        }

    }*/


}
