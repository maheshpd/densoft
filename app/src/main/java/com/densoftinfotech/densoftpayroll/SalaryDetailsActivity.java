package com.densoftinfotech.densoftpayroll;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.densoftinfotech.densoftpayroll.utilities.CommonActivity;

import java.util.Calendar;

public class SalaryDetailsActivity extends CommonActivity {


    @BindView(R.id.tv_pickdate)
    TextView tv_pickdate;
    @BindView(R.id.tv_selecteddate)
    TextView tv_selecteddate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_details);

        toolbar_common();
        back();

        ButterKnife.bind(this);

        tv_pickdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SalaryDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayofmonth) {
                        tv_selecteddate.setText(dayofmonth + "/" + (month+1) + "/" + year);
                    }
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

    }
}
