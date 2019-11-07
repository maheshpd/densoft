package com.densoftinfotech.densoftpaysmart;

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

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.densoftinfotech.densoftpaysmart.adapter.SalarySlipAdapter;
import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.classes.SalarySlip;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.retrofit.GetServiceInterface;
import com.densoftinfotech.densoftpaysmart.retrofit.RetrofitClient;
import com.densoftinfotech.densoftpaysmart.room_database.Paysmart_roomdatabase;
import com.densoftinfotech.densoftpaysmart.room_database.Staff.StaffDetailsRoom;
import com.itextpdf.text.pdf.parser.Line;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SalaryDetailsActivity extends CommonActivity {


    @BindView(R.id.tv_pickdate)
    TextView tv_pickdate;
    @BindView(R.id.tv_selecteddate)
    TextView tv_selecteddate;
    @BindView(R.id.recycler_view_salaryslip)
    RecyclerView recycler_view_salaryslip;
    @BindView(R.id.spinner_month)
    Spinner spinner_month;
    @BindView(R.id.spinner_year)
    Spinner spinner_year;

    GetServiceInterface getServiceInterface;

    int year_of_joining = 0;
    String month, year = "";

    private ArrayList<Integer> years = new ArrayList<>();
    private static String[] month_short = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    RecyclerView.LayoutManager layoutManager;
    SalarySlipAdapter salarySlipAdapter;
    ArrayList<SalarySlip> salarySlips = new ArrayList<>();
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_details);

        toolbar_common();
        back();

        ButterKnife.bind(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(SalaryDetailsActivity.this);

        layoutManager = new LinearLayoutManager(SalaryDetailsActivity.this);
        recycler_view_salaryslip.setLayoutManager(layoutManager);

        GetSalary_Async getSalary_async = new GetSalary_Async();
        getSalary_async.execute();

    }

    private void get_salary_data(String staffid, String month_send, String year_send) {

        Retrofit retrofit = RetrofitClient.getRetrofit();
        getServiceInterface = retrofit.create(GetServiceInterface.class);

        Map<String, Object>  params = new HashMap<>();

        //params.put("customerid", preferences.getString("customerid", ""));
        params.put("ActionId", "0");
        params.put("StaffId", staffid);
        params.put("Month", month_send);
        params.put("Year", year_send);

        JSONObject obj = new JSONObject(params);
        Log.d("params ", obj + "");

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (obj).toString());
        Call<ArrayList<SalarySlip>> call = getServiceInterface.request_salary(requestBody);

        call.enqueue(new Callback<ArrayList<SalarySlip>>() {
            @Override
            public void onResponse(Call<ArrayList<SalarySlip>> call, Response<ArrayList<SalarySlip>> response) {
                if(!response.isSuccessful()){
                    Log.d("response code ", response.code() + " ");
                }else {
                    Log.d("response ", response.body() + "");
                    if(response.body()!=null){
                            salarySlips = response.body();
                            //salarySlipAdapter = new SalarySlipAdapter(SalaryDetailsActivity.this, salarySlips, response.body(), 1);
                            recycler_view_salaryslip.setAdapter(salarySlipAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SalarySlip>> call, Throwable t) {
                Log.d("failed ", t.getMessage() + "");
            }
        });


    }

    private class GetSalary_Async extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            StaffDetailsRoom staffDetails = Paysmart_roomdatabase.get_PaysmartDatabase(SalaryDetailsActivity.this).staffDetails_dao().getAll();

            if(staffDetails!=null){
                year_of_joining = Integer.parseInt(staffDetails.getJoiningDate().split("-")[2]);

                getset_spinner_data(year_of_joining, staffDetails.getStaffId());
            }

            return null;
        }
    }

    private void get_monthandyear(String  staffid, String month, String year) {

        if(year.equals("")){
            year = Constants.current_year;
        }
        if(month.equals("")){
            month = Constants.current_month;
        }

        tv_selecteddate.setText(getResources().getString(R.string.payslipfor) + " " + month + " " + year);

        switch (month){
            case "Jan":
                get_salary_data(staffid, "1", year);
                break;
            case "Feb":
                get_salary_data(staffid, "2", year);
                break;
            case "Mar":
                get_salary_data(staffid, "3", year);
                break;
            case "Apr":
                get_salary_data(staffid, "4", year);
                break;
            case "May":
                get_salary_data(staffid, "5", year);
                break;
            case "Jun":
                get_salary_data(staffid, "6", year);
                break;
            case "Jul":
                get_salary_data(staffid, "7", year);
                break;
            case "Aug":
                get_salary_data(staffid, "8", year);
                break;
            case "Sep":
                get_salary_data(staffid, "9", year);
                break;
            case "Oct":
                get_salary_data(staffid, "10", year);
                break;
            case "Nov":
                get_salary_data(staffid, "11", year);
                break;
            case "Dec":
                get_salary_data(staffid, "12", year);
                break;
        }

    }

    private void getset_spinner_data(int year_of_joining, String staffid) {

        for(int i = year_of_joining; i<=Calendar.getInstance().get(Calendar.YEAR); i++) {
            years.add(i);
        }

        ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<Integer>(SalaryDetailsActivity.this, R.layout.custom_spinnerlayout, R.id.text1, years);
        spinner_year.setAdapter(yearAdapter);

        spinner_year.setSelection((years.size()-1));

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(SalaryDetailsActivity.this, R.layout.custom_spinnerlayout, R.id.text1, month_short);
        spinner_month.setAdapter(monthAdapter);

        spinner_month.setSelection(Calendar.getInstance().get(Calendar.MONTH));

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
}
