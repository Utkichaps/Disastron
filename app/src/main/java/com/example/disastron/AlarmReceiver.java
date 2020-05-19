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
        //Or if needed we can retrieve state data here as well
        con = context;
        createNotificationChannel();
        builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Alert!")
                //.setContentText("There is a storm brewing!")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Your coordinates:"+Double.toString(lat)+","+Double.toString(lon)))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
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

