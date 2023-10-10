package com.example.myservice;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.notification.MainActivity;
import com.example.notification.R;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String CHANNEL_ID = "2";
    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        if(message.getNotification()!=null){
            createNotificationChannel();
            showNotification(message.getNotification().getTitle(),message.getNotification().getBody());
        }
    }
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    public void showNotification(String title, String text){
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_chat)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat
                .from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManagerCompat.notify(1, builder.build());
    }

    public void createNotificationChannel(){
        //Create Notification channel only on API Level 26+
        //NotificationChannel is a new Class and not in a support library
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String name = "My Channel Name";
            String description = "My Channel description";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setVibrationPattern(new long[]{ 0, 250, 250, 250});
            //Register the channel with the system.
            //You cannot change importance or other notification behaviors after this
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }


}