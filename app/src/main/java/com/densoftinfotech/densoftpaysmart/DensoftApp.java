package com.densoftinfotech.densoftpaysmart;

import android.content.Context;

import com.google.firebase.messaging.FirebaseMessaging;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DensoftApp extends MultiDexApplication {
    private static Context context;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    public static Context getContext(){
        return context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().name("OfflineData").build();
        Realm.setDefaultConfiguration(configuration);

        context = getApplicationContext();
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
    }
}
