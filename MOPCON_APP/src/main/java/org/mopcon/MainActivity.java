package org.mopcon;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import org.mopcon.fragment.FragmentActivity;
import org.mopcon.services.HttpService;

/**
 * Created by chuck on 13/10/3.
 */
public class MainActivity extends Activity {
  public static final  int TO_FRAGMENT = 0x1;
  private Handler handler;
  private HttpService mService = null;
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTheme(R.style.Theme_Sherlock_Light_NoActionBar);
    setContentView(R.layout.activity_main);

    handler = new Handler(){
      @Override
      public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(msg.what == TO_FRAGMENT){
          Intent intent = new Intent(MainActivity.this, FragmentActivity.class);
          startActivity(intent);
          unbindService(conn);
          MainActivity.this.finish();
        }
      }
    };

    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        Intent intent = new Intent(MainActivity.this, HttpService.class);
        startService(intent);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
      }
    }, 1000);
  }

  private ServiceConnection conn = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      mService = ((HttpService.ServiceBinder)service).getService();
      Message msg = new Message();
      msg.what = TO_FRAGMENT;
      mService.updateSession();
      mService.updateNews();

      if(mService.isExists()){
        handler.sendMessage(msg);
        return;
      }

      Toast.makeText(MainActivity.this,"Not network.", Toast.LENGTH_LONG).show();
      mService.stopService(new Intent(MainActivity.this, HttpService.class));
      MainActivity.this.finish();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
  };
}