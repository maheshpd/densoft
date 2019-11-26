package com.densoftinfotech.densoftpaysmart.background_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.densoftinfotech.densoftpaysmart.map.MapConstants;

public class BackgroundService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != intent && intent.getAction().equals("location_update")) {
            Location locationData = intent.getParcelableExtra(MapConstants.LOCATION_MESSAGE);
            Log.d("Location: ", "Latitude: " + locationData.getLatitude() + "Longitude:" + locationData.getLongitude());
            //send your call to api or do any things with the of location data
        }
    }
}
