package com.densoftinfotech.densoftpaysmart.app_utilities;

import android.app.ActivityManager;

import android.app.Application;
import android.content.Context;

import java.util.List;

public class ServiceUtils {

        private static String LOG_TAG = ServiceUtils.class.getName();

        public static boolean isServiceRunning(String serviceClassName, Context context){
            final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

            for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
                if (runningServiceInfo.service.getClassName().equals(serviceClassName)){
                    return true;
                }
            }
            return false;
        }

}
