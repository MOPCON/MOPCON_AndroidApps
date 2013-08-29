package org.mopcon.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Binder;

/**
 * Created by chuck on 13/8/29.
 */
public class HttpService extends Service {
    private final IBinder mBinder = new ServiceBinder();

    public class ServiceBinder extends Binder{
        HttpService getService(){
            return HttpService.this;
        }
    }

    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
