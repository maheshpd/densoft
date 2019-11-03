package com.densoftinfotech.densoftpaysmart;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.densoftinfotech.densoftpaysmart.adapter.MonthAdapter;
import com.densoftinfotech.densoftpaysmart.adapter.SalarySlipAdapter;
import com.densoftinfotech.densoftpaysmart.classes.MonthDisplay;
import com.densoftinfotech.densoftpaysmart.classes.SalarySlipDistinct;
import com.densoftinfotech.densoftpaysmart.demo_class.SalarySlipDemo;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;

public class SalarySlipDetailsActivity extends CommonActivity {


    @BindView(R.id.tv_takehome)
    TextView tv_takehome;
    @BindView(R.id.tv_deductions)
    TextView tv_deductions;
    @BindView(R.id.tv_grosspay)
    TextView tv_grosspay;

    @BindView(R.id.piechart)
    PieChart piechart;

    @BindView(R.id.recycler_view_salaryslipdetails)
    RecyclerView recycler_view_salaryslipdetails;
    @BindView(R.id.spinner_range)
    Spinner spinner;
    ArrayList<String> monthrange = new ArrayList<>();

    RecyclerView.LayoutManager layoutManager;

    MonthAdapter monthAdapter;
    ArrayList<MonthDisplay> monthDisplays = new ArrayList<>();
    ArrayList<SalarySlipDistinct> salarySlipDistincts = new ArrayList<>();
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
                //gotoselection(pos);
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


    }

    /*public void gotoselection(int i) {

        for (int i1 = 0; i1 < SalarySlipDemo.month.length; i1++) {
            salarySlipDistincts.add(new SalarySlipDistinct(SalarySlipDemo.month[i1], SalarySlipDemo.days_of_month[i1], SalarySlipDemo.take_home[i1], SalarySlipDemo.deductions[i1]));
        }
        tv_takehome.setText("₹ " + salarySlipDistincts.get(i).getTake_home());
        tv_deductions.setText("₹ " + salarySlipDistincts.get(i).getDeduction());
        tv_grosspay.setText(getResources().getString(R.string.totalgrosspay) + ": ₹ " + (salarySlipDistincts.get(i).getTake_home() + salarySlipDistincts.get(pos).getDeduction()));


        setdata(i);
    }*/

    /*public void setdata(int i) {
        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry((float) salarySlipDistincts.get(i).getTake_home(), "Take Home"));
        entries.add(new PieEntry((float) salarySlipDistincts.get(i).getDeduction(), "Deductions"));

        PieDataSet set = new PieDataSet(entries, "");
        PieData data = new PieData(set);
        piechart.setData(data);
        data.setValueTextColor(ContextCompat.getColor(SalarySlipDetailsActivity.this, R.color.black));
        data.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        //set.setColors(ColorTemplate.COLORFUL_COLORS);
        set.setColors(new int[]{R.color.bluepie, R.color.purplepie}, SalarySlipDetailsActivity.this);
        piechart.notifyDataSetChanged();
        piechart.invalidate();
        //piechart.animateXY(5000, 5000);
    }*/


}
