package com.densoftinfotech.densoftpaysmart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.densoftinfotech.densoftpaysmart.adapter.LeaveAdapter;
import com.densoftinfotech.densoftpaysmart.adapter.LeaveAppliedDetailsAdapter;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.model.CalendarDetails;
import com.densoftinfotech.densoftpaysmart.model.LeaveAppliedDetails;
import com.densoftinfotech.densoftpaysmart.model.LeaveDetails;
import com.densoftinfotech.densoftpaysmart.model.StaffDetails;
import com.densoftinfotech.densoftpaysmart.retrofit.GetServiceInterface;
import com.densoftinfotech.densoftpaysmart.retrofit.RetrofitClient;

import org.json.JSONObject;

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

import static com.densoftinfotech.densoftpaysmart.app_utilities.Constants.staffid;

public class LeaveListActivity extends CommonActivity {

    @BindView(R.id.recyclerview_leave)
    RecyclerView recyclerView;
    @BindView(R.id.recyclerview_leave_request_status)
    RecyclerView recyclerview_leave_request_status;
    @BindView(R.id.spinner_year)
    Spinner spinner_year;
    @BindView(R.id.spinner_month)
    Spinner spinner_month;
    @BindView(R.id.tv_leavestatus)
    TextView tv_leavestatus;


    private RecyclerView.LayoutManager layoutManager, layoutManager1;
    private LeaveAdapter leaveAdapter;
    private LeaveAppliedDetailsAdapter leaveAppliedDetailsAdapter;

    private ArrayList<LeaveDetails> leaveDetails = new ArrayList<>();
    private ArrayList<LeaveAppliedDetails> leaveAppliedDetails = new ArrayList<>();
    private GetServiceInterface getServiceInterface;

    private SharedPreferences preferences;

    Map<String, Object> params = new HashMap<>();
    private ArrayList<Integer> years = new ArrayList<>();
    private static String[] month_short = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private StaffDetails staffDetails = new StaffDetails();
    String month = "0", year = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_list);

        setTitle(getResources().getString(R.string.leave));
        back();

        ButterKnife.bind(this);

        staffDetails = getStaffDetails(LeaveListActivity.this);

        preferences = PreferenceManager.getDefaultSharedPreferences(LeaveListActivity.this);

        if (staffDetails != null) {
            //get_attendance_details(staffDetails.getStaffId(), voids[0]);

            getset_spinner_data(Integer.parseInt(staffDetails.getJoiningDate().split("-")[2]), Calendar.getInstance().get(Calendar.MONTH), 1);
        }

        layoutManager = new LinearLayoutManager(LeaveListActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        layoutManager1 = new LinearLayoutManager(LeaveListActivity.this);
        recyclerview_leave_request_status.setLayoutManager(layoutManager1);

        Retrofit retrofit = RetrofitClient.getRetrofit();
        getServiceInterface = retrofit.create(GetServiceInterface.class);

        params.put("customerid", preferences.getInt("customerid", 0));
        params.put("ActionId", "0");
        params.put("StaffId", staffid);

        JSONObject obj = new JSONObject(params);

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (obj).toString());
        Call<ArrayList<LeaveDetails>> call = getServiceInterface.request_leavedetails(requestBody);

        call.enqueue(new Callback<ArrayList<LeaveDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<LeaveDetails>> call, Response<ArrayList<LeaveDetails>> response) {
                if (!response.isSuccessful()) {
                    //Log.d("response code ", response.code() + " ");
                } else {
                    if (!response.body().isEmpty()) {
                        leaveDetails = response.body();

                        leaveAdapter = new LeaveAdapter(LeaveListActivity.this, leaveDetails);
                        recyclerView.setAdapter(leaveAdapter);

                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<LeaveDetails>> call, Throwable t) {

            }
        });

        get_leave_applied_status(String.valueOf(Calendar.getInstance().get(Calendar.MONTH)), staffDetails.getJoiningDate().split("-")[2]);

    }

    private void get_leave_applied_status(String month, String year) {
        Retrofit retrofit = RetrofitClient.getRetrofit();
        getServiceInterface = retrofit.create(GetServiceInterface.class);

        params = new HashMap<>();
        params.put("customerid", preferences.getInt("customerid", 0));
        params.put("ActionId", "0");
        params.put("StaffId", preferences.getInt("staffid", 0));
        params.put("Month", month);
        params.put("Year", year);

        JSONObject obj = new JSONObject(params);

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (obj).toString());
        Call<ArrayList<LeaveAppliedDetails>> call = getServiceInterface.request_leaveapplieddetails(requestBody);

        call.enqueue(new Callback<ArrayList<LeaveAppliedDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<LeaveAppliedDetails>> call, Response<ArrayList<LeaveAppliedDetails>> response) {
                if (!response.isSuccessful()) {
                    Log.d("response code ", response.code() + " ");
                } else {
                    if (response.body() != null && !response.body().isEmpty()) {

                        leaveAppliedDetails = response.body();

                        recyclerview_leave_request_status.setVisibility(View.VISIBLE);
                        leaveAppliedDetailsAdapter = new LeaveAppliedDetailsAdapter(LeaveListActivity.this, leaveAppliedDetails);
                        recyclerview_leave_request_status.setAdapter(leaveAppliedDetailsAdapter);

                    } else {
                        recyclerview_leave_request_status.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<LeaveAppliedDetails>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.leave_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.holiday:
                Intent i = new Intent(LeaveListActivity.this, HolidayPlannerActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getset_spinner_data(int year_of_joining, int month1, int flag) {

        for (int i = Integer.parseInt(staffDetails.getJoiningDate().split("-")[2]); i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            years.add(i);
        }

        ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<Integer>(LeaveListActivity.this, R.layout.custom_spinnerlayout, R.id.text1, years);
        spinner_year.setAdapter(yearAdapter);

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(LeaveListActivity.this, R.layout.custom_spinnerlayout, R.id.text1, month_short);
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
                get_monthandyear(month, year);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                year = spinner_year.getItemAtPosition(i).toString();
                get_monthandyear(month, year);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void get_monthandyear(String month, String year1) {

        if (year.equals("")) {
            year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        } else {
            year = year1;
        }

        if (month.equals("")) {
            month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH));
        }
        get_leave_applied_status(get_monthNumber(month), year);

    }
}
