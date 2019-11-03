package com.densoftinfotech.densoftpaysmart.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.densoftinfotech.densoftpaysmart.MainActivity;
import com.densoftinfotech.densoftpaysmart.MarkAttendanceActivity;
import com.densoftinfotech.densoftpaysmart.R;
import com.densoftinfotech.densoftpaysmart.adapter.CalendarDetailsAdapter;
import com.densoftinfotech.densoftpaysmart.adapter.MarkAttendanceAdapter;
import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.classes.CalendarCustomView;
import com.densoftinfotech.densoftpaysmart.classes.CalendarDetails;
import com.densoftinfotech.densoftpaysmart.classes.MarkAttendanceDetails;
import com.densoftinfotech.densoftpaysmart.demo_class.CalendarDetailsDemo;
import com.densoftinfotech.densoftpaysmart.retrofit.GetServiceInterface;
import com.densoftinfotech.densoftpaysmart.retrofit.RetrofitClient;
import com.densoftinfotech.densoftpaysmart.room_database.Paysmart_roomdatabase;
import com.densoftinfotech.densoftpaysmart.room_database.Staff.StaffDetailsRoom;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;

public class MyPlannerFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @BindView(R.id.recyclerview_planner)
    RecyclerView recyclerview;
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    @BindView(R.id.custom_calendar)
    CalendarCustomView mView;
    @BindView(R.id.iv_history)
    ImageView iv_history;

    View v;
    Calendar calendar = Calendar.getInstance();

    ArrayList<CalendarDetails> calendarDetails = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    CalendarDetailsAdapter calendarDetailsAdapter;
    private GetServiceInterface getServiceInterface;

    private StaffDetailsRoom staffDetailsRoom;

    public MyPlannerFragment() {
        // Required empty public constructor
    }
    public static MyPlannerFragment newInstance(String param1, String param2) {
        MyPlannerFragment fragment = new MyPlannerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_my_planner, container, false);

        if(getContext()!=null) {
            getContext().registerReceiver(broadcastReceiver, new IntentFilter("notifyrecycler"));
        }

        ButterKnife.bind(this, v);
        scrollview.fullScroll(ScrollView.FOCUS_UP);
        recyclerview.setFocusable(false);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);

        iv_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MarkAttendanceActivity.class);
                getActivity().startActivity(i);
            }
        });


        return v;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.hasExtra("status")){
                int month = intent.getIntExtra("status", 0);
                Log.d("month by broadcast ", month + "");

                if(month == 0){
                    GetRoomData getRoomData = new GetRoomData();
                    getRoomData.execute(String.valueOf(12));
                }else {
                    GetRoomData getRoomData = new GetRoomData();
                    getRoomData.execute(String.valueOf(month));
                }


                //checkandadd(month);
            }else if(intent.hasExtra("scrolltoposition")){
                //recyclerview.getLayoutManager().smoothScrollToPosition(recyclerview, new RecyclerView.State(), intent.getIntExtra("scrolltoposition", 0));
                Log.d("month by broadcast ", intent.getIntExtra("scrolltoposition", 0) + "");

                LinearSmoothScroller smoothScroller=new LinearSmoothScroller(getActivity()){
                    @Override
                    protected int getVerticalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_START;
                    }
                };

                smoothScroller.setTargetPosition(intent.getIntExtra("scrolltoposition", 0));  // pos on which item you want to scroll recycler view
                recyclerview.getLayoutManager().startSmoothScroll(smoothScroller);
            }
        }
    };

    @Override
    public void onDestroy() {
        if(getContext()!=null){
            getContext().unregisterReceiver(broadcastReceiver);
        }

        super.onDestroy();
    }

    private void get_attendance_details(String staffid, String month_send) {

        Retrofit retrofit = RetrofitClient.getRetrofit();

        getServiceInterface = retrofit.create(GetServiceInterface.class);

        Map<String, Object> params = new HashMap<>();
        params.put("ActionId", "1");
        params.put("StaffId", staffid);
        params.put("Month",String.valueOf(month_send));
        params.put("Year", String.valueOf(2019));

        JSONObject obj = new JSONObject(params);
        Log.d("params ", obj + "");

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (obj).toString());

        Call<ArrayList<CalendarDetails>> call = getServiceInterface.request_planner(requestBody);
        call.enqueue(new Callback<ArrayList<CalendarDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<CalendarDetails>> call, Response<ArrayList<CalendarDetails>> response) {
                if(!response.isSuccessful()){
                    Log.d("response code ", response.code() + " ");
                }else {
                    Log.d("response ", response.body() + "");

                    if(response.body()!=null && !response.body().isEmpty()) {
                        calendarDetails = response.body();

                        calendarDetailsAdapter = new CalendarDetailsAdapter(getActivity(), calendarDetails);
                        recyclerview.setAdapter(calendarDetailsAdapter);

                    }else {

                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CalendarDetails>> call, Throwable t) {

            }
        });

    }

    private class GetRoomData extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... voids) {
            staffDetailsRoom = Paysmart_roomdatabase.get_PaysmartDatabase(getActivity()).staffDetails_dao().getAll();
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
}
