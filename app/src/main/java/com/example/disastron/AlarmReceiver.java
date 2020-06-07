package com.example.disastron;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.disastron.AlertsReceiver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AlarmReceiver extends BroadcastReceiver {
    String CHANNEL_ID = "DisastronAlert";
    NotificationCompat.Builder builder;
    Context con;

    @Override
    public void onReceive(Context context, Intent intent) {
        double lat = intent.getDoubleExtra("Latitude",0);
        double lon = intent.getDoubleExtra("Longitude",0);
        Log.i("Coordinate",Double.toString(lat));
        Log.i("Coordinate",Double.toString(lon));

        AlertsReceiver ar = new AlertsReceiver(lat,lon);
        try {
            ar.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList<String> Alerts = new ArrayList<String>();
        Alerts = ar.DailyAlerts;
        String today = "", tomorrow;
        String state = ar.finalstate;
        Log.d("TESTSTATE",state);
        for(String alert: Alerts) {
            Log.d("ShowAlerts",alert);
            String[] sent = alert.split(":");
            if(sent[0].equals("today")) {
                String[] data_str = sent[1].split(";");
                for(String data: data_str) {
                    if(data.contains(state)) {
                        today += data;
                    }
                }
            }
        }
        Log.d("TESTTODAY",today);
        //Or if needed we can retrieve state data here as well
        con = context;
        createNotificationChannel();
        if(today.equals("")) {
            builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle("Alert!")
                    //.setContentText("There is a storm brewing!")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("No warnings today!"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
        else {
            builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle("Alert!")
                    //.setContentText("There is a storm brewing!")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("Warnings today: "+today))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Location_Disaster_Alert";
            String description = "This notification contains disaster alerts";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = con.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

