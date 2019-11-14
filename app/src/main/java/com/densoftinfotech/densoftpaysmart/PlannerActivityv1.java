package com.densoftinfotech.densoftpaysmart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.densoftinfotech.densoftpaysmart.adapter.CalendarDetailsAdapter;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.classes.CalendarCustomView;
import com.densoftinfotech.densoftpaysmart.classes.CalendarDetails;
import com.densoftinfotech.densoftpaysmart.retrofit.GetServiceInterface;
import com.densoftinfotech.densoftpaysmart.retrofit.RetrofitClient;
import com.densoftinfotech.densoftpaysmart.room_database.Paysmart_roomdatabase;
import com.densoftinfotech.densoftpaysmart.room_database.Staff.StaffDetailsRoom;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PlannerActivityv1 extends CommonActivity {

    @BindView(R.id.recyclerview_planner)
    RecyclerView recyclerview_planner;
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    @BindView(R.id.custom_calendar)
    CalendarCustomView mView;
    @BindView(R.id.iv_history)
    TextView iv_history;

    ArrayList<CalendarDetails> calendarDetails = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    CalendarDetailsAdapter calendarDetailsAdapter;
    private GetServiceInterface getServiceInterface;

    private StaffDetailsRoom staffDetailsRoom;
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    private SharedPreferences preferences;

    int month = Calendar.getInstance().get(Calendar.MONTH)+1;
    int year = Calendar.getInstance().get(Calendar.YEAR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_activityv1);

        ButterKnife.bind(this);

        setTitle(getResources().getString(R.string.myplanner));
        back();

        preferences = PreferenceManager.getDefaultSharedPreferences(PlannerActivityv1.this);

        /*LocalBroadcastManager.getInstance(PlannerActivityv1.this).*/
        registerReceiver(broadcastReceiver, new IntentFilter("notifyrecycler"));

        layoutManager = new LinearLayoutManager(PlannerActivityv1.this, LinearLayoutManager.VERTICAL, false);
        recyclerview_planner.setLayoutManager(layoutManager);

        iv_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PlannerActivityv1.this, MarkAttendanceActivity.class);
                i.putExtra("planner_month", String.valueOf(month));
                i.putExtra("planner_year", String.valueOf(year));
                startActivity(i);
            }
        });

        Calendar mCal = (Calendar) cal.clone();
        mCal.add(Calendar.MONTH, 1);
        GetRoomData getRoomData = new GetRoomData();
        getRoomData.execute(String.valueOf(mCal.get(Calendar.MONTH)));


    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.d("recvd value ", "in Plannerv1");
            if (intent.hasExtra("status_month")) {
                month = intent.getIntExtra("status_month", 0);
                //Log.d("month by broadcast ", month + " PlannerV1Activity");

                if (month == 0) {
                    month = 12;
                    GetRoomData getRoomData = new GetRoomData();
                    getRoomData.execute(String.valueOf(12));
                } else {
                    GetRoomData getRoomData = new GetRoomData();
                    getRoomData.execute(String.valueOf(month));
                }

            } else if (intent.hasExtra("scrolltoposition")) {
                //recyclerview.getLayoutManager().smoothScrollToPosition(recyclerview, new RecyclerView.State(), intent.getIntExtra("scrolltoposition", 0));
                int scrollpos = intent.getIntExtra("scrolltoposition", 0) - Constants.count_before_firstpos;

                Log.d("scroll to ",  + scrollpos + "");

                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(PlannerActivityv1.this) {
                    @Override
                    protected int getVerticalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_START;
                    }
                };

                smoothScroller.setTargetPosition(scrollpos);  // pos on which item you want to scroll recycler view
                recyclerview_planner.getLayoutManager().startSmoothScroll(smoothScroller);
            }
            if(intent.hasExtra("status_year")){
                year = intent.getIntExtra("status_year",2019);
            }
        }
    };

    private class GetRoomData extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... voids) {
            staffDetailsRoom = Paysmart_roomdatabase.get_PaysmartDatabase(PlannerActivityv1.this).staffDetails_dao().getAll();
            if (staffDetailsRoom != null) {
                get_attendance_details(staffDetailsRoom.getStaffId(), voids[0]);
                Constants.staffid = staffDetailsRoom.getStaffId();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


        }
    }

    private void get_attendance_details(String staffid, String month_send) {

        Retrofit retrofit = RetrofitClient.getRetrofit();
        getServiceInterface = retrofit.create(GetServiceInterface.class);

        Map<String, Object> params = new HashMap<>();

        params.put("customerid", preferences.getString("customerid", ""));
        params.put("ActionId", "1");
        params.put("StaffId", staffid);
        params.put("Month", String.valueOf(month_send));
        params.put("Year", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));

        JSONObject obj = new JSONObject(params);
        //Log.d("params ", obj + "");

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (obj).toString());

        Call<ArrayList<CalendarDetails>> call = getServiceInterface.request_planner(requestBody);
        call.enqueue(new Callback<ArrayList<CalendarDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<CalendarDetails>> call, Response<ArrayList<CalendarDetails>> response) {
                if (!response.isSuccessful()) {
                    Log.d("response code ", response.code() + " ");
                } else {
                    //Log.d("recvd in planner ", response.body() + "");

                    if (response.body() != null && !response.body().isEmpty()) {
                        calendarDetails = response.body();

                        calendarDetailsAdapter = new CalendarDetailsAdapter(PlannerActivityv1.this, calendarDetails);
                        recyclerview_planner.setAdapter(calendarDetailsAdapter);

                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CalendarDetails>> call, Throwable t) {

            }
        });

    }

    @Override
    public void onDestroy() {

        unregisterReceiver(broadcastReceiver);

        super.onDestroy();
    }
}
