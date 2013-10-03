package org.mopcon.fragment;

import android.content.ComponentName;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.TabHost;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import org.mopcon.R;
import org.mopcon.model.News;
import org.mopcon.model.Session;
import org.mopcon.services.HttpService;
import org.mopcon.services.ServiceImp;

import java.util.HashMap;

public class FragmentActivity extends SherlockFragmentActivity {

  public static final int TAB_UI_INIT = 0x1;

  private ServiceImp mService;

  private TabHost tabHost;
  private TabManager tabManager;
  public static HashMap<Integer,Session> hashMapSession;
  public static HashMap<Integer,News> hashMapNews;

  private Handler handler = new Handler(){
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      switch(msg.what){
        case TAB_UI_INIT:
          tabHost.clearAllTabs();
          tabManager.addTab(tabHost.newTabSpec("Tab1").setIndicator("",getResources().getDrawable(R.drawable.tab_ic_even)),
              FragmentSessionPager.class,null);
          tabManager.addTab(tabHost.newTabSpec("Tab2").setIndicator("",getResources().getDrawable(R.drawable.tab_ic_news)),
              FragmentNews.class,null);
          tabManager.addTab(tabHost.newTabSpec("Tab3").setIndicator("",getResources().getDrawable(R.drawable.tab_ic_maps)),
              FragmentGMap.class,null);
          break;
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setTheme(R.style.Theme_Sherlock_Light);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_fragment_activity);

    Intent intent = new Intent(this,HttpService.class);
    bindService(intent, conn, Context.BIND_AUTO_CREATE);

    tabHost = (TabHost) findViewById(android.R.id.tabhost);
    tabHost.setup();

    tabManager = new TabManager(this,tabHost,R.id.realtabcontent);
    if(savedInstanceState != null)
      tabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putString("tab",tabHost.getCurrentTabTag());
  }

  @Override
  protected void onDestroy() {
    unbindService(conn);
    super.onDestroy();
  }

  public ServiceConnection conn = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      mService = ((HttpService.ServiceBinder)service).getService();
      hashMapSession = mService.getSession();
      hashMapNews = mService.getNews();
      if(hashMapSession != null && hashMapNews != null){
        Message msg = new Message();
        msg.what = TAB_UI_INIT;
        handler.sendMessage(msg);
      }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
  };
  public static class TabManager implements TabHost.OnTabChangeListener {
    private final android.support.v4.app.FragmentActivity mActivity;
    private final TabHost mTabHost;
    private final int mContainerId;
    private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
    TabInfo mLastTab;

    static final class TabInfo {
      private final String tag;
      private final Class<?> clss;
      private final Bundle args;
      private Fragment fragment;
      TabInfo(String _tag, Class<?> _class, Bundle _args) {
        tag = _tag;
        clss = _class;
        args = _args;
      }
    }

    static class DummyTabFactory implements TabHost.TabContentFactory {
      private final Context mContext;

      public DummyTabFactory(Context context) {
        mContext = context;
      }

      @Override
      public View createTabContent(String tag) {
        View v = new View(mContext);
        v.setMinimumWidth(0);
        v.setMinimumHeight(0);
        return v;
      }
    }

    public TabManager(android.support.v4.app.FragmentActivity activity, TabHost tabHost, int containerId) {
      mActivity = activity;
      mTabHost = tabHost;
      mContainerId = containerId;
      mTabHost.setOnTabChangedListener(this);
    }

    public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
      tabSpec.setContent(new DummyTabFactory(mActivity));
      String tag = tabSpec.getTag();

      TabInfo info = new TabInfo(tag, clss, args);

      info.fragment = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
      if (info.fragment != null && !info.fragment.isDetached()) {
        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
        ft.detach(info.fragment);
        ft.commit();
      }

      mTabs.put(tag, info);
      mTabHost.addTab(tabSpec);
    }

    @Override
    public void onTabChanged(String tabId) {
      TabInfo newTab = mTabs.get(tabId);
      if (mLastTab != newTab) {
        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
        if (mLastTab != null) {
          if (mLastTab.fragment != null) {
            ft.detach(mLastTab.fragment);
          }
        }
        if (newTab != null) {
          if (newTab.fragment == null) {
            newTab.fragment = Fragment.instantiate(mActivity,
                newTab.clss.getName(), newTab.args);
            ft.add(mContainerId, newTab.fragment, newTab.tag);
          } else {
            ft.attach(newTab.fragment);
          }
        }

        mLastTab = newTab;
        ft.commit();
        mActivity.getSupportFragmentManager().executePendingTransactions();
      }
    }
  }
}
