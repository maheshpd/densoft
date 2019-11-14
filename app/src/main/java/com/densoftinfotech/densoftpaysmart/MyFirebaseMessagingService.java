package com.densoftinfotech.densoftpaysmart;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import com.densoftinfotech.densoftpaysmart.sqlitedatabase.DatabaseHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessaging";
    Bitmap bitmap = null;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        updateTokenToFirebase(s);

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage);
        }

    }

    private void sendNotification(RemoteMessage remoteMessage) {

        Log.d("recvd data ", remoteMessage.getNotification().getTitle() + " " + remoteMessage.getNotification().getBody() + " " + remoteMessage.getNotification().getImageUrl() + " ");
        String title = remoteMessage.getNotification().getTitle();
        String content = remoteMessage.getNotification().getBody();
        String bigpicture = String.valueOf(remoteMessage.getNotification().getImageUrl());


        if (remoteMessage.getNotification().getImageUrl() != null) {
            bitmap = getBitmapfromUrl(bigpicture);
        }

        DatabaseHelper.getInstance(this).savenotificationData(title, content, bigpicture);

        Intent activityIntent = new Intent(this, NotificationActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, activityIntent, 0);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "Paysmart";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Paysmart", NotificationManager.IMPORTANCE_DEFAULT);

            //Configure the notification channel
            notificationChannel.setName(title);
            notificationChannel.setDescription(content);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder
                .setContentTitle(title)
                .setLargeIcon(bitmap)/*Notification icon image*/
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(content)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                .setAutoCancel(true)
                .setTicker("Hearty365")
                .setContentIntent(contentIntent);

        notificationManager.notify(1, notificationBuilder.build());
    }

    //update notification
    private void updateTokenToFirebase(String s) {
        //Log.d("string token ", s) ;
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

}
