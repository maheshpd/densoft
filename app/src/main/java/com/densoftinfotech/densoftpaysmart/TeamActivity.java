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
import android.util.Log;
import android.widget.Toast;

import com.densoftinfotech.densoftpaysmart.adapter.TeamListAdapter;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.model.CheckLeaveStatus;
import com.densoftinfotech.densoftpaysmart.model.TeamList;
import com.densoftinfotech.densoftpaysmart.retrofit.GetServiceInterface;
import com.densoftinfotech.densoftpaysmart.retrofit.RetrofitClient;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeamActivity extends CommonActivity {

    @BindView(R.id.recycler_view_team)
    RecyclerView recycler_view_team;

    RecyclerView.LayoutManager layoutManager;
    GetServiceInterface getServiceInterface;

    SharedPreferences preferences;
    ArrayList<TeamList> teamLists = new ArrayList<>();

    TeamListAdapter teamListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        ButterKnife.bind(this);

        layoutManager = new LinearLayoutManager(TeamActivity.this);
        recycler_view_team.setLayoutManager(layoutManager);

        preferences = PreferenceManager.getDefaultSharedPreferences(TeamActivity.this);

        setTitle(getResources().getString(R.string.myteam));
        back();

        get_approval_list();
    }

    private void get_approval_list(){
        Retrofit retrofit = RetrofitClient.getRetrofit();
        getServiceInterface = retrofit.create(GetServiceInterface.class);
        Map<String, Object> params = new HashMap<>();

        params.put("ActionId", "1");
        params.put("StaffId", preferences.getString("staffid",""));
        params.put("Month", "");
        params.put("Year", "");
        params.put("customerid", preferences.getString("customerid", ""));

        JSONObject obj = new JSONObject(params);
        Log.d("params ", obj + "");

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (obj).toString());

        Call<ArrayList<TeamList>> call = getServiceInterface.request_team_list(requestBody);
        call.enqueue(new Callback<ArrayList<TeamList>>() {
            @Override
            public void onResponse(Call<ArrayList<TeamList>> call, Response<ArrayList<TeamList>> response) {
                if (!response.isSuccessful()) {
                    //Log.d("response code ", response.code() + " ");
                } else {
                    //Log.d("response ", response.body() + "");

                    if (response.body() != null && !response.body().isEmpty()) {

                        teamLists = response.body();
                        teamListAdapter = new TeamListAdapter(TeamActivity.this, teamLists);
                        recycler_view_team.setAdapter(teamListAdapter);

                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TeamList>> call, Throwable t) {

            }
        });
    }

    public void gotoGetStatus(TeamList teamList, String status, String status_name) {
        add_loader(TeamActivity.this);
        Map<String, Object> params = new HashMap<>();

        params.put("customerid", preferences.getString("customerid", ""));
        params.put("ActionId", "1");
        params.put("StaffId", teamList.getStaffId());
        params.put("Name", teamList.getEmpName());
        params.put("ApplicationDate", "");
        params.put("LeaveType", teamList.getLeaveType());
        params.put("NoOfDays", teamList.getNoOfDays());
        params.put("FromDate", teamList.getFromDate());
        params.put("ToDate", teamList.getToDate());
        params.put("Description", "");
        params.put("ContactNo", "");
        params.put("CareOfStaff", "0");
        params.put("Priority", "0");
        params.put("Status", status);
        params.put("P_Id", teamList.getP_Id());
        params.put("Reason", "");
        params.put("RemainingLeave", "0");
        params.put("SendTo", "0");
        params.put("AcceptedBy", preferences.getString("staffid",""));
        params.put("aNoOfDays", "0");
        params.put("aFromDate", "");
        params.put("aToDate", "");
        params.put("typeId", "0");
        params.put("FromTime", "");
        params.put("toTime", "");
        params.put("ConfirmRelieve", "");


        JSONObject obj = new JSONObject(params);
        //Log.d("params ", obj + "");

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (obj).toString());
        Call<ArrayList<CheckLeaveStatus>> call = getServiceInterface.request_leave_applied_success(requestBody);

        call.enqueue(new Callback<ArrayList<CheckLeaveStatus>>() {
            @Override
            public void onResponse(Call<ArrayList<CheckLeaveStatus>> call, Response<ArrayList<CheckLeaveStatus>> response) {
                if (!response.isSuccessful()) {
                    //Log.d("response code ", response.code() + " ");
                } else {

                    if (response.body() != null) {

                        if(response.body().get(0).getStatus().equalsIgnoreCase("1")){

                            if(status_name.equalsIgnoreCase(getResources().getString(R.string.onhold))){
                                Toast.makeText(TeamActivity.this, "You have put the leave " + status_name, Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(TeamActivity.this, "You have " + status_name + "ed the leave", Toast.LENGTH_SHORT).show();
                            }
                        }

                        dismiss_loader();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CheckLeaveStatus>> call, Throwable t) {

            }
        });
    }
}
