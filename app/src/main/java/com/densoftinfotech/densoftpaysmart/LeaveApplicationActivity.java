package com.densoftinfotech.densoftpaysmart;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.densoftinfotech.densoftpaysmart.classes.LeaveDetails;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.demo_class.SalarySlipDemo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

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

    LeaveDetails leaveDetails;
    ArrayList<String> templates = new ArrayList<>();
    ArrayList<String> leavecodes = new ArrayList<>();

    Bundle b;

    int days = 0;
    int days_from = 0;
    int days_to = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fullscreen();
        setContentView(R.layout.activity_leave);

        ButterKnife.bind(this);

        b = getIntent().getExtras();
        if (b != null) {
            if(b.containsKey("leave"));
            leaveDetails = (LeaveDetails)  b.getSerializable("leave");
            Log.d("leave details ", leaveDetails + "");
            if(leaveDetails!=null) {
                tv_leavetype.setText(leaveDetails.getName_of_leave());
            }
        }

        et_dateofleave_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(LeaveApplicationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayofmonth) {
                        et_dateofleave_from.setText(dayofmonth + "/" + (month+1) + "/" + year);
                        days_from = -(dayofmonth + (month+1) + year);
                        if((!et_dateofleave_from.getText().toString().trim().equals("")) && (!et_dateofleave_to.getText().toString().trim().equals(""))) {
                            calculate_days(days_from, days_to);
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
                        et_dateofleave_to.setText(dayofmonth + "/" + (month+1) + "/" + year);
                        if((!et_dateofleave_from.getText().toString().trim().equals("")) && (!et_dateofleave_to.getText().toString().trim().equals(""))){
                            days_to = dayofmonth + (month+1) + year;
                            calculate_days(days_from, days_to);
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
                LeaveApplicationActivity.this.finish();
            }
        });

    }

    private void calculate_days(int days_from, int days_to) {
        days=0;
        if((days_from + days_to)>0){
            days = days_from + days_to;
            tv_days.setText(days + "");
        }else{
            tv_days.setText("");
            Toast.makeText(LeaveApplicationActivity.this, getResources().getString(R.string.selectgreaterdate), Toast.LENGTH_SHORT).show();
        }
    }
}
