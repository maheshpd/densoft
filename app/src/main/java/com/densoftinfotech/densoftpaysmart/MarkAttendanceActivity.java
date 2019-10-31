package com.densoftinfotech.densoftpaysmart;

import androidx.annotation.NonNull;
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

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.densoftinfotech.densoftpaysmart.adapter.MarkAttendanceAdapter;
import com.densoftinfotech.densoftpaysmart.app_utilities.URLS;
import com.densoftinfotech.densoftpaysmart.classes.MarkAttendanceDetails;
import com.densoftinfotech.densoftpaysmart.location_utilities.UserLocation;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.retrofit.GetServiceInterface;
import com.densoftinfotech.densoftpaysmart.retrofit.RetrofitClient;
import com.densoftinfotech.densoftpaysmart.room_database.Paysmart_roomdatabase;
import com.densoftinfotech.densoftpaysmart.room_database.Staff.StaffDetailsRoom;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.text.pdf.parser.Line;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MarkAttendanceActivity extends CommonActivity {

    @BindView(R.id.tv_checkin)
    TextView tv_checkin;
    @BindView(R.id.tv_checkout)
    TextView tv_checkout;
    @BindView(R.id.tv_checkintime)
    TextView tv_checkintime;
    @BindView(R.id.tv_checkouttime)
    TextView tv_checkouttime;
    @BindView(R.id.et_month)
    EditText et_month;
    @BindView(R.id.recycler_view_markattendance)
    RecyclerView recycler_view_markattendance;
    @BindView(R.id.linearLayout11)
    LinearLayout linearLayout11;

    UserLocation userLocation;
    String longitude = "";
    String latitude = "";

    RecyclerView.LayoutManager layoutManager;
    MarkAttendanceAdapter markAttendanceAdapter;

    GetServiceInterface getServiceInterface;
    ArrayList<MarkAttendanceDetails> markAttendanceDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        toolbar_common();
        back();

        ButterKnife.bind(this);

        layoutManager = new LinearLayoutManager(MarkAttendanceActivity.this);
        recycler_view_markattendance.setLayoutManager(layoutManager);

        tv_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_check_time(0);
            }
        });

        tv_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_check_time(1);
            }
        });

        et_month.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!charSequence.toString().trim().equals("")) {
                    Get_Attendance get_attendance = new Get_Attendance();
                    get_attendance.execute(charSequence.toString());
                }else {
                    linearLayout11.setVisibility(View.GONE);
                    recycler_view_markattendance.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void get_attendance_details(String staffid, String month_send) {

        Retrofit retrofit = RetrofitClient.getRetrofit();

        getServiceInterface = retrofit.create(GetServiceInterface.class);

        Map<String, Object> params = new HashMap<>();
        params.put("ActionId", "0");
        params.put("StaffId", staffid);
        params.put("Month",String.valueOf(month_send));
        params.put("Year", String.valueOf(2019));

        JSONObject obj = new JSONObject(params);
        Log.d("params ", obj + "");

        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (obj).toString());

        Call<ArrayList<MarkAttendanceDetails>> call = getServiceInterface.request_attendance(requestBody);
        call.enqueue(new Callback<ArrayList<MarkAttendanceDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<MarkAttendanceDetails>> call, Response<ArrayList<MarkAttendanceDetails>> response) {
                if(!response.isSuccessful()){
                    Log.d("response code ", response.code() + " ");
                }else {
                    Log.d("response ", response.body() + "");

                    if(response.body()!=null && !response.body().isEmpty()) {
                        linearLayout11.setVisibility(View.VISIBLE);
                        recycler_view_markattendance.setVisibility(View.VISIBLE);
                        markAttendanceDetails = response.body();
                        markAttendanceAdapter = new MarkAttendanceAdapter(MarkAttendanceActivity.this, markAttendanceDetails);
                        recycler_view_markattendance.setAdapter(markAttendanceAdapter);
                    }else {
                        linearLayout11.setVisibility(View.GONE);
                        recycler_view_markattendance.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MarkAttendanceDetails>> call, Throwable t) {

            }
        });

    }

    private void set_check_time(int flag) {
        userLocation = new UserLocation(MarkAttendanceActivity.this);
        if (userLocation.isGpsEnabled()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy ', ' HH:mm:ss z");
            String currentDateandTime = sdf.format(new Date());
            if(flag == 0){
                tv_checkintime.setText(getResources().getString(R.string.checkintime) + " " + currentDateandTime + " at " + userLocation.getAddress());
                tv_checkintime.setVisibility(View.VISIBLE);
            }else{
                tv_checkouttime.setText(getResources().getString(R.string.checkouttime) + " " + currentDateandTime + " at " + userLocation.getAddress());
                tv_checkouttime.setVisibility(View.VISIBLE);
            }
            longitude = Double.toString(userLocation.getLongitude());
            latitude = Double.toString(userLocation.getLatitude());
            Log.d("lat and long checkin ", longitude + "    " + latitude +  " address " +userLocation.getAddress());
        } else {
            userLocation.gpsNotEnabled_Alert();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.attendance_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_checkin:
                set_check_time(0);
                break;
            case R.id.menu_checkout:
                set_check_time(1);
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    private class Get_Attendance extends AsyncTask<String, Void, Void > {

        @Override
        protected Void doInBackground(String... voids) {

            StaffDetailsRoom staffDetails = Paysmart_roomdatabase.get_PaysmartDatabase(MarkAttendanceActivity.this).staffDetails_dao().getAll();

            if(staffDetails!=null){
                get_attendance_details(staffDetails.getStaffId(), voids[0]);
            }

            return null;
        }
    }
}



/*final Animation animLeftToRight = AnimationUtils.loadAnimation(MarkAttendanceActivity.this, R.anim.swiperight);
        animLeftToRight.setRepeatMode(Animation.INFINITE);
        tv_checkin.startAnimation(animLeftToRight);

        final Animation animRightToLeft = AnimationUtils.loadAnimation(MarkAttendanceActivity.this, R.anim.swipeleft);
        animRightToLeft.setRepeatMode(Animation.INFINITE);
        tv_checkout.startAnimation(animRightToLeft);*/

        /*final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(3000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = tv_checkin.getWidth();
                final float translationX = width * progress;
                tv_checkin.setTranslationX(translationX);

                final float translationX1 = - (width * progress);
                tv_checkout.setTranslationX(translationX1);
            }
        });
        animator.start();*/
