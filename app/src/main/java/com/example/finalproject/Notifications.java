package com.example.finalproject;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;

public class Notifications {

    private static final String CHANNEL_ID = "channel0";
    private NotificationManager notificationManager;
    String cName = "firstChannel";
    Context context;

    public Notifications (Context context) {
        this.context = context;
    }

    protected void createNotificationChannel(Class certainClass) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, cName, importance);
            String description = "Final Project";
            channel.setDescription(description);
            notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent contentIntent = new Intent(context, certainClass);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(context, 0, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle("Final Project")
                .setContentText("Don't forget about me!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(contentPendingIntent);
        int notificationId = 0;
        notificationManager.notify(notificationId, builder.build());
    }

    protected void createNotificationChannel(Class certainClass, String name, String picture, String username, String email, String phone, String website) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, cName, importance);
            String description = "Final Project";
            channel.setDescription(description);
            notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent contentIntent = new Intent(context, certainClass);
        contentIntent.putExtra("Name", name);
        contentIntent.putExtra("Picture", picture);
        contentIntent.putExtra("Username", username);
        contentIntent.putExtra("Email", email);
        contentIntent.putExtra("Phone", phone);
        contentIntent.putExtra("Website", website);

        PendingIntent contentPendingIntent = PendingIntent.getActivity(context, 0, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle("Notification")
                .setContentText("Don't forget about me!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(contentPendingIntent);
        int notificationId = 0;
        notificationManager.notify(notificationId, builder.build());
    }

    public void onDestroy() {
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("isDestroyedCalled", true);
        editor.apply();
    }
}
