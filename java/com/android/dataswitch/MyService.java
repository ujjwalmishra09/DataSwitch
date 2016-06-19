package com.android.dataswitch;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
    private DataSwitch ds;
    @Override
    public void onCreate() {
        super.onCreate();
        ds = new DataSwitch(2500,(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
        if(ds.socketOpened)
        ds.start();


    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle("DataSwitch").build();

            startForeground(1, notification);

        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        if(ds.isAlive())
        {
         stopServerThread sst=new stopServerThread();
            sst.start();
        }
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
