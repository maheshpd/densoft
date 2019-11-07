package com.densoftinfotech.densoftpaysmart;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.densoftinfotech.densoftpaysmart.adapter.MarkAttendanceAdapter;
import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.app_utilities.URLS;
import com.densoftinfotech.densoftpaysmart.classes.MarkAttendanceDetails;
import com.densoftinfotech.densoftpaysmart.location_utilities.UserLocation;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.retrofit.GetServiceInterface;
import com.densoftinfotech.densoftpaysmart.retrofit.RetrofitClient;
import com.densoftinfotech.densoftpaysmart.room_database.Paysmart_roomdatabase;
import com.densoftinfotech.densoftpaysmart.room_database.Staff.StaffDetailsRoom;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.pdf.parser.Line;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MarkAttendanceActivity extends CommonActivity {

    @BindView(R.id.tv_checkin)
    TextView tv_checkin;
    @BindView(R.id.tv_checkout)
    TextView tv_checkout;
    @BindView(R.id.tv_checkintime)
    TextView tv_checkintime;
    @BindView(R.id.tv_checkouttime)
    TextView tv_checkouttime;
    @BindView(R.id.recycler_view_markattendance)
    RecyclerView recycler_view_markattendance;
    @BindView(R.id.linearLayout11)
    LinearLayout linearLayout11;
    @BindView(R.id.spinner_month)
    Spinner spinner_month;
    @BindView(R.id.spinner_year)
    Spinner spinner_year;

    UserLocation userLocation;
    String longitude = "";
    String latitude = "";
    int year_of_joining = 2000;

    String month, year = "";

    RecyclerView.LayoutManager layoutManager;
    MarkAttendanceAdapter markAttendanceAdapter;

    GetServiceInterface getServiceInterface;
    ArrayList<MarkAttendanceDetails> markAttendanceDetails = new ArrayList<>();
    private ArrayList<Integer> years = new ArrayList<>();
    private static String[] month_short = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private SharedPreferences preferences;

    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        toolbar_common();
        back();

        ButterKnife.bind(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(MarkAttendanceActivity.this);

        b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("planner_month") && b.containsKey("planner_year")) {
                getset_spinner_data(Integer.parseInt(b.getString("planner_year", "")), Integer.parseInt(b.getString("planner_month", "")), Constants.staffid, 0);
                get_monthandyear(Constants.staffid, b.getString("planner_month", ""), b.getString("planner_year", ""));

            }
        }else{
            Get_Attendance get_attendance = new Get_Attendance();
            get_attendance.execute();
        }

        layoutManager = new LinearLayoutManager(MarkAttendanceActivity.this, LinearLayoutManager.VERTICAL, true);
        recycler_view_markattendance.setLayoutManager(layoutManager);

        tv_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_check_time(0);
            }
        });

        tv_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_check_time(1);
            }
        });


    }

    private void get_attendance_details(String staffid, String month_send, String year_send) {

        Retrofit retrofit = RetrofitClient.getRetrofit();
        getServiceInterface = retrofit.create(GetServiceInterface.class);
        Map<String, Object> params = new HashMap<>();

        params.put("customerid", preferences.getString("customerid", ""));
        params.put("ActionId", "0");
        params.put("StaffId", staffid);
        params.put("Month", month_send);
        params.put("Year", year_send);

        JSONObject obj = new JSONObject(params);
        Log.d("params ", obj + "");

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (obj).toString());

        Call<ArrayList<MarkAttendanceDetails>> call = getServiceInterface.request_attendance(requestBody);
        call.enqueue(new Callback<ArrayList<MarkAttendanceDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<MarkAttendanceDetails>> call, Response<ArrayList<MarkAttendanceDetails>> response) {
                if (!response.isSuccessful()) {
                    Log.d("response code ", response.code() + " ");
                } else {
                    Log.d("response ", response.body() + "");

                    if (response.body() != null && !response.body().isEmpty()) {
                        linearLayout11.setVisibility(View.VISIBLE);
                        recycler_view_markattendance.setVisibility(View.VISIBLE);
                        markAttendanceDetails = response.body();
                        markAttendanceAdapter = new MarkAttendanceAdapter(MarkAttendanceActivity.this, markAttendanceDetails);
                        recycler_view_markattendance.setAdapter(markAttendanceAdapter);
                    } else {
                        linearLayout11.setVisibility(View.GONE);
                        recycler_view_markattendance.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MarkAttendanceDetails>> call, Throwable t) {

            }
        });

    }

    private void set_check_time(int flag) {
        userLocation = new UserLocation(MarkAttendanceActivity.this);
        if (userLocation.isGpsEnabled()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy ', ' HH:mm:ss z");
            String currentDateandTime = sdf.format(new Date());
            if (flag == 0) {
                tv_checkintime.setText(getResources().getString(R.string.checkintime) + " " + currentDateandTime + " at " + userLocation.getAddress());
                tv_checkintime.setVisibility(View.VISIBLE);
            } else {
                tv_checkouttime.setText(getResources().getString(R.string.checkouttime) + " " + currentDateandTime + " at " + userLocation.getAddress());
                tv_checkouttime.setVisibility(View.VISIBLE);
            }
            longitude = Double.toString(userLocation.getLongitude());
            latitude = Double.toString(userLocation.getLatitude());
            Log.d("lat and long checkin ", longitude + "    " + latitude + " address " + userLocation.getAddress());

            /*jsonObject.accumulate("myConStr", "a");
            jsonObject.accumulate("type", type); // in / out
            jsonObject.accumulate("id", id);
            jsonObject.accumulate("lat", latitude);
            jsonObject.accumulate("logi", longitude);
            jsonObject.accumulate("number", "");*/
        } else {
            userLocation.gpsNotEnabled_Alert();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.attendance_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_checkin:
                set_check_time(0);
                break;
            case R.id.menu_checkout:
                set_check_time(1);
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    private class Get_Attendance extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            StaffDetailsRoom staffDetails = Paysmart_roomdatabase.get_PaysmartDatabase(MarkAttendanceActivity.this).staffDetails_dao().getAll();

            if (staffDetails != null) {
                //get_attendance_details(staffDetails.getStaffId(), voids[0]);
                year_of_joining = Integer.parseInt(staffDetails.getJoiningDate().split("-")[2]);

                getset_spinner_data(year_of_joining, Calendar.getInstance().get(Calendar.MONTH), staffDetails.getStaffId(), 1);
            }

            return null;
        }
    }

    private void getset_spinner_data(int year_of_joining, int month1, String staffid, int flag) {

        for (int i = Integer.parseInt(Constants.staffDetailsRoom.getJoiningDate().split("-")[2]); i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            years.add(i);
        }

        ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<Integer>(MarkAttendanceActivity.this, R.layout.custom_spinnerlayout, R.id.text1, years);
        spinner_year.setAdapter(yearAdapter);

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(MarkAttendanceActivity.this, R.layout.custom_spinnerlayout, R.id.text1, month_short);
        spinner_month.setAdapter(monthAdapter);

        if(flag == 1) {
            spinner_month.setSelection(Calendar.getInstance().get(Calendar.MONTH));
            spinner_year.setSelection((years.size() - 1));
        }else{
            spinner_month.setSelection(month1-1);
            spinner_year.setSelection(yearAdapter.getPosition(year_of_joining));
        }

        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                month = spinner_month.getItemAtPosition(i).toString();
                get_monthandyear(staffid, month, year);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year = spinner_year.getItemAtPosition(i).toString();
                get_monthandyear(staffid, month, year);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void get_monthandyear(String staffid, String month, String year1) {

        if (year.equals("")) {
            year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        }else {
            year = year1;
        }


        if (month.equals("")) {
            month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH));
        }

        Log.d("month by broadcast ", month + " MarkAttendanceActivity " + year1);

        switch (month) {
            case "Jan":
            case "1":
                get_attendance_details(staffid, "1", year);
                break;
            case "Feb":
            case "2":
                get_attendance_details(staffid, "2", year);
                break;
            case "Mar":
            case "3":
                get_attendance_details(staffid, "3", year);
                break;
            case "Apr":
            case "4":
                get_attendance_details(staffid, "4", year);
                break;
            case "May":
            case "5":
                get_attendance_details(staffid, "5", year);
                break;
            case "Jun":
            case "6":
                get_attendance_details(staffid, "6", year);
                break;
            case "Jul":
            case "7":
                get_attendance_details(staffid, "7", year);
                break;
            case "Aug":
            case "8":
                get_attendance_details(staffid, "8", year);
                break;
            case "Sep":
            case "9":
                get_attendance_details(staffid, "9", year);
                break;
            case "Oct":
            case "10":
                get_attendance_details(staffid, "10", year);
                break;
            case "Nov":
            case "11":
                get_attendance_details(staffid, "11", year);
                break;
            case "Dec":
            case "12":
                get_attendance_details(staffid, "12", year);
                break;
        }

    }
}



/*final Animation animLeftToRight = AnimationUtils.loadAnimation(MarkAttendanceActivity.this, R.anim.swiperight);
        animLeftToRight.setRepeatMode(Animation.INFINITE);
        tv_checkin.startAnimation(animLeftToRight);

        final Animation animRightToLeft = AnimationUtils.loadAnimation(MarkAttendanceActivity.this, R.anim.swipeleft);
        animRightToLeft.setRepeatMode(Animation.INFINITE);
        tv_checkout.startAnimation(animRightToLeft);*/

        /*final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(3000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = tv_checkin.getWidth();
                final float translationX = width * progress;
                tv_checkin.setTranslationX(translationX);

                final float translationX1 = - (width * progress);
                tv_checkout.setTranslationX(translationX1);
            }
        });
        animator.start();*/
