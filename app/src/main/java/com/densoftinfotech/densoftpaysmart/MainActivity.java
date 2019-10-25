package com.densoftinfotech.densoftpaysmart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.densoftinfotech.densoftpaysmart.adapter.QuickActionsAdapter;
import com.densoftinfotech.densoftpaysmart.adapter.SalarySlipAdapter;
import com.densoftinfotech.densoftpaysmart.classes.QuickActions;
import com.densoftinfotech.densoftpaysmart.classes.SalarySlip;
import com.densoftinfotech.densoftpaysmart.demo_class.QuickActionsDemo;
import com.densoftinfotech.densoftpaysmart.demo_class.SalarySlipDemo;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import androidx.annotation.NonNull;
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

        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(Constants.channel_id, Constants.channel_name, importance);
            mChannel.setDescription(Constants.channel_description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }


        FirebaseMessaging.getInstance().subscribeToTopic("DensoftUser");*/

        FirebaseMessaging.getInstance().subscribeToTopic("DensoftUser").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    public void gotoactivity(String name) {

        if(name.equalsIgnoreCase("Planner")){
            Intent i = new Intent(MainActivity.this, PlannerActivity.class);
            startActivity(i);
        }else if(name.equalsIgnoreCase("Salary Details")){
            Intent i = new Intent(MainActivity.this, SalaryDetailsActivity.class);
            startActivity(i);
        }else if(name.equalsIgnoreCase("Attendance History")){
            Intent i = new Intent(MainActivity.this, MarkAttendanceActivity.class);
            startActivity(i);
        }else if(name.equalsIgnoreCase("Travel Claims")){
            Intent i = new Intent(MainActivity.this, MarkAttendanceActivity.class);
            startActivity(i);
        }
    }
}
