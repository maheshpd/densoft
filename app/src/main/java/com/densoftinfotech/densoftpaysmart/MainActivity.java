package com.densoftinfotech.densoftpaysmart;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.densoftinfotech.densoftpaysmart.adapter.QuickActionsAdapter;
import com.densoftinfotech.densoftpaysmart.adapter.SalarySlipAdapter;
import com.densoftinfotech.densoftpaysmart.app_utilities.URLS;
import com.densoftinfotech.densoftpaysmart.classes.QuickActions;
import com.densoftinfotech.densoftpaysmart.classes.SalarySlip;
import com.densoftinfotech.densoftpaysmart.classes.StaffDetails;
import com.densoftinfotech.densoftpaysmart.demo_class.QuickActionsDemo;
import com.densoftinfotech.densoftpaysmart.demo_class.SalarySlipDemo;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.retrofit.GetServiceInterface;
import com.densoftinfotech.densoftpaysmart.retrofit.RetrofitClient;
import com.densoftinfotech.densoftpaysmart.room_database.Paysmart_roomdatabase;
import com.densoftinfotech.densoftpaysmart.room_database.Staff.StaffDetailsRoom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import retrofit2.converter.gson.GsonConverterFactory;

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

    SalarySlipAdapter salarySlipAdapter;
    QuickActionsAdapter quickActionsAdapter;

    ArrayList<SalarySlip> salarySlips = new ArrayList<>();
    ArrayList<QuickActions> quickActions = new ArrayList<>();
    StaffDetailsRoom staffDetailsRoom;

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

        layoutManager_quickaction = new GridLayoutManager(this, 4);
        recycler_view_quickactions.setLayoutManager(layoutManager_quickaction);


        /*for (int i = 0; i < SalarySlipDemo.month.length; i++) {
            salarySlips.add(new SalarySlip(SalarySlipDemo.apply_for_month[i], SalarySlipDemo.days_of_month[i], SalarySlipDemo.month_short[i], SalarySlipDemo.deductions[i]));
        }*/



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
            Call<ArrayList<SalarySlip>> call = getServiceInterface.request_salary(requestBody);

            call.enqueue(new Callback<ArrayList<SalarySlip>>() {
                @Override
                public void onResponse(Call<ArrayList<SalarySlip>> call, Response<ArrayList<SalarySlip>> response) {
                    if(!response.isSuccessful()){
                        Log.d("response code ", response.code() + " ");
                    }else {
                        if(!response.body().isEmpty()){


                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                List<SalarySlip> distinctElements = response.body().stream()
                                        .filter( distinctByKey(p -> p.getApplyForMonth()) )
                                        .collect( Collectors.toList() );
                                Log.d("SalarySlipscount ",  distinctElements.size() + "");
                            }



                            //Set<SalarySlip> salarySlipscount = new HashSet<SalarySlip>(response.body());


                            /*salarySlipAdapter = new SalarySlipAdapter(MainActivity.this, salarySlips);
                            recycler_view_salaryslip.setAdapter(salarySlipAdapter);*/

                            Log.d("response ", response.body() + " " + response.body().size());

                        }else{

                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<SalarySlip>> call, Throwable t) {

                }
            });

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor)
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public void gotoactivity(String name) {

        if (name.equalsIgnoreCase("Planner")) {
            Intent i = new Intent(MainActivity.this, PlannerActivity.class);
            startActivity(i);
        } else if (name.equalsIgnoreCase("Salary Details")) {
            Intent i = new Intent(MainActivity.this, SalaryDetailsActivity.class);
            startActivity(i);
        } else if (name.equalsIgnoreCase("Attendance History")) {
            Intent i = new Intent(MainActivity.this, MarkAttendanceActivity.class);
            startActivity(i);
        } else if (name.equalsIgnoreCase("Travel Claims")) {
            Intent i = new Intent(MainActivity.this, MarkAttendanceActivity.class);
            startActivity(i);
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
            }

        }
    }
}
