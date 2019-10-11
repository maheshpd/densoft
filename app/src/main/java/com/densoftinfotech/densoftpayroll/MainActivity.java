package com.densoftinfotech.densoftpayroll;

import android.os.Bundle;

import com.densoftinfotech.densoftpayroll.adapter.QuickActionsAdapter;
import com.densoftinfotech.densoftpayroll.adapter.SalarySlipAdapter;
import com.densoftinfotech.densoftpayroll.classes.QuickActions;
import com.densoftinfotech.densoftpayroll.classes.SalarySlip;
import com.densoftinfotech.densoftpayroll.demo_class.QuickActionsDemo;
import com.densoftinfotech.densoftpayroll.demo_class.SalarySlipDemo;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        layoutManager_salaryslip = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_view_salaryslip.setLayoutManager(layoutManager_salaryslip);

        layoutManager_quickaction = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
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
}
