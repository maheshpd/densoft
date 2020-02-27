package com.densoftinfotech.densoftpaysmart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.densoftinfotech.densoftpaysmart.adapter.QuickActionsAdapter;
import com.densoftinfotech.densoftpaysmart.adapter.SalarySlipAdapter;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.app_utilities.InternetUtils;
import com.densoftinfotech.densoftpaysmart.app_utilities.SnapHelperOneByOne;
import com.densoftinfotech.densoftpaysmart.location_utilities.LocationMonitoringService;
import com.densoftinfotech.densoftpaysmart.location_utilities.LocationTrackerService;
import com.densoftinfotech.densoftpaysmart.model.QuickActions;
import com.densoftinfotech.densoftpaysmart.model.QuickActionsArray;
import com.densoftinfotech.densoftpaysmart.model.SalarySlip;
import com.densoftinfotech.densoftpaysmart.model.StaffDetails;
import com.densoftinfotech.densoftpaysmart.retrofit.GetServiceInterface;
import com.densoftinfotech.densoftpaysmart.retrofit.RetrofitClient;
import com.densoftinfotech.densoftpaysmart.room_database.Paysmart_roomdatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends CommonActivity {

    @BindView(R.id.recycler_view_salaryslip)
    RecyclerView recycler_view_salaryslip;
    @BindView(R.id.recycler_view_quickactions)
    RecyclerView recycler_view_quickactions;

    @BindView(R.id.iv_settings)
    ImageView iv_settings;
    @BindView(R.id.iv_profile)
    ImageView iv_profile;
    @BindView(R.id.iv_notification)
    ImageView iv_notification;
    @BindView(R.id.iv_logout)
    ImageView iv_logout;

    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_title)
    TextView tv_title;

    private RecyclerView.LayoutManager layoutManager_salaryslip, layoutManager_quickaction;

    private QuickActionsAdapter quickActionsAdapter;

    private ArrayList<SalarySlip> salarySlips = new ArrayList<>();
    private ArrayList<QuickActions> quickActions = new ArrayList<>();
    //private StaffDetailsRoom staffDetailsRoom;
    private SalarySlipAdapter salarySlipAdapter;

    private GetServiceInterface getServiceInterface;
    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;
    private StaffDetails staffDetails;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fullscreen();
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        edit = preferences.edit();

        ButterKnife.bind(this);

        staffDetails = getStaffDetails(MainActivity.this);

        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.firebase_database_name + "/" + preferences.getInt("customerid", 0));

        if(staffDetails!=null){
            Constants.staffid = staffDetails.getStaffId();
            tv_name.setText(" " + staffDetails.getPName());
            tv_title.setText(staffDetails.getCompanyName());

            if (staffDetails.getStaffPhoto() != null && !staffDetails.getStaffPhoto().trim().equals("")) {
                Picasso.get().load(staffDetails.getStaffPhoto()).error(R.mipmap.ic_launcher).into(iv_profile);
            }
        }

        if(InternetUtils.getInstance(MainActivity.this).available()){
            if(staffDetails!=null){
                get_salary_data(staffDetails.getStaffId());
                add_loader1();
            }

        }else{
            Toast.makeText(MainActivity.this, getResources().getString(R.string.msg_alert_no_internet), Toast.LENGTH_SHORT).show();
        }


        /*GetRoomData getRoomData = new GetRoomData();
        getRoomData.execute();*/

        LinearSnapHelper linearSnapHelper = new SnapHelperOneByOne();
        layoutManager_salaryslip = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_view_salaryslip.setLayoutManager(layoutManager_salaryslip);
        linearSnapHelper.attachToRecyclerView(recycler_view_salaryslip);


        iv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inot = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(inot);
            }
        });

        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent stopIntent = new Intent(MainActivity.this, LocationTrackerService.class);
                stopIntent.setAction("stop");
                startService(stopIntent);

                Logout_DeleteUser logout_deleteUser = new Logout_DeleteUser();
                logout_deleteUser.execute();

            }
        });

        layoutManager_quickaction = new GridLayoutManager(this, 3);
        recycler_view_quickactions.setLayoutManager(layoutManager_quickaction);

        quickActions.add(new QuickActions("Salary Details", R.mipmap.salary_details));
        quickActions.add(new QuickActions("Planner", R.mipmap.planner));
        quickActions.add(new QuickActions("Attendance History", R.mipmap.attendance_history));
        //quickActions.add(new QuickActions("Travel Claims", R.mipmap.travel_claims));
        quickActions.add(new QuickActions("Leaves", R.mipmap.leaves));
        quickActions.add(new QuickActions("Team", R.mipmap.team));
        quickActions.add(new QuickActions("Live Tracking", R.mipmap.map_marker));

        /*for (int i = 0; i < QuickActionsArray.names.length; i++) {
            quickActions.add(new QuickActions(QuickActionsArray.names[i], QuickActionsArray.image[i]));
        }*/

        quickActionsAdapter = new QuickActionsAdapter(MainActivity.this, quickActions);
        recycler_view_quickactions.setAdapter(quickActionsAdapter);


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            //Log.d("task failed", "" + task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        //String token = task.getResult().getToken();

                        //Log.d("task success", " " + token);
                    }
                });

    }

    private void get_salary_data(int staffid) {

        Retrofit retrofit = RetrofitClient.getRetrofit();
        getServiceInterface = retrofit.create(GetServiceInterface.class);

        Map<String, Object> params = new HashMap<>();

        params.put("customerid", preferences.getInt("customerid", 0));
        params.put("ActionId", "0");
        params.put("StaffId", staffid);
        params.put("Month", 0);
        params.put("Year", String.valueOf(Calendar.getInstance().get(Calendar.YEAR))); //current year
        JSONObject obj = new JSONObject(params);
        //Log.d("params ", obj + "");

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (obj).toString());
        Call<ArrayList<SalarySlip>> call = getServiceInterface.request_salary(requestBody);


        call.enqueue(new Callback<ArrayList<SalarySlip>>() {
            @Override
            public void onResponse(Call<ArrayList<SalarySlip>> call, Response<ArrayList<SalarySlip>> response) {
                if (!response.isSuccessful()) {
                    //Log.d("response code ", response.code() + " ");
                } else {
                    if (!response.body().isEmpty()) {

                        TreeSet<Integer> newset = new TreeSet<>();
                        salarySlips = response.body();

                        for (SalarySlip record : salarySlips) {
                            newset.add(record.getApplyForMonth());
                        }

                        //Log.d("newset is ", newset.size() + " " + newset);

                        salarySlipAdapter = new SalarySlipAdapter(MainActivity.this, newset, response.body());
                        recycler_view_salaryslip.setAdapter(salarySlipAdapter);
                        dismiss_loader1();


                    } else {
                        dismiss_loader1();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SalarySlip>> call, Throwable t) {

            }
        });

    }

    public void gotoactivity(int image) {

        switch (image) {
            case R.mipmap.salary_details:
                Intent isalary = new Intent(MainActivity.this, SalarySlipDetailsActivity.class);
                startActivity(isalary);
                break;
            case R.mipmap.planner:
                Intent iplan = new Intent(MainActivity.this, PlannerActivityv1.class);
                startActivity(iplan);
                break;
            case R.mipmap.attendance_history:
                Intent iattendance = new Intent(MainActivity.this, MarkAttendanceActivity.class);
                startActivity(iattendance);
                break;
            case R.mipmap.travel_claims:
                break;
            case R.mipmap.leaves:
                Intent ileave = new Intent(MainActivity.this, LeaveListActivity.class);
                startActivity(ileave);
                break;
            case R.mipmap.team:
                Intent iteam = new Intent(MainActivity.this, TeamActivity.class);
                startActivity(iteam);
                break;
            case R.mipmap.map_marker:
                Intent itravel = new Intent(MainActivity.this, LiveTrackingActivityv1.class);
                startActivity(itravel);
                break;
        }
    }

    /*private class GetRoomData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            staffDetailsRoom = Paysmart_roomdatabase.get_PaysmartDatabase(MainActivity.this).staffDetails_dao().getAll();
            Constants.staffid = staffDetailsRoom.getStaffId();
            //Constants.staffDetailsRoom = staffDetailsRoom;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (staffDetailsRoom != null) {
                tv_name.setText(" " + staffDetailsRoom.getPName());
                tv_title.setText(staffDetailsRoom.getCompanyName());
                get_salary_data(staffDetailsRoom.getStaffId());

                if (staffDetailsRoom.getStaffPhoto() != null && !staffDetailsRoom.getStaffPhoto().trim().equals("")) {
                    Picasso.with(MainActivity.this).load(staffDetailsRoom.getStaffPhoto()).error(R.mipmap.ic_launcher).into(iv_profile);
                }
            }

        }
    }*/


    private class Logout_DeleteUser extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Paysmart_roomdatabase.get_PaysmartDatabase(MainActivity.this).staffDetails_dao().delete(Constants.staffid);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            databaseReference.child(String.valueOf(preferences.getInt("staffid", 0))).child("allow_tracking").setValue(0);
            edit.clear();
            edit.apply();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();

        }
    }

    private void add_loader1() {

        if (!((Activity) MainActivity.this).isFinishing()) {
            progressDialog = ProgressDialog.show(MainActivity.this, "Loading", "Please wait...");
            progressDialog.setCancelable(false);
        }
    }

    private void dismiss_loader1() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            //Toast.makeText(MainActivity.this, " Pressed Volume Up", Toast.LENGTH_SHORT).show();
            return true;
        }else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            //Toast.makeText(MainActivity.this, " Pressed Volume Down", Toast.LENGTH_SHORT).show();
            return true;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
