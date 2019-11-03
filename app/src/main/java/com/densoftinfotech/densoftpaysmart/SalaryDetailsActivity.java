package com.densoftinfotech.densoftpaysmart;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Spinner;
import android.widget.TextView;

import com.densoftinfotech.densoftpaysmart.classes.SalarySlipDistinct;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.retrofit.GetServiceInterface;
import com.densoftinfotech.densoftpaysmart.retrofit.RetrofitClient;
import com.densoftinfotech.densoftpaysmart.room_database.Paysmart_roomdatabase;
import com.densoftinfotech.densoftpaysmart.room_database.Staff.StaffDetailsRoom;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SalaryDetailsActivity extends CommonActivity {


    @BindView(R.id.tv_pickdate)
    TextView tv_pickdate;
    @BindView(R.id.tv_selecteddate)
    TextView tv_selecteddate;
    @BindView(R.id.tv_grosspay)
    TextView tv_grosspay;
    @BindView(R.id.tv_deductions)
    TextView tv_deductions;
    @BindView(R.id.tv_netpay)
    TextView tv_netpay;
    @BindView(R.id.spinner_month)
    Spinner spinner_month;
    @BindView(R.id.spinner_year)
    Spinner spinner_year;

    GetServiceInterface getServiceInterface;

    int month_send = 0; int year_send = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_details);

        toolbar_common();
        back();

        ButterKnife.bind(this);



        Date d = new Date();
        CharSequence s  = DateFormat.format("MM/yyyy", d.getTime());
        tv_selecteddate.setText(getResources().getString(R.string.payslipfor) + " " + s);

        GetSalary_Async getSalary_async = new GetSalary_Async();
        getSalary_async.execute();

        /*tv_pickdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SalaryDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayofmonth) {
                        tv_selecteddate.setText(getResources().getString(R.string.payslipfor) + " " + dayofmonth + "/" + (month+1) + "/" + year);

                        month_send = month + 1;
                        year_send = year;

                        for(int i = 0; i< SalarySlipDemo.month.length; i++){

                        }

                        GetSalary_Async getSalary_async = new GetSalary_Async();
                        getSalary_async.execute();
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });*/

    }

    private void get_salary_data(String staffid) {

        Retrofit retrofit = RetrofitClient.getRetrofit();

        getServiceInterface = retrofit.create(GetServiceInterface.class);

        Map<String, Object>  params = new HashMap<>();
        params.put("ActionId", "0");
        params.put("StaffId", staffid);
        params.put("Month",String.valueOf(month_send));
        params.put("Year", String.valueOf(year_send));

        JSONObject obj = new JSONObject(params);
        Log.d("params ", obj + "");

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (obj).toString());
        Call<ArrayList<SalarySlipDistinct>> call = getServiceInterface.request_salary(requestBody);

        call.enqueue(new Callback<ArrayList<SalarySlipDistinct>>() {
            @Override
            public void onResponse(Call<ArrayList<SalarySlipDistinct>> call, Response<ArrayList<SalarySlipDistinct>> response) {
                if(!response.isSuccessful()){
                    Log.d("response code ", response.code() + " ");
                }else {
                    Log.d("response ", response.body() + "");
                    if(response.body()!=null){
                        set_responsedata(response.body());
                    }else{
                        tv_grosspay.setText(getResources().getString(R.string.rs) + " 0");
                        tv_deductions.setText(getResources().getString(R.string.rs) + " 0");
                        tv_netpay.setText(getResources().getString(R.string.rs) + " 0");
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SalarySlipDistinct>> call, Throwable t) {
                Log.d("failed ", t.getMessage() + "");
            }
        });


    }

    private void set_responsedata(ArrayList<SalarySlipDistinct> salarySlipDistincts) {
        for(int i = 0; i< salarySlipDistincts.size(); i++){
            if(salarySlipDistincts.get(i).getName().equalsIgnoreCase("Gross Salary")){
                tv_grosspay.setText(getResources().getString(R.string.rs) + " " + salarySlipDistincts.get(i).getAmount());
            }
            if(salarySlipDistincts.get(i).getName().equalsIgnoreCase("Total Deduction")){
                tv_deductions.setText(getResources().getString(R.string.rs) + " " + salarySlipDistincts.get(i).getAmount());
            }
            if(salarySlipDistincts.get(i).getName().equalsIgnoreCase("Net Salary")){
                tv_netpay.setText(getResources().getString(R.string.rs) + " " + salarySlipDistincts.get(i).getAmount());
            }
            if(salarySlipDistincts.get(i).getName().equalsIgnoreCase("Payable Days")){
                //tv_grosspay.setText(getResources().getString(R.string.rs) + " " +salarySlipDistincts.get(i).getAmount());
            }
        }
    }

    private class GetSalary_Async extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            StaffDetailsRoom staffDetails = Paysmart_roomdatabase.get_PaysmartDatabase(SalaryDetailsActivity.this).staffDetails_dao().getAll();

            if(staffDetails!=null){
                get_salary_data(staffDetails.getStaffId());
            }

            return null;
        }
    }
}
