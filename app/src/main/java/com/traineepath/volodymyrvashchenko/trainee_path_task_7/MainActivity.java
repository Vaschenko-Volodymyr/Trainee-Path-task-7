package com.traineepath.volodymyrvashchenko.trainee_path_task_7;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.app.Notification.DEFAULT_LIGHTS;
import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int NOTIFICATION_ID = MainActivity.class.hashCode();
    private static final String NOTIFICATION_TEXT = "notify";

    private static int sNumber = 0;

    private static int sLEDBlinkingTime = 100;
    private static int sLEDPauseTime = 2000;
    private static int sLEDColor = createColor(255, 95, 169, 88);

    private static int createColor(int A, int R, int G, int B) {
        Log.v(TAG, ">> Method: createColor()");
        if (A <= 255 && R <= 255 && G <= 255 && B <= 255) {
            Log.v(TAG, "<< Method: createColor()");
            return A << 24 + R << 16 + G << 8 + B;
        } else {
            Log.v(TAG, "<< Method: createColor()");
            return 0;
        }
    }

    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, ">> Method: onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Button showNotification = (Button) findViewById(R.id.simple_notification_button);
        setShowNotificationOnClickListener(showNotification);

        Button clearNotification = (Button) findViewById(R.id.clear_notification);
        setClearNotificationOnClickListener(clearNotification);

        handleReceivedIntent();

        Log.v(TAG, "<< Method: onCreate()");
    }

    private void setShowNotificationOnClickListener(Button notify) {
        Log.v(TAG, ">> Method: setShowNotificationOnClickListener()");
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNotificationManager.notify(NOTIFICATION_ID, initNotification().build());
            }
        });
        Log.v(TAG, "<< Method: setShowNotificationOnClickListener()");
    }

    private void setClearNotificationOnClickListener(Button clearNotification) {
        Log.v(TAG, ">> Method: setClearNotificationOnClickListener()");
        clearNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNotificationManager.cancel(NOTIFICATION_ID);
            }
        });
        Log.v(TAG, "<< Method: setClearNotificationOnClickListener()");
    }

    private void handleReceivedIntent() {
        String received = getIntent().getStringExtra(NOTIFICATION_TEXT);
        if (received != null) {
            Toast.makeText(this, received, Toast.LENGTH_SHORT).show();
        }
    }

    private NotificationCompat.Builder initNotification() {
        Log.v(TAG, ">> Method: initNotification()");
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(MainActivity.this)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle(getString(R.string.notification_title))
                        .setContentText(getString(R.string.notification_text))
                        .setAutoCancel(true)
                        .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE | DEFAULT_LIGHTS)
                        .setSound(Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "6"))
                        .setNumber(++sNumber)
                        .setLights(sLEDColor, sLEDBlinkingTime, sLEDPauseTime);

        Intent resultIntent = new Intent(MainActivity.this, MainActivity.class);
        resultIntent.putExtra(NOTIFICATION_TEXT, getString(R.string.notification_received_text));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(MainActivity.this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);

        builder.setContentIntent(resultPendingIntent);
        Log.v(TAG, "<< Method: initNotification()");
        return builder;
    }
}
