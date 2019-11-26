package com.densoftinfotech.densoftpaysmart;

import android.os.Bundle;

import com.densoftinfotech.densoftpaysmart.adapter.NotificationAdapter;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.classes.NotificationReceived;
import com.densoftinfotech.densoftpaysmart.sqlitedatabase.DatabaseHelper;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationActivity extends CommonActivity {

    private RecyclerView recycler_view_notification;
    private RecyclerView.LayoutManager layoutManager;
    private NotificationAdapter notificationAdapter;
    private ArrayList<NotificationReceived> notificationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        setTitle(getResources().getString(R.string.notifications));
        back();

        recycler_view_notification = findViewById(R.id.recycler_view_notification);

        layoutManager = new LinearLayoutManager(NotificationActivity.this);
        recycler_view_notification.setLayoutManager(layoutManager);

        notificationList = DatabaseHelper.getInstance(NotificationActivity.this).get_NotificationData();
        notificationAdapter = new NotificationAdapter(NotificationActivity.this, notificationList);
        recycler_view_notification.setAdapter(notificationAdapter);

    }

}
