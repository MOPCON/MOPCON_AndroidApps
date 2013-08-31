package org.mopcon.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Binder;
import android.os.StrictMode;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mopcon.model.News;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by chuck on 13/8/29.
 */
public class HttpService extends Service {
    private static final String JSON_NEWS = "NEWS.json";
    private static final String JSON_SESSION = "NEWS.json";

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
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        super.onCreate();
        Toast.makeText(this,"Service : onCreate",Toast.LENGTH_LONG).show();
        String str = connect("http://mopcon.org/2013/api/news.php");
        System.out.println(str);
        System.out.println("------------------------");
        jsonParse(str);
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
                if (stream != null)
                    stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private void jsonParse(String json){
        JSONArray jsonArray;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            jsonArray = jsonObject.getJSONArray("news");
            System.out.println("length = " + jsonArray.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean writeJSONFile(String fileName,String json) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(json.getBytes());
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private String readJSONFile(String fileName){
        FileInputStream fis = null;
        try {
            fis = openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer sb= new StringBuffer();
            String line = null;
            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
