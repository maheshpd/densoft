package com.densoftinfotech.densoftpayroll;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.densoftinfotech.densoftpayroll.adapter.QuickActionsAdapter;
import com.densoftinfotech.densoftpayroll.adapter.SalarySlipAdapter;
import com.densoftinfotech.densoftpayroll.classes.QuickActions;
import com.densoftinfotech.densoftpayroll.classes.SalarySlip;
import com.densoftinfotech.densoftpayroll.demo_class.QuickActionsDemo;
import com.densoftinfotech.densoftpayroll.demo_class.SalarySlipDemo;
import com.densoftinfotech.densoftpayroll.utilities.CommonActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends CommonActivity {

    @BindView(R.id.recycler_view_salaryslip)
    RecyclerView recycler_view_salaryslip;
    @BindView(R.id.recycler_view_quickactions)
    RecyclerView recycler_view_quickactions;

    RecyclerView.LayoutManager layoutManager_salaryslip, layoutManager_quickaction;

    SalarySlipAdapter salarySlipAdapter;
    QuickActionsAdapter quickActionsAdapter;

    ArrayList<SalarySlip> salarySlips = new ArrayList<>();
    ArrayList<QuickActions> quickActions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fullscreen();
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        layoutManager_salaryslip = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_view_salaryslip.setLayoutManager(layoutManager_salaryslip);

        layoutManager_quickaction = new GridLayoutManager(this, 4);
        recycler_view_quickactions.setLayoutManager(layoutManager_quickaction);


        for (int i = 0; i < SalarySlipDemo.month.length; i++) {
            salarySlips.add(new SalarySlip(SalarySlipDemo.month[i], SalarySlipDemo.days_of_month[i], SalarySlipDemo.take_home[i], SalarySlipDemo.deductions[i]));
        }

        salarySlipAdapter = new SalarySlipAdapter(MainActivity.this, salarySlips);
        recycler_view_salaryslip.setAdapter(salarySlipAdapter);

        for (int i = 0; i < QuickActionsDemo.names.length; i++) {
            quickActions.add(new QuickActions(QuickActionsDemo.names[i], QuickActionsDemo.image[i]));
        }

        quickActionsAdapter = new QuickActionsAdapter(MainActivity.this, quickActions);
        recycler_view_quickactions.setAdapter(quickActionsAdapter);

    }

    public void gotoactivity(String name) {

        if(name.equalsIgnoreCase("Planner")){
            Intent i = new Intent(MainActivity.this, PlannerActivity.class);
            startActivity(i);
        }else if(name.equalsIgnoreCase("Salary Details")){
            Intent i = new Intent(MainActivity.this, SalaryDetailsActivity.class);
            startActivity(i);
        }
    }
}
