package org.mopcon.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Binder;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.mopcon.model.News;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by chuck on 13/8/29.
 */
public class HttpService extends Service {
    private final IBinder mBinder = new ServiceBinder();

    public class ServiceBinder extends Binder{
        public HttpService getService(){
            return HttpService.this;
        }
    }

    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this,"Service : onCreate",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"Service : onDestroy",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Toast.makeText(this,"Service : onRebind",Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(this,"Service : onUnbind",Toast.LENGTH_LONG).show();
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"Service : onStartCommand",Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }

    private String connect(String url){
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response ;
        InputStream stream = null;
        try {
            response = httpClient.execute(httpGet);
            if(response == null)
                return null;
            HttpEntity entity = response.getEntity();
            if(entity == null)
                return null;
            stream = entity.getContent();
            String result = null;
            StringBuffer sb = new StringBuffer();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(stream));
            while((result = reader.readLine()) != null){
               sb.append(result).append("\n");
            }

            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
