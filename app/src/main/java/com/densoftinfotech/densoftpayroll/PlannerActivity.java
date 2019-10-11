package com.densoftinfotech.densoftpayroll;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ScrollView;

import com.densoftinfotech.densoftpayroll.adapter.CalendarDetailsAdapter;
import com.densoftinfotech.densoftpayroll.classes.CalendarDetails;
import com.densoftinfotech.densoftpayroll.demo_class.CalendarDetailsDemo;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PlannerActivity extends AppCompatActivity {


    CalendarView calendar;
    ArrayList<CalendarDetails> calendarDetails = new ArrayList<>();
    RecyclerView recyclerview;
    RecyclerView.LayoutManager layoutManager;
    CalendarDetailsAdapter calendarDetailsAdapter;
    ScrollView scrollview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner);

        calendar = findViewById(R.id.calendar);
        recyclerview = findViewById(R.id.recyclerview);
        scrollview = findViewById(R.id.scrollview);

        scrollview.fullScroll(ScrollView.FOCUS_UP);

        layoutManager = new LinearLayoutManager(PlannerActivity.this);
        recyclerview.setLayoutManager(layoutManager);

        for (int i = 0; i < CalendarDetailsDemo.status.length; i++) {
            calendarDetails.add(new CalendarDetails(CalendarDetailsDemo.status[i], CalendarDetailsDemo.description[i]));
        }

        calendarDetailsAdapter = new CalendarDetailsAdapter(PlannerActivity.this, calendarDetails);
        recyclerview.setAdapter(calendarDetailsAdapter);

    }
}
