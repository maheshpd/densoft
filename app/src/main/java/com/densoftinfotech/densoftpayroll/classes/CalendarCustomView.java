package com.densoftinfotech.densoftpayroll.classes;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.densoftinfotech.densoftpayroll.R;
import com.densoftinfotech.densoftpayroll.adapter.CalendarGridAdapter;
import com.densoftinfotech.densoftpayroll.demo_class.CalendarDetailsDemo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    public CalendarCustomView(Context context) {
        super(context);
    }

    public CalendarCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
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
            }
        });
    }

    private void setGridCellClickEvents() {
        calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(context, "Clicked " + position, Toast.LENGTH_LONG).show();
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
        Log.d(TAG, "Number of date " + dayValueInCells.size());

        Log.d(TAG, "Number of days avail " + (mCal.get(Calendar.YEAR)) + " " + (mCal.get(Calendar.MONTH) - 1) + " " + mCal.get(Calendar.DAY_OF_MONTH));


        switch (mCal.get(Calendar.MONTH) - 1) {
            case Calendar.JANUARY:
            case Calendar.MARCH:
            case Calendar.MAY:
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.OCTOBER:
            case Calendar.DECEMBER:{
                checkandadd(31);
                break;
            }

            case Calendar.FEBRUARY:
                if ((mCal.get(Calendar.YEAR) % 4 == 0) && ((mCal.get(Calendar.YEAR) % 100 == 0) || (mCal.get(Calendar.YEAR) % 400 == 0)))
                    checkandadd(29);
                else
                    checkandadd(28);
                break;

            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:{
                checkandadd(30);
                break;
            }

            default:
                checkandadd(31);
                break;
        }


        String sDate = formatter.format(cal.getTime());
        currentDate.setText(sDate);

        mAdapter = new CalendarGridAdapter(context, dayValueInCells, cal, status);
        calendarGridView.setAdapter(mAdapter);

    }

    private void checkandadd(int days) {
        status.clear();

        for (int i = 0; i < days; i++) {
            if (days == 28) {
                status.add(CalendarDetailsDemo.status28[i]);
            } else if (days == 29) {
                status.add(CalendarDetailsDemo.status29[i]);
            } else if (days == 30) {
                status.add(CalendarDetailsDemo.status30[i]);
            } else {
                status.add(CalendarDetailsDemo.status31[i]);
            }
        }

        Log.d("status size ", status.size() + "   " + status);

        Intent intent = new Intent("notifyrecycler");
        intent.putExtra("status", status.size());
        context.sendBroadcast(intent);
    }
}