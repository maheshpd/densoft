package com.densoftinfotech.densoftpayroll;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.densoftinfotech.densoftpayroll.adapter.CalendarDetailsAdapter;
import com.densoftinfotech.densoftpayroll.adapter.PlannerAdapter;
import com.densoftinfotech.densoftpayroll.classes.CalendarDetails;
import com.densoftinfotech.densoftpayroll.demo_class.CalendarDetailsDemo;
import com.densoftinfotech.densoftpayroll.utilities.CommonActivity;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PlannerActivity extends CommonActivity {


    @BindView(R.id.tv_myleave)
    TextView tv_myleave;
    @BindView(R.id.tv_myplanner)
    TextView tv_myplanner;
    @BindView(R.id.tv_myteam)
    TextView tv_myteam;
    @BindView(R.id.linear1)
    LinearLayout linear1;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    PlannerAdapter plannerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);
        toolbar_common();
        back();

        ButterKnife.bind(this);


        plannerAdapter = new PlannerAdapter(getSupportFragmentManager(), 1);
        viewpager.setAdapter(plannerAdapter);

        tv_myleave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_myleave.setBackgroundResource(R.drawable.textview_rounded_selected_left);
                tv_myleave.setTextColor(getResources().getColor(R.color.white));
                tv_myplanner.setBackgroundResource(R.drawable.textview_unselected);
                tv_myplanner.setTextColor(getResources().getColor(R.color.black));
                tv_myteam.setBackgroundResource(R.drawable.textview_rounded_unselected_right);
                tv_myteam.setTextColor(getResources().getColor(R.color.black));
            }
        });

        tv_myplanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tv_myleave.setBackgroundResource(R.drawable.textview_rounded_unselected_left);
                tv_myleave.setTextColor(getResources().getColor(R.color.black));
                tv_myplanner.setBackgroundColor(getResources().getColor(R.color.blue));
                tv_myplanner.setTextColor(getResources().getColor(R.color.white));
                tv_myteam.setBackgroundResource(R.drawable.textview_rounded_unselected_right);
                tv_myteam.setTextColor(getResources().getColor(R.color.black));

            }
        });

        tv_myteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_myleave.setBackgroundResource(R.drawable.textview_rounded_unselected_left);
                tv_myleave.setTextColor(getResources().getColor(R.color.black));
                tv_myplanner.setBackgroundResource(R.drawable.textview_unselected);
                tv_myplanner.setTextColor(getResources().getColor(R.color.black));
                tv_myteam.setBackgroundResource(R.drawable.textview_rounded_selected_right);
                tv_myteam.setTextColor(getResources().getColor(R.color.white));
            }
        });


    }
}