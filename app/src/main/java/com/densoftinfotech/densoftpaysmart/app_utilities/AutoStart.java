package com.densoftinfotech.densoftpaysmart.app_utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.densoftinfotech.densoftpaysmart.MainActivity;
import com.densoftinfotech.densoftpaysmart.background_service.LocationTrackerService1;

public class AutoStart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("servicePrefs", Context.MODE_PRIVATE);
        boolean serviceActivated = sharedPreferences.getBoolean("serviceActivated", false);
        if (serviceActivated) {

            if(DateUtils.within_office_hours()){
                context.startService(new Intent(context, LocationTrackerService1.class));
            }
        }
    }
}
