package com.densoftinfotech.densoftpaysmart;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.densoftinfotech.densoftpaysmart.adapter.PlannerAdapter;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.classes.StaffDetails;
import com.densoftinfotech.densoftpaysmart.room_database.Paysmart_roomdatabase;
import com.densoftinfotech.densoftpaysmart.room_database.Staff.StaffDetailsRoom;

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

    PlannerAdapter plannerAdapter;

    private StaffDetailsRoom staffDetailsRoom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);
        toolbar_common();
        back();

        ButterKnife.bind(this);

        plannerAdapter = new PlannerAdapter(getSupportFragmentManager(), 1);
        viewpager.setAdapter(plannerAdapter);

        GetRoomData getRoomData = new GetRoomData();
        getRoomData.execute();


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
    }

    private void select_tv_myteam() {
        tv_myleave.setBackgroundResource(R.drawable.textview_rounded_unselected_left);
        tv_myleave.setTextColor(getResources().getColor(R.color.black));
        tv_myplanner.setBackgroundResource(R.drawable.textview_unselected);
        tv_myplanner.setTextColor(getResources().getColor(R.color.black));
        tv_myteam.setBackgroundResource(R.drawable.textview_rounded_selected_right);
        tv_myteam.setTextColor(getResources().getColor(R.color.white));
    }


    private class GetRoomData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            staffDetailsRoom = Paysmart_roomdatabase.get_PaysmartDatabase(PlannerActivity.this).staffDetails_dao().getAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (staffDetailsRoom != null) {
                tv_name.setText(staffDetailsRoom.getPName());
            }

        }
    }
}