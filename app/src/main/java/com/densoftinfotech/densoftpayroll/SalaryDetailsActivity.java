package com.densoftinfotech.densoftpayroll;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.densoftinfotech.densoftpayroll.classes.SalarySlip;
import com.densoftinfotech.densoftpayroll.demo_class.SalarySlipDemo;
import com.densoftinfotech.densoftpayroll.utilities.CommonActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SalaryDetailsActivity extends CommonActivity {


    @BindView(R.id.tv_pickdate)
    TextView tv_pickdate;
    @BindView(R.id.tv_selecteddate)
    TextView tv_selecteddate;
    ArrayList<SalarySlip> salaryDetailsActivities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_details);

        toolbar_common();
        back();

        ButterKnife.bind(this);

        Date d = new Date();
        CharSequence s  = DateFormat.format("dd/MM/yyyy", d.getTime());
        tv_selecteddate.setText(getResources().getString(R.string.payslipfor) + " " + s);

        tv_pickdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SalaryDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayofmonth) {
                        tv_selecteddate.setText(getResources().getString(R.string.payslipfor) + " " + dayofmonth + "/" + (month+1) + "/" + year);

                        for(int i = 0; i< SalarySlipDemo.month.length; i++){

                        }
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

    }
}
