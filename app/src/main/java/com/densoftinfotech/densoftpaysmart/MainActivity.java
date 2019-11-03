package com.densoftinfotech.densoftpaysmart;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.densoftinfotech.densoftpaysmart.adapter.QuickActionsAdapter;
import com.densoftinfotech.densoftpaysmart.adapter.SalaryDistinctAdapter;
import com.densoftinfotech.densoftpaysmart.adapter.SalarySlipAdapter;
import com.densoftinfotech.densoftpaysmart.classes.QuickActions;
import com.densoftinfotech.densoftpaysmart.classes.SalarySlipDistinct;
import com.densoftinfotech.densoftpaysmart.classes.SalarySlip;
import com.densoftinfotech.densoftpaysmart.demo_class.QuickActionsArray;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.retrofit.GetServiceInterface;
import com.densoftinfotech.densoftpaysmart.retrofit.RetrofitClient;
import com.densoftinfotech.densoftpaysmart.room_database.Paysmart_roomdatabase;
import com.densoftinfotech.densoftpaysmart.room_database.Staff.StaffDetailsRoom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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

    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_title)
    TextView tv_title;


    RecyclerView.LayoutManager layoutManager_salaryslip, layoutManager_quickaction;

    SalaryDistinctAdapter salaryDistinctAdapter;
    QuickActionsAdapter quickActionsAdapter;

    ArrayList<SalarySlipDistinct> salarySlipDistincts = new ArrayList<>();
    ArrayList<QuickActions> quickActions = new ArrayList<>();
    StaffDetailsRoom staffDetailsRoom;

    ArrayList<SalarySlip> salarySlips = new ArrayList<>();
    SalarySlipAdapter salarySlipAdapter;

    private GetServiceInterface getServiceInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fullscreen();
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        GetRoomData getRoomData = new GetRoomData();
        getRoomData.execute();


        layoutManager_salaryslip = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_view_salaryslip.setLayoutManager(layoutManager_salaryslip);

        layoutManager_quickaction = new GridLayoutManager(this, 2);
        recycler_view_quickactions.setLayoutManager(layoutManager_quickaction);


        /*for (int i = 0; i < SalarySlipDemo.month.length; i++) {
            salarySlipDistincts.add(new SalarySlipDistinct(SalarySlipDemo.apply_for_month[i], SalarySlipDemo.days_of_month[i], SalarySlipDemo.month_short[i], SalarySlipDemo.deductions[i]));
        }*/


        for (int i = 0; i < QuickActionsArray.names.length; i++) {
            quickActions.add(new QuickActions(QuickActionsArray.names[i], QuickActionsArray.image[i]));
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

    private void get_salary_data(String staffid) {


        Retrofit retrofit = RetrofitClient.getRetrofit();

        getServiceInterface = retrofit.create(GetServiceInterface.class);

        Map<String, Object> params = new HashMap<>();
        params.put("ActionId", "0");
        params.put("StaffId", staffid);
        params.put("Month", 0);
        params.put("Year", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        JSONObject obj = new JSONObject(params);
        Log.d("params ", obj + "");

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (obj).toString());
        Call<ArrayList<SalarySlipDistinct>> call = getServiceInterface.request_salary(requestBody);

        call.enqueue(new Callback<ArrayList<SalarySlipDistinct>>() {
            @Override
            public void onResponse(Call<ArrayList<SalarySlipDistinct>> call, Response<ArrayList<SalarySlipDistinct>> response) {
                if (!response.isSuccessful()) {
                    Log.d("response code ", response.code() + " ");
                } else {
                    if (!response.body().isEmpty()) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            ArrayList<SalarySlipDistinct> distinctElements = (ArrayList<SalarySlipDistinct>) response.body().stream()
                                    .filter(distinctByKey(SalarySlipDistinct::getApplyForMonth))
                                    .collect(Collectors.toList());
                            Log.d("SalarySlipscount ", distinctElements.size() + "");

                            salarySlipDistincts = distinctElements;


                        }


                        Log.d("size distinct ", salarySlipDistincts.size() + " \nsize arraylist " + response.body().size());
                        for (int i = 0; i < salarySlipDistincts.size(); i++) {

                            /*salarySlips.clear();
                            for (int j = 0; j < response.body().size(); j++) {
                                if (response.body().get(j).getApplyForMonth() == salarySlipDistincts.get(i).getApplyForMonth()) {
                                    Log.d("distinct elements are ", response.body().get(j).getApplyForMonth() + "");
                                    salarySlips.add(new SalarySlip(response.body().get(j).getName(),
                                            response.body().get(j).getAmount(), response.body().get(j).getApplyForMonth(),
                                            response.body().get(j).getApplyForYear()));

                                }

                                Log.d("salary slip added ", salarySlips.size() + "");

                            }*/

                            salaryDistinctAdapter = new SalaryDistinctAdapter(MainActivity.this, response.body(), salarySlipDistincts);
                            recycler_view_salaryslip.setAdapter(salaryDistinctAdapter);

                            /*salarySlipAdapter = new SalarySlipAdapter(MainActivity.this, salarySlips);
                            recycler_view_salaryslip.setAdapter(salarySlipAdapter);*/


                        }

                        /*salarySlipAdapter = new SalarySlipAdapter(MainActivity.this, salarySlipDistincts);
                        recycler_view_salaryslip.setAdapter(salarySlipAdapter);*/

                        //Set<SalarySlipDistinct> salarySlipscount = new HashSet<SalarySlipDistinct>(response.body());


                            /*salarySlipAdapter = new SalarySlipAdapter(MainActivity.this, salarySlipDistincts);
                            recycler_view_salaryslip.setAdapter(salarySlipAdapter);*/

                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SalarySlipDistinct>> call, Throwable t) {

            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public void gotoactivity(int image) {

        switch (image) {
            case R.mipmap.salarydetails:
                Intent isalary = new Intent(MainActivity.this, SalaryDetailsActivity.class);
                startActivity(isalary);
                break;
            case R.mipmap.planner:
                Intent iplan = new Intent(MainActivity.this, PlannerActivity.class);
                startActivity(iplan);
                break;
            case R.mipmap.attendancehistory:
                Intent iattendance = new Intent(MainActivity.this, MarkAttendanceActivity.class);
                startActivity(iattendance);
                break;
            case R.mipmap.travelclaims:
                Intent itravel = new Intent(MainActivity.this, MarkAttendanceActivity.class);
                startActivity(itravel);
                break;
        }
    }

    private class GetRoomData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            staffDetailsRoom = Paysmart_roomdatabase.get_PaysmartDatabase(MainActivity.this).staffDetails_dao().getAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (staffDetailsRoom != null) {
                tv_name.setText(getResources().getString(R.string.welcome) + " " + staffDetailsRoom.getPName());
                tv_title.setText(staffDetailsRoom.getCompanyName());
                get_salary_data(staffDetailsRoom.getStaffId());

                Picasso.with(MainActivity.this).load(staffDetailsRoom.getStaffPhoto()).error(R.mipmap.ic_launcher).into(iv_profile);
            }

        }
    }
}
