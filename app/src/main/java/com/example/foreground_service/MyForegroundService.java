package com.example.foreground_service;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
public class MyForegroundService extends Service {

    public static final String TAG = "MyTag";
    private MediaPlayer mediaPlayer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showNotification();
        
        mediaPlayer = MediaPlayer.create( getApplicationContext(), Settings.System.DEFAULT_NOTIFICATION_URI );
        mediaPlayer.setLooping( true );
        mediaPlayer.start();
        
        Thread thread = new Thread( new Runnable() {
            @Override
            public void run() {
                Log.d( TAG, "start loop: " );
                for (int i = 0; i < 10; i++) {
                    Log.d( TAG, "loop : " + i );
                    try {
                        Thread.sleep( 1000 );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.d( TAG, "finish loop: " );
                stopForeground( true );
                stopSelf();
            }
        } );
        thread.start();
        return START_STICKY;
    }

    private void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, "channelId" );
        builder.setSmallIcon( R.mipmap.ic_launcher )
                .setContentText( "This is ForeGround Service" )
                .setContentTitle( "Music is playing" );
        Notification notification = builder.build();
        startForeground( 123, notification );
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        Log.d( TAG, "onDestroy: " );
    }
}
