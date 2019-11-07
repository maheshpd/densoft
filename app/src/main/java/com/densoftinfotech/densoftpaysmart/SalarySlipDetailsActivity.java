package com.densoftinfotech.densoftpaysmart;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.densoftinfotech.densoftpaysmart.adapter.MonthAdapter;
import com.densoftinfotech.densoftpaysmart.adapter.SalarySlipAdapter;
import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.app_utilities.URLS;
import com.densoftinfotech.densoftpaysmart.classes.MonthDisplay;
import com.densoftinfotech.densoftpaysmart.classes.SalarySlip;
import com.densoftinfotech.densoftpaysmart.classes.StaffDetails;
import com.densoftinfotech.densoftpaysmart.demo_class.SalarySlipDemo;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.retrofit.GetServiceInterface;
import com.densoftinfotech.densoftpaysmart.retrofit.RetrofitClient;
import com.densoftinfotech.densoftpaysmart.room_database.Staff.StaffDetailsRoom;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class SalarySlipDetailsActivity extends CommonActivity {

    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.recycler_view_salaryslipdetails)
    RecyclerView recycler_view_salaryslipdetails;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.tv_nodataavailable)
    TextView tv_nodataavailable;

    ArrayList<Integer> years = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    MonthAdapter monthAdapter;
    Bundle b;

    StaffDetailsRoom staffDetails;
    private GetServiceInterface getServiceInterface;
    ArrayList<SalarySlip> salarySlips = new ArrayList<>();
    private SharedPreferences preferences;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_slip_details);

        ButterKnife.bind(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(SalarySlipDetailsActivity.this);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setBuiltInZoomControls(true);

        staffDetails = Constants.staffDetailsRoom;
        if (staffDetails != null) {
            getset_spinner_data(Integer.parseInt(staffDetails.getJoiningDate().split("-")[2]), staffDetails.getStaffId());
        }

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_view_salaryslipdetails.setLayoutManager(layoutManager);

        b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("month")) {
                gotoselection(String.valueOf(b.getInt("month")));
            }
        }

    }

    public void gotoselection(String monthname) {
        webview.loadUrl(URLS.dynamic_url_webroute(Constants.staffDetailsRoom.getDomainUrl()) + "AdminRpt/EmployeeSalarySlip.htm?CategoryId=233&" + "month=" + monthname +
                "&year=" + "2019" + "&StaffId=" + Constants.staffid + "&mText=" + get_monthName(Integer.parseInt(monthname)));
    }

    private void getset_spinner_data(int year_of_joining, String staffid) {

        for (int i = year_of_joining; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            years.add(i);
        }

        ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<Integer>(SalarySlipDetailsActivity.this, R.layout.custom_spinnerlayout, R.id.text1, years);
        spinner.setAdapter(yearAdapter);

        spinner.setSelection((years.size() - 1));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                get_salary_data(Constants.staffid, adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void get_salary_data(String staffid, String year) {

        Retrofit retrofit = RetrofitClient.getRetrofit();
        getServiceInterface = retrofit.create(GetServiceInterface.class);

        Map<String, Object> params = new HashMap<>();

        salarySlips.clear();
        params.put("customerid", preferences.getString("customerid", ""));
        params.put("ActionId", "0");
        params.put("StaffId", staffid);
        params.put("Month", "0");
        params.put("Year", year); //current year
        JSONObject obj = new JSONObject(params);
        Log.d("params ", obj + "");

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (obj).toString());
        Call<ArrayList<SalarySlip>> call = getServiceInterface.request_salary(requestBody);


        call.enqueue(new Callback<ArrayList<SalarySlip>>() {
            @Override
            public void onResponse(Call<ArrayList<SalarySlip>> call, Response<ArrayList<SalarySlip>> response) {
                if (!response.isSuccessful()) {
                    Log.d("response code ", response.code() + " ");
                } else {
                    if (response.body() != null) {

                        TreeSet<Integer> newset = new TreeSet<>();
                        salarySlips = response.body();

                        Log.d("salary slip size ", salarySlips.size() + "");

                        if (salarySlips.size() > 0) {
                            tv_nodataavailable.setVisibility(View.GONE);
                            recycler_view_salaryslipdetails.setVisibility(View.VISIBLE);
                            webview.setVisibility(View.VISIBLE);
                            for (SalarySlip record : salarySlips) {
                                newset.add(record.getApplyForMonth());
                            }

                            monthAdapter = new MonthAdapter(SalarySlipDetailsActivity.this, newset);
                            recycler_view_salaryslipdetails.setAdapter(monthAdapter);
                        } else {
                            tv_nodataavailable.setVisibility(View.VISIBLE);
                            recycler_view_salaryslipdetails.setVisibility(View.GONE);
                            webview.setVisibility(View.GONE);
                        }

                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SalarySlip>> call, Throwable t) {

            }
        });

    }

}
