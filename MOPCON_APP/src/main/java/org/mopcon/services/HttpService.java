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
import org.mopcon.model.Session;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by chuck on 13/8/29.
 */
public class HttpService extends Service implements ServiceImp{
  private static long sessionLastUpdate = 0;
  private static long newsLastUpdate = 0;
  private static final String NEWS = "NEWS";
  private static final String SESSION = "SESSION";
  private static final String JSON = ".json";
  private static final String PHP = ".php";
  private static final String apiUrl = "http://mopcon.org/2013/api/";

  private HashMap<Integer,News> newsMap = null;
  private HashMap<Integer,Session> sessionMap = null;

  private final IBinder mBinder = new ServiceBinder();

  @Override
  public boolean updateNews() {
    String news = connect(apiUrl + NEWS.toLowerCase() + PHP);
    if(news == null)
      return false;
    writeJSONFile(NEWS + JSON,news);
    newsMap = getNews();
    return true;
  }

  @Override
  public boolean updateSession() {
    String session = connect(apiUrl + SESSION.toLowerCase() + PHP);
    if(session == null)
      return false;
    writeJSONFile(SESSION + JSON,session);
    sessionMap = getSession();
    return true;
  }

  @Override
  public HashMap<Integer, News> getNews() {
    HashMap<Integer, News> map = new HashMap<Integer, News>();
    JSONArray jsonArray;
    JSONObject jsonObject;
    String str = readJSONFile(NEWS + JSON);
    try {
      System.out.println("News JSON = " + str);
      jsonObject = new JSONObject(str);
      newsLastUpdate = jsonObject.getLong("lastu_pdate");
      jsonArray = jsonObject.getJSONArray("news");
      for(int i = 0 ;i < jsonArray.length();i++){
        JSONObject jObject = jsonArray.getJSONObject(i);
        News news = new News();
        news.id = jObject.getInt(News.Key.id);
        news.title = jObject.getString(News.Key.title);
        news.content = jObject.getString(News.Key.content);
        news.publisher = jObject.getString(News.Key.publisher);
        news.pub_time = jObject.getLong(News.Key.pub_time);
        map.put(news.id,news);
      }
      return map;
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public HashMap<Integer, Session> getSession() {
    HashMap<Integer, Session> map = new HashMap<Integer, Session>();
    JSONObject jsonObject;
    JSONArray jsonArray;
    String str = readJSONFile(SESSION + JSON);
    try {
      System.out.println("Session JSON = " + str);
      jsonObject = new JSONObject(str);
      sessionLastUpdate = jsonObject.getLong("last_update");
      jsonArray = jsonObject.getJSONArray("sessions");
      for(int i = 0;i < jsonArray.length();i++){
        JSONObject jObject = jsonArray.getJSONObject(i);
        Session session = new Session();
        session.id = jObject.getInt(Session.Key.id);
        session.name = jObject.getString(Session.Key.name);
        session.content = jObject.getString(Session.Key.content);
        session.keyword = jObject.getString(Session.Key.keyword);
        session.speaker = jObject.getString(Session.Key.speaker);
        session.speaker_bio = jObject.getString(Session.Key.speaker_bio);
        session.loc = jObject.getString(Session.Key.loc);
        session.catalog = jObject.getString(Session.Key.catalog);
        session.start_time = jObject.getLong(Session.Key.start_time);
        session.end_time = jObject.getLong(Session.Key.end_time);
        map.put(session.id,session);
      }
      return map;
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public News getNews(int id) {
    return newsMap.get(id);
  }

  @Override
  public Session getSession(int id) {
    return sessionMap.get(id);
  }

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
    HashMap<Intent,String> map;
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
  private boolean updateAll(){
    int i = 0;
    if(!updateNews())
      i++;
    if(!updateSession())
      i++;
    return i == 0? true: false;
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
