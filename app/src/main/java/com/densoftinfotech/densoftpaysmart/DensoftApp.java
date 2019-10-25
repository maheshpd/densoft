package com.densoftinfotech.densoftpaysmart;

import android.content.Context;

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
