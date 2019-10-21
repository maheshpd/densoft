package com.densoftinfotech.densoftpayroll;

import android.content.Context;

import com.google.firebase.messaging.FirebaseMessaging;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

public class DensoftApp extends MultiDexApplication {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        //FirebaseMessaging.getInstance().setAutoInitEnabled(true);
    }
}
