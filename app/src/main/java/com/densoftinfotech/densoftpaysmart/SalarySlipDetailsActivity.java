package com.densoftinfotech.densoftpaysmart;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.densoftinfotech.densoftpaysmart.adapter.MonthAdapter;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.app_utilities.URLS;
import com.densoftinfotech.densoftpaysmart.model.SalarySlip;
import com.densoftinfotech.densoftpaysmart.model.StaffDetails;
import com.densoftinfotech.densoftpaysmart.retrofit.GetServiceInterface;
import com.densoftinfotech.densoftpaysmart.retrofit.RetrofitClient;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

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

public class SalarySlipDetailsActivity extends CommonActivity {

    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.recycler_view_salaryslipdetails)
    RecyclerView recycler_view_salaryslipdetails;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.tv_nodataavailable)
    TextView tv_nodataavailable;

    private ArrayList<Integer> years = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    private MonthAdapter monthAdapter;
    private Bundle b;

    private StaffDetails staffDetails;
    private GetServiceInterface getServiceInterface;
    private ArrayList<SalarySlip> salarySlips = new ArrayList<>();
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

        staffDetails = getStaffDetails(SalarySlipDetailsActivity.this);


        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_view_salaryslipdetails.setLayoutManager(layoutManager);


        if (staffDetails != null) {

            b = getIntent().getExtras();
            if (b != null) {
                if (b.containsKey("monthpos")) {
                    //gotoselection_showslip(String.valueOf(b.getInt("monthpos")), String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
                    getset_spinner_data(Integer.parseInt(staffDetails.getJoiningDate().split("-")[2]), b.getInt("monthpos"));
                    // if option is selected from the card view of Main Activity
                }
            } else {
                if (staffDetails != null) {
                    getset_spinner_data(Integer.parseInt(staffDetails.getJoiningDate().split("-")[2]), -1);
                    // if option is selected from Quick Actions
                }
            }
        }

    }

    private void gotoselection_showslip(String month_number, String year) {

        webview.loadUrl(URLS.dynamic_url_webroute(staffDetails.getDomainUrl()) + "AdminRpt/EmployeeSalarySlip.htm?CategoryId=233&" + "month=" + month_number +
                "&year=" + year + "&StaffId=" + Constants.staffid + "&mText=" + get_monthName(Integer.parseInt(month_number)));
    }

    private void getset_spinner_data(int year_of_joining, int monthpos) {

        for (int i = year_of_joining; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            years.add(i);
        }

        ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<Integer>(SalarySlipDetailsActivity.this, R.layout.custom_spinnerlayout, R.id.text1, years);
        spinner.setAdapter(yearAdapter);

        spinner.setSelection((years.size() - 1));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                get_salary_data(Constants.staffid, adapterView.getItemAtPosition(i).toString(), monthpos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void get_salary_data(int staffid, String year, int monthpos) {

        Retrofit retrofit = RetrofitClient.getRetrofit();
        getServiceInterface = retrofit.create(GetServiceInterface.class);

        Map<String, Object> params = new HashMap<>();

        salarySlips.clear();
        params.put("customerid", preferences.getInt("customerid", 0));
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

                            monthAdapter = new MonthAdapter(SalarySlipDetailsActivity.this, newset, monthpos);
                            recycler_view_salaryslipdetails.setAdapter(monthAdapter);
                        } else {
                            tv_nodataavailable.setVisibility(View.VISIBLE);
                            recycler_view_salaryslipdetails.setVisibility(View.INVISIBLE);
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

    public void gotoselection(String valueOf) {
        gotoselection_showslip(valueOf, String.valueOf(salarySlips.get(spinner.getSelectedItemPosition()).getApplyForYear()));
    }
}
