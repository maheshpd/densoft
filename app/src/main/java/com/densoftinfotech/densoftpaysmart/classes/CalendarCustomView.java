package com.densoftinfotech.densoftpaysmart.classes;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.densoftinfotech.densoftpaysmart.R;
import com.densoftinfotech.densoftpaysmart.adapter.CalendarDetailsAdapter;
import com.densoftinfotech.densoftpaysmart.adapter.CalendarGridAdapter;
import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.demo_class.CalendarDetailsDemo;
import com.densoftinfotech.densoftpaysmart.retrofit.GetServiceInterface;
import com.densoftinfotech.densoftpaysmart.retrofit.RetrofitClient;
import com.densoftinfotech.densoftpaysmart.room_database.Paysmart_roomdatabase;
import com.densoftinfotech.densoftpaysmart.room_database.Staff.StaffDetailsRoom;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CalendarCustomView extends LinearLayout {
    private static final String TAG = CalendarCustomView.class.getSimpleName();
    private ImageView previousButton, nextButton;
    private TextView currentDate;
    private GridView calendarGridView;
    private Button addEventButton;
    private static final int MAX_CALENDAR_COLUMN = 42;
    private int month, year;
    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    private Context context;
    private CalendarGridAdapter mAdapter;

    ArrayList<String> status = new ArrayList<>();
    ArrayList<CalendarDetails> calendarDetails = new ArrayList<>();

    private GetServiceInterface getServiceInterface;
    private StaffDetailsRoom staffDetailsRoom;

    public CalendarCustomView(Context context, ArrayList<CalendarDetails> calendarDetails) {
        super(context);
        this.context = context;
        this.calendarDetails = calendarDetails;

    }

    public CalendarCustomView(Context context, AttributeSet attrs/*, ArrayList<CalendarDetails> calendarDetails*/) {
        super(context, attrs);
        this.context = context;
        //Log.d("calendar size is ", calendarDetails.size() + "");
        initializeUILayout();
        setUpCalendarAdapter();
        setPreviousButtonClickEvent();
        setNextButtonClickEvent();
        setGridCellClickEvents();
        //Log.d(TAG, "I need to call this method");
    }

    public CalendarCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }




    private void initializeUILayout() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        previousButton = view.findViewById(R.id.previous_month);
        nextButton = view.findViewById(R.id.next_month);
        currentDate = view.findViewById(R.id.display_current_date);
        calendarGridView = view.findViewById(R.id.calendar_grid);
    }

    private void setPreviousButtonClickEvent() {
        previousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, -1);
                setUpCalendarAdapter();
            }
        });
    }

    private void setNextButtonClickEvent() {
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, 1);
                setUpCalendarAdapter();
                /*Calendar c = Calendar.getInstance(Locale.ENGLISH);
                c.add(Calendar.MONTH, 1);

                Date date1 = c.getTime();
                Date date2 = Calendar.getInstance().getTime();

                if(date1.before(date2)){
                    cal.add(Calendar.MONTH, 1);
                    setUpCalendarAdapter();
                }*/

            }
        });
    }

    private void setGridCellClickEvents() {
        calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "Clicked " + position, Toast.LENGTH_LONG).show();
                Intent intent = new Intent("notifyrecycler");
                intent.putExtra("scrolltoposition", position);
                context.sendBroadcast(intent);
            }
        });

        /*calendarGridView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                int act = motionEvent.getActionMasked();
               *//* if(act == MotionEvent.ACTION_UP){
                    Log.d("here ", act + "");
                }else *//*if(act == MotionEvent.ACTION_MOVE){
                    Log.d("here ", act + "");
                }else {

                }
                return false;
            }
        });*/
    }

    private void setUpCalendarAdapter() {
        List<Date> dayValueInCells = new ArrayList<Date>();

        Calendar mCal = (Calendar) cal.clone();
        mCal.set(Calendar.DAY_OF_MONTH, 1);

        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);

        while (dayValueInCells.size() < MAX_CALENDAR_COLUMN) {
            dayValueInCells.add(mCal.getTime());
            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        //Log.d(TAG, "Number of date " + dayValueInCells.size());

        //Log.d(TAG, "Number of days avail " + (mCal.get(Calendar.YEAR)) + " " + (mCal.get(Calendar.MONTH)) + " " + mCal.get(Calendar.DAY_OF_MONTH));

        Intent intent = new Intent("notifyrecycler");
        intent.putExtra("status", mCal.get(Calendar.MONTH));
        context.sendBroadcast(intent);

        String sDate = formatter.format(cal.getTime());
        currentDate.setText(sDate);

        if(mCal.get(Calendar.MONTH) == 0) {
            get_attendance_details(Constants.staffid, String.valueOf(12), dayValueInCells);
        }else {
            get_attendance_details(Constants.staffid, String.valueOf(mCal.get(Calendar.MONTH)), dayValueInCells);
        }



    }

    private void get_attendance_details(String staffid, String month_send, List<Date> dayValueInCells) {

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

                        mAdapter = new CalendarGridAdapter(context, dayValueInCells, cal, calendarDetails);
                        calendarGridView.setAdapter(mAdapter);


                    }else {

                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CalendarDetails>> call, Throwable t) {

            }
        });
    }

}