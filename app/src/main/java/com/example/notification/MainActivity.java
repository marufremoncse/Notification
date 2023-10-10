package com.example.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    EditText cookies;
    Button buy_btn;
    String CHANNEL_ID = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        myRegistrationToken();
        cookies = findViewById(R.id.cookies);
        buy_btn = findViewById(R.id.buy_btn);

        buy_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String noOfCookies = cookies.getText().toString();

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_chat)
                        .setContentTitle("Cookies")
                        .setContentText("You just bought " + noOfCookies + " cookies")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat
                        .from(MainActivity.this);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                notificationManagerCompat.notify(1, builder.build());
            }
        });
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

    void myRegistrationToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("FCM Token", "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get the token
                    String token = task.getResult();

                    // Use the token as needed (e.g., save to a server, send notifications)
                    Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    Log.d("FCM Token", token);
                });
    }
}