package com.densoftinfotech.densoftpaysmart;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.densoftinfotech.densoftpaysmart.adapter.LeaveAdapter;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.classes.LeaveDetails;
import com.densoftinfotech.densoftpaysmart.retrofit.GetServiceInterface;
import com.densoftinfotech.densoftpaysmart.retrofit.RetrofitClient;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

import static com.densoftinfotech.densoftpaysmart.app_utilities.Constants.staffid;

public class LeaveListActivity extends CommonActivity {

    @BindView(R.id. recyclerview_leave)
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    LeaveAdapter leaveAdapter;

    private ArrayList<LeaveDetails> leaveDetails = new ArrayList<>();
    private GetServiceInterface getServiceInterface;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_list);

        setTitle(getResources().getString(R.string.leave));
        back();

        ButterKnife.bind(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(LeaveListActivity.this);

        layoutManager = new LinearLayoutManager(LeaveListActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        Retrofit retrofit = RetrofitClient.getRetrofit();
        getServiceInterface = retrofit.create(GetServiceInterface.class);

        Map<String, Object> params = new HashMap<>();

        params.put("customerid", preferences.getString("customerid", ""));
        params.put("ActionId", "0");
        params.put("StaffId", staffid);

        JSONObject obj = new JSONObject(params);
        //Log.d("params ", obj + "");

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (obj).toString());
        Call<ArrayList<LeaveDetails>> call = getServiceInterface.request_leavedetails(requestBody);

        call.enqueue(new Callback<ArrayList<LeaveDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<LeaveDetails>> call, Response<ArrayList<LeaveDetails>> response) {
                if (!response.isSuccessful()) {
                    //Log.d("response code ", response.code() + " ");
                } else {
                    if (!response.body().isEmpty()) {

                        leaveDetails = response.body();

                        leaveAdapter = new LeaveAdapter(LeaveListActivity.this, leaveDetails);
                        recyclerView.setAdapter(leaveAdapter);

                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<LeaveDetails>> call, Throwable t) {

            }
        });

    }
}
