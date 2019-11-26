package com.densoftinfotech.densoftpaysmart;

import androidx.preference.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.classes.CareOfStaff_Employee;
import com.densoftinfotech.densoftpaysmart.classes.CheckLeaveStatus;
import com.densoftinfotech.densoftpaysmart.classes.LeaveDetails;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.classes.ParentEmployee;
import com.densoftinfotech.densoftpaysmart.retrofit.GetServiceInterface;
import com.densoftinfotech.densoftpaysmart.retrofit.RetrofitClient;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.densoftinfotech.densoftpaysmart.app_utilities.Constants.staffid;

public class LeaveApplicationActivity extends CommonActivity {

    @BindView(R.id.tv_leavetype)
    TextView tv_leavetype;
    @BindView(R.id.tv_cancel)
    TextView tv_cancel;
    @BindView(R.id.tv_submit)
    TextView tv_submit;
    @BindView(R.id.et_dateofleave_from)
    EditText et_dateofleave_from;
    @BindView(R.id.et_dateofleave_to)
    EditText et_dateofleave_to;
    @BindView(R.id.tv_days)
    TextView tv_days;
    @BindView(R.id.tv_employee_name)
    TextView tv_employee_name;
    @BindView(R.id.et_description)
    EditText et_description;
    @BindView(R.id.tv_balance_leave)
    TextView tv_balance_leave;

    @BindView(R.id.spinner_reportto)
    Spinner spinner_reportto;
    @BindView(R.id.spinner_duties_coveredby)
    Spinner spinner_duties_coveredby;

    private LeaveDetails leaveDetails;
    private String leave_id = "";


    private ArrayList<ParentEmployee> spinner_parentEmployees = new ArrayList<>();
    private ArrayAdapter<ParentEmployee> parentEmployee_ArrayAdapter;

    private ArrayList<CareOfStaff_Employee> spinner_careOfStaff_employees = new ArrayList<>();
    private ArrayAdapter<CareOfStaff_Employee> careOfStaffEmployee_ArrayAdapter;

    private Bundle b;
    private GetServiceInterface getServiceInterface;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fullscreen();
        setContentView(R.layout.activity_leave);

        ButterKnife.bind(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(LeaveApplicationActivity.this);

        Retrofit retrofit = RetrofitClient.getRetrofit();
        getServiceInterface = retrofit.create(GetServiceInterface.class);

        b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("leaves")) ;
            leaveDetails = (LeaveDetails) b.getSerializable("leaves");
            //Log.d("leaves details ", leaveDetails + "");
            if (leaveDetails != null) {
                tv_leavetype.setText(leaveDetails.getName());
                tv_balance_leave.setText(leaveDetails.getBalanceLeave());
                leave_id = leaveDetails.getLeaveId();
            }
        }

        get_parent_employees();

        tv_employee_name.setText(Constants.staffDetailsRoom.getPName());
        et_dateofleave_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(LeaveApplicationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayofmonth) {
                        et_dateofleave_from.setText(year + "-" + (month + 1) + "-" + dayofmonth);

                        if ((!et_dateofleave_from.getText().toString().trim().equals("")) && (!et_dateofleave_to.getText().toString().trim().equals(""))) {
                            calculate_days(et_dateofleave_from.getText().toString(), et_dateofleave_to.getText().toString());
                        }

                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        et_dateofleave_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(LeaveApplicationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayofmonth) {
                        et_dateofleave_to.setText((year + "-" + (month + 1) + "-" + dayofmonth));
                        if ((!et_dateofleave_from.getText().toString().trim().equals("")) && (!et_dateofleave_to.getText().toString().trim().equals(""))) {

                            calculate_days(et_dateofleave_from.getText().toString(), et_dateofleave_to.getText().toString());
                        }

                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LeaveApplicationActivity.this.finish();
            }
        });

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit_leave_application();
            }
        });

    }

    private void submit_leave_application() {

        Map<String, Object> params = new HashMap<>();

        params.put("customerid", preferences.getString("customerid", ""));
        params.put("ActionId", "0");
        params.put("StaffId", staffid);
        params.put("Name", tv_employee_name.getText().toString());
        params.put("ApplicationDate", Constants.today_date);
        params.put("LeaveType", leave_id);
        params.put("NoOfDays", tv_days.getText().toString());
        params.put("FromDate", et_dateofleave_from.getText().toString());
        params.put("ToDate", et_dateofleave_to.getText().toString());
        params.put("Description", et_description.getText().toString());
        params.put("ContactNo", Constants.staffDetailsRoom.getMobile1());
        params.put("CareOfStaff", spinner_careOfStaff_employees.get(spinner_duties_coveredby.getSelectedItemPosition()).getPatientid());
        params.put("Priority", "0");
        params.put("Status", "0");
        params.put("P_Id", "0");
        params.put("Reason", "");
        params.put("RemainingLeave", "0");
        params.put("SendTo", spinner_parentEmployees.get(spinner_reportto.getSelectedItemPosition()).getPatientId());
        params.put("AcceptedBy", "0");
        params.put("aNoOfDays", "0");
        params.put("aFromDate", "");
        params.put("aToDate", "");
        params.put("typeId", "0");
        params.put("FromTime", "");
        params.put("toTime", "");
        params.put("ConfirmRelieve", "");


        JSONObject obj = new JSONObject(params);
        //Log.d("params ", obj + "");

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (obj).toString());
        Call<ArrayList<CheckLeaveStatus>> call = getServiceInterface.request_leave_applied_success(requestBody);

        call.enqueue(new Callback<ArrayList<CheckLeaveStatus>>() {
            @Override
            public void onResponse(Call<ArrayList<CheckLeaveStatus>> call, Response<ArrayList<CheckLeaveStatus>> response) {
                if (!response.isSuccessful()) {
                    //Log.d("response code ", response.code() + " ");
                } else {

                        if (response.body() != null) {

                            switch (response.body().get(0).getStatus()) {
                                case "1":
                                    Toast.makeText(LeaveApplicationActivity.this, getResources().getString(R.string.receivedleaveapplication), Toast.LENGTH_SHORT).show();
                                    LeaveApplicationActivity.this.finish();
                                    break;

                                case "In Probation":
                                    showAlert(getResources().getString(R.string.cannotapplyforleaveinprobation));
                                    break;

                                case "ExcessLeave":
                                    showAlert(getResources().getString(R.string.leavesnotremaining));
                                    break;

                                default:
                                    showAlert(getResources().getString(R.string.pleasecontactadmin));
                                    break;
                            }
                        }

                }
            }

            @Override
            public void onFailure(Call<ArrayList<CheckLeaveStatus>> call, Throwable t) {

            }
        });
    }

    private void showAlert(String string) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LeaveApplicationActivity.this);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(string).setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        alertDialog.show();

    }

    private void get_parent_employees() {

        Map<String, Object> params = new HashMap<>();

        params.put("customerid", preferences.getString("customerid", ""));
        params.put("ChildEmpId", staffid);

        JSONObject obj = new JSONObject(params);
        //Log.d("params ", obj + "");

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (obj).toString());
        Call<ArrayList<ParentEmployee>> call = getServiceInterface.request_parent_report_to(requestBody);

        call.enqueue(new Callback<ArrayList<ParentEmployee>>() {
            @Override
            public void onResponse(Call<ArrayList<ParentEmployee>> call, Response<ArrayList<ParentEmployee>> response) {
                if (!response.isSuccessful()) {
                    //Log.d("response code ", response.code() + " ");
                } else {
                    if (!response.body().isEmpty()) {

                        spinner_parentEmployees = response.body();
                        parentEmployee_ArrayAdapter = new ArrayAdapter(LeaveApplicationActivity.this, R.layout.custom_spinnerlayout, R.id.text1, spinner_parentEmployees);
                        spinner_reportto.setAdapter(parentEmployee_ArrayAdapter);

                        get_duties_covered_by_list();

                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ParentEmployee>> call, Throwable t) {

            }
        });
    }

    private void get_duties_covered_by_list() {

        Map<String, Object> params = new HashMap<>();
        params.put("customerid", preferences.getString("customerid", ""));
        params.put("ChildEmp", staffid);

        JSONObject obj = new JSONObject(params);
        //Log.d("params ", obj + "");

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (obj).toString());
        Call<ArrayList<CareOfStaff_Employee>> call = getServiceInterface.request_replacement_staff_during_absence(requestBody);

        call.enqueue(new Callback<ArrayList<CareOfStaff_Employee>>() {
            @Override
            public void onResponse(Call<ArrayList<CareOfStaff_Employee>> call, Response<ArrayList<CareOfStaff_Employee>> response) {
                if (!response.isSuccessful()) {
                    //Log.d("response code ", response.code() + " ");
                } else {
                    if (!response.body().isEmpty()) {

                        spinner_careOfStaff_employees = response.body();

                        careOfStaffEmployee_ArrayAdapter = new ArrayAdapter(LeaveApplicationActivity.this, R.layout.custom_spinnerlayout, R.id.text1, spinner_careOfStaff_employees);
                        spinner_duties_coveredby.setAdapter(careOfStaffEmployee_ArrayAdapter);


                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CareOfStaff_Employee>> call, Throwable t) {

            }
        });
    }

    private void calculate_days(String days_from, String days_to) {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date dateBefore = myFormat.parse(days_from);
            Date dateAfter = myFormat.parse(days_to);
            long difference = dateAfter.getTime() - dateBefore.getTime();
            float daysBetween = (difference / (1000 * 60 * 60 * 24));

            if (daysBetween < 0) {
                tv_days.setText("0");
                Toast.makeText(this, getResources().getString(R.string.selectgreaterdate), Toast.LENGTH_SHORT).show();
            } else {
                tv_days.setText(String.valueOf(((int) daysBetween)+1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
