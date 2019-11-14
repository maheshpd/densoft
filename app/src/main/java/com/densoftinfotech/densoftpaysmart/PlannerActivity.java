package com.densoftinfotech.densoftpaysmart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.densoftinfotech.densoftpaysmart.adapter.PlannerAdapter;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;

import java.util.Calendar;
import java.util.Locale;

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
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.iv_profile)
    ImageView iv_profile;

    PlannerAdapter plannerAdapter;
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        ButterKnife.bind(this);


        if(Constants.staffDetailsRoom!=null){
            setTitle(Constants.staffDetailsRoom.getPName());
            back();
        }else{
            toolbar_common();
            back();
        }

        plannerAdapter = new PlannerAdapter(getSupportFragmentManager(), 1);
        viewpager.setAdapter(plannerAdapter);

        tv_myleave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(0);
            }
        });

        tv_myplanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(1);
            }
        });

        tv_myteam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(2);
            }
        });



        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (viewpager.getCurrentItem()){
                    case 0:
                        select_tv_myleave();
                        break;

                    case 1:
                        select_tv_myplanner();
                        break;

                    case  2:
                        select_tv_myteam();
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void select_tv_myleave() {
        tv_myleave.setBackgroundResource(R.drawable.textview_rounded_selected_left);
        tv_myleave.setTextColor(getResources().getColor(R.color.white));
        tv_myplanner.setBackgroundResource(R.drawable.textview_unselected);
        tv_myplanner.setTextColor(getResources().getColor(R.color.black));
        tv_myteam.setBackgroundResource(R.drawable.textview_rounded_unselected_right);
        tv_myteam.setTextColor(getResources().getColor(R.color.black));
    }

    private void select_tv_myplanner() {
        tv_myleave.setBackgroundResource(R.drawable.textview_rounded_unselected_left);
        tv_myleave.setTextColor(getResources().getColor(R.color.black));
        tv_myplanner.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        tv_myplanner.setTextColor(getResources().getColor(R.color.white));
        tv_myteam.setBackgroundResource(R.drawable.textview_rounded_unselected_right);
        tv_myteam.setTextColor(getResources().getColor(R.color.black));

        Calendar mCal = (Calendar) cal.clone();
        mCal.add(Calendar.MONTH, 1);
        Log.d("month ", mCal.get(Calendar.MONTH) + " ");

        Intent intent = new Intent("notifyrecycler");
        intent.putExtra("status", mCal.get(Calendar.MONTH));
        sendBroadcast(intent);

    }

    private void select_tv_myteam() {
        tv_myleave.setBackgroundResource(R.drawable.textview_rounded_unselected_left);
        tv_myleave.setTextColor(getResources().getColor(R.color.black));
        tv_myplanner.setBackgroundResource(R.drawable.textview_unselected);
        tv_myplanner.setTextColor(getResources().getColor(R.color.black));
        tv_myteam.setBackgroundResource(R.drawable.textview_rounded_selected_right);
        tv_myteam.setTextColor(getResources().getColor(R.color.white));
    }

}