package com.densoftinfotech.densoftpayroll;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.densoftinfotech.densoftpayroll.adapter.MonthAdapter;
import com.densoftinfotech.densoftpayroll.adapter.SalarySlipAdapter;
import com.densoftinfotech.densoftpayroll.classes.MonthDisplay;
import com.densoftinfotech.densoftpayroll.classes.SalarySlip;
import com.densoftinfotech.densoftpayroll.demo_class.SalarySlipDemo;

import java.util.ArrayList;

public class SalarySlipDetailsActivity extends AppCompatActivity {


    @BindView(R.id.tv_takehome)
    TextView tv_takehome;
    @BindView(R.id.tv_deductions)
    TextView tv_deductions;
    @BindView(R.id.tv_grosspay)
    TextView tv_grosspay;

    @BindView(R.id.recycler_view_salaryslipdetails)
    RecyclerView recycler_view_salaryslipdetails;
    @BindView(R.id.spinner_range)
    Spinner spinner;
    ArrayList<String> monthrange = new ArrayList<>();

    RecyclerView.LayoutManager layoutManager;

    MonthAdapter monthAdapter;
    ArrayList<MonthDisplay> monthDisplays = new ArrayList<>();
    ArrayList<SalarySlip> salarySlips = new ArrayList<>();
    SalarySlipAdapter salarySlipAdapter;

    Bundle b;

    int pos = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_slip_details);

        ButterKnife.bind(this);

        b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("pos")) {
                pos = b.getInt("pos");
            }
        }

        for (int i = 0; i < SalarySlipDemo.month_range.length; i++) {
            monthrange.add(SalarySlipDemo.month_range[i]);
        }

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_view_salaryslipdetails.setLayoutManager(layoutManager);

        for (int i = 0; i < SalarySlipDemo.month_short.length; i++) {
            monthDisplays.add(new MonthDisplay(SalarySlipDemo.month_short[i]));
        }


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, monthrange);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        monthAdapter = new MonthAdapter(SalarySlipDetailsActivity.this, monthDisplays, pos);
        recycler_view_salaryslipdetails.setAdapter(monthAdapter);

        for (int i = 0; i < SalarySlipDemo.month.length; i++) {
            salarySlips.add(new SalarySlip(SalarySlipDemo.month[i], SalarySlipDemo.days_of_month[i], SalarySlipDemo.take_home[i], SalarySlipDemo.deductions[i]));
        }


    }

    public void gotoselection(int i) {
            tv_takehome.setText("₹ " + salarySlips.get(i).getTake_home());
            tv_deductions.setText("₹ " + salarySlips.get(i).getDeduction());
            tv_grosspay.setText(getResources().getString(R.string.totalgrosspay) + ": ₹ " + (salarySlips.get(i).getTake_home() + salarySlips.get(pos).getDeduction()));
    }


}
