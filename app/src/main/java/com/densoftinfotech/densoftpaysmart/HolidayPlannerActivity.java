package com.densoftinfotech.densoftpaysmart;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.densoftinfotech.densoftpaysmart.adapter.HolidayPlannerAdapter;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.model.CalendarDetails;
import com.densoftinfotech.densoftpaysmart.retrofit.GetServiceInterface;
import com.densoftinfotech.densoftpaysmart.retrofit.RetrofitClient;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HolidayPlannerActivity extends CommonActivity {

    @BindView(R.id.iv_leftarrow)
    ImageView iv_leftarrow;
    @BindView(R.id.iv_rightarrow)
    ImageView iv_rightarrow;
    @BindView(R.id.tv_year)
    TextView tv_year;
    @BindView(R.id.recycler_view_holidayplanner)
    RecyclerView recycler_view_holidayplanner;
    RecyclerView.LayoutManager layoutManager;

    GetServiceInterface getServiceInterface;
    SharedPreferences preferences;
    Map<String, Object> params = new HashMap<>();
    ArrayList<CalendarDetails> planner_details = new ArrayList<>();

    HolidayPlannerAdapter holidayPlannerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_planner);

        ButterKnife.bind(this);
        layoutManager = new LinearLayoutManager(HolidayPlannerActivity.this);
        recycler_view_holidayplanner.setLayoutManager(layoutManager);

        //getsharedpref(HolidayPlannerActivity.this);

        preferences = PreferenceManager.getDefaultSharedPreferences(HolidayPlannerActivity.this);

        Retrofit retrofit = RetrofitClient.getRetrofit();
        getServiceInterface = retrofit.create(GetServiceInterface.class);

        //for (int i = 1; i <= 12; i++) {
            get_planner_details(String.valueOf(8), "2019");
            tv_year.setText("2019");
        //}



    }

    private void get_planner_details(String month, String year) {
        params.clear();
        params.put("customerid", preferences.getString("customerid", ""));
        params.put("ActionId", "1");
        params.put("StaffId", preferences.getString("staffid", ""));
        params.put("Month", month);
        params.put("Year", year);

        JSONObject object = new JSONObject(params);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), object.toString());

        Call<ArrayList<CalendarDetails>> call = getServiceInterface.request_planner(requestBody);
        call.enqueue(new Callback<ArrayList<CalendarDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<CalendarDetails>> call, Response<ArrayList<CalendarDetails>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    planner_details = response.body();
                    holidayPlannerAdapter = new HolidayPlannerAdapter(HolidayPlannerActivity.this, planner_details);
                    recycler_view_holidayplanner.setAdapter(holidayPlannerAdapter);
                }

            }

            @Override
            public void onFailure(Call<ArrayList<CalendarDetails>> call, Throwable t) {

            }
        });
    }
}
