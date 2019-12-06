package com.densoftinfotech.densoftpaysmart;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.densoftinfotech.densoftpaysmart.adapter.MarkAttendanceAdapter;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.app_utilities.DateUtils;
import com.densoftinfotech.densoftpaysmart.classes.CheckLeaveStatus;
import com.densoftinfotech.densoftpaysmart.classes.MarkAttendanceDetails;
import com.densoftinfotech.densoftpaysmart.location_utilities.UserLocation;
import com.densoftinfotech.densoftpaysmart.retrofit.GetServiceInterface;
import com.densoftinfotech.densoftpaysmart.retrofit.RetrofitClient;
import com.densoftinfotech.densoftpaysmart.room_database.Paysmart_roomdatabase;
import com.densoftinfotech.densoftpaysmart.room_database.Staff.StaffDetailsRoom;
import com.densoftinfotech.densoftpaysmart.sqlitedatabase.DatabaseHelper;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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

    private UserLocation userLocation;
    private int year_of_joining = 2000;

    private String month, year = "";

    private RecyclerView.LayoutManager layoutManager;
    private MarkAttendanceAdapter markAttendanceAdapter;

    private GetServiceInterface getServiceInterface;
    private ArrayList<MarkAttendanceDetails> markAttendanceDetails = new ArrayList<>();
    private ArrayList<Integer> years = new ArrayList<>();
    private static String[] month_short = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private SharedPreferences preferences;

    private Bundle b;

    private GetServiceInterface apiInterface;

    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat df_date = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat df_time = new SimpleDateFormat("HH:mm");
    private String formattedDate = "";
    private String formattedTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        setTitle(getResources().getString(R.string.myattendancehistory));
        back();

        ButterKnife.bind(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(MarkAttendanceActivity.this);

        b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("planner_month") && b.containsKey("planner_year")) {
                getset_spinner_data(Integer.parseInt(b.getString("planner_year", "")), Integer.parseInt(b.getString("planner_month", "")), Constants.staffid, 0);
                get_monthandyear(Constants.staffid, b.getString("planner_month", ""), b.getString("planner_year", ""));

            }
        } else {
            Get_Attendance get_attendance = new Get_Attendance();
            get_attendance.execute();
        }

        layoutManager = new LinearLayoutManager(MarkAttendanceActivity.this, LinearLayoutManager.VERTICAL, true);
        recycler_view_markattendance.setLayoutManager(layoutManager);

    }


    private boolean check_param_ok() {
        userLocation = new UserLocation(MarkAttendanceActivity.this);
        if (userLocation.isGpsEnabled()) {

            if (userLocation.getLatitude() > 0 && userLocation.getLongitude() > 0 && !userLocation.getAddress().trim().equals("")) {
                return true;
            } else {
                Toast.makeText(MarkAttendanceActivity.this, getResources().getString(R.string.unabletofindlocation), Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            userLocation.gpsNotEnabled_Alert();
            return false;

        }
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
                    //Log.d("response code ", response.code() + " ");
                } else {
                    //Log.d("response ", response.body() + "");

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

    private void send_checkIn_checkOut(int flag) {

        //{"UserID":"50", "lag":"19", "logi":"72", "Mobile":"", "address":"", "CustomerId":0}

        userLocation = new UserLocation(MarkAttendanceActivity.this);

        Retrofit retrofit = RetrofitClient.getRetrofit();
        apiInterface = retrofit.create(GetServiceInterface.class);

        Map<String, Object> params = new HashMap<>();

        params.put("UserID", Constants.staffid);
        params.put("lag", userLocation.getLatitude());
        params.put("logi", userLocation.getLongitude());
        params.put("Mobile", Constants.staffDetailsRoom.getMobile1());
        params.put("address", userLocation.getAddress());
        params.put("CustomerId", preferences.getString("customerid", ""));

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(params)).toString());
        Call<ArrayList<CheckLeaveStatus>> call;
        if (flag == 0) {
            call = apiInterface.request_checkIn(body);
        } else {
            call = apiInterface.request_checkOut(body);
        }


        call.enqueue(new Callback<ArrayList<CheckLeaveStatus>>() {
            @Override
            public void onResponse(Call<ArrayList<CheckLeaveStatus>> call, Response<ArrayList<CheckLeaveStatus>> response) {

                if (response.isSuccessful()) {
                    formattedDate = df_date.format(c.getTime());
                    formattedTime = df_time.format(c.getTime());

                    if (flag == 0) {
                        if (response.body() != null && !response.body().isEmpty()) {
                            linearLayout11.setVisibility(View.VISIBLE);
                            recycler_view_markattendance.setVisibility(View.VISIBLE);
                            markAttendanceDetails.add(new MarkAttendanceDetails(formattedDate, formattedTime));
                            markAttendanceAdapter = new MarkAttendanceAdapter(MarkAttendanceActivity.this, markAttendanceDetails);
                            recycler_view_markattendance.setAdapter(markAttendanceAdapter);
                        } else {
                            linearLayout11.setVisibility(View.GONE);
                            recycler_view_markattendance.setVisibility(View.GONE);
                        }
                        DatabaseHelper.getInstance(MarkAttendanceActivity.this).save_time(Constants.staffid, formattedTime, "0", formattedDate);
                        Toast.makeText(MarkAttendanceActivity.this, getResources().getString(R.string.checkinsuccess), Toast.LENGTH_SHORT).show();
                    } else {
                        if (response.body() != null && !response.body().isEmpty()) {
                            linearLayout11.setVisibility(View.VISIBLE);
                            recycler_view_markattendance.setVisibility(View.VISIBLE);
                            markAttendanceDetails.add(new MarkAttendanceDetails(formattedDate, formattedTime));
                            markAttendanceAdapter = new MarkAttendanceAdapter(MarkAttendanceActivity.this, markAttendanceDetails);
                            recycler_view_markattendance.setAdapter(markAttendanceAdapter);
                        } else {
                            linearLayout11.setVisibility(View.GONE);
                            recycler_view_markattendance.setVisibility(View.GONE);
                        }
                        DatabaseHelper.getInstance(MarkAttendanceActivity.this).save_time(Constants.staffid, "0", formattedTime, formattedDate);
                        Toast.makeText(MarkAttendanceActivity.this, getResources().getString(R.string.checkoutsuccess), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ArrayList<CheckLeaveStatus>> call, Throwable t) {
                //Log.d("failed is ", t.getMessage());
            }
        });


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

                if (check_param_ok()) {
                    if (DatabaseHelper.getInstance(MarkAttendanceActivity.this).allow_check(0)) {
                        if (month_short[spinner_month.getSelectedItemPosition()].equalsIgnoreCase(get_monthName(Calendar.getInstance().get(Calendar.MONTH) + 1))) {
                            send_checkIn_checkOut(0);
                        } else {
                            Toast.makeText(MarkAttendanceActivity.this, getResources().getString(R.string.selectcurrentmonth), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MarkAttendanceActivity.this, getResources().getString(R.string.checkin_once), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.menu_checkout:
                if (check_param_ok()) {
                    if (DatabaseHelper.getInstance(MarkAttendanceActivity.this).allow_check(1)) {
                        if (month_short[spinner_month.getSelectedItemPosition()].equalsIgnoreCase(get_monthName(Calendar.getInstance().get(Calendar.MONTH) + 1))) {
                            if(DatabaseHelper.getInstance(MarkAttendanceActivity.this).allow_check(2)) {
                                send_checkIn_checkOut(1);
                            }else{
                                Toast.makeText(MarkAttendanceActivity.this, getResources().getString(R.string.cannotcheckout), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MarkAttendanceActivity.this, getResources().getString(R.string.selectcurrentmonth), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MarkAttendanceActivity.this, getResources().getString(R.string.checkout_once), Toast.LENGTH_LONG).show();
                    }
                }

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

        if (flag == 1) {
            spinner_month.setSelection(Calendar.getInstance().get(Calendar.MONTH));
            spinner_year.setSelection((years.size() - 1));
        } else {
            spinner_month.setSelection(month1 - 1);
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
        } else {
            year = year1;
        }

        if (month.equals("")) {
            month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH));
        }
        get_attendance_details(staffid, get_monthNumber(month), year);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DateUtils.calculate_validity(DatabaseHelper.getInstance(MarkAttendanceActivity.this).check_sqliteDate(), Constants.today_date)) {
            Log.d("validity ", DatabaseHelper.getInstance(MarkAttendanceActivity.this).check_sqliteDate() + "" + Constants.today_date);
            DatabaseHelper.getInstance(MarkAttendanceActivity.this).deleteEntry("table_attendance", Constants.staffid);
        } else {
            Log.d("validity ", DatabaseHelper.getInstance(MarkAttendanceActivity.this).check_sqliteDate() + " " + Constants.today_date);
        }
    }
}