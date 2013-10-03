package org.mopcon.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.mopcon.R;
import org.mopcon.model.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by chuck on 13/9/18.
 */

public class FragmentSessionPager extends Fragment{

  private ViewPager viewPager;
  private ImageButton previousButton,nextButton;
  private TextView textView;

  private static final String[] sessionData = {"2013年10月26日 星期六","2013年10月27日 星期日"};
  public static ArrayList<Integer>[] keyList ;
  private static final int fragmentPagerNum = 2;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    keyList = new ArrayList[2];
    for(int j = 0;j < keyList.length;j++)
      keyList[j] = new ArrayList<Integer>();
    HashMap<Integer,Session> hashMap = FragmentActivity.hashMapSession;
    Set<Integer> index = hashMap.keySet();
    Object[] obj = index.toArray();
    for (int i = 0;i < obj.length;i++){
      Integer integer = (Integer)obj[i];
      int tmp = integer.intValue();
      if((tmp / 100) == 1){
        keyList[0].add((Integer)obj[i]);
      }else if((tmp / 100) == 2){
        keyList[1].add((Integer)obj[i]);
      }
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.viewpager_session,container,false);
    viewPager = (ViewPager) view.findViewById(R.id.pager);
    SessionFragmentPagerAdapter sessionFragmentPagerAdapter = new SessionFragmentPagerAdapter(
        getChildFragmentManager());
    viewPager.setAdapter(sessionFragmentPagerAdapter);
    textView = (TextView) view.findViewById(R.id.session_data);
    textView.setText(sessionData[viewPager.getCurrentItem()]);
    previousButton = (ImageButton) view.findViewById(R.id.previousButton);
    previousButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        viewPager.setCurrentItem(0);
      }
    });
    nextButton = (ImageButton) view.findViewById(R.id.nextButton);
    nextButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        viewPager.setCurrentItem(1);
      }
    });

    viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int i, float v, int i2) {
      }

      @Override
      public void onPageSelected(int i) {
        textView.setText(sessionData[i]);
      }

      @Override
      public void onPageScrollStateChanged(int i) {
      }
    });
    return view;
  }

  public static class SessionFragmentPagerAdapter extends FragmentPagerAdapter{
    public SessionFragmentPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int i) {
      return FragmentSession.newInstance(i);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      return super.instantiateItem(container, position);
    }

    @Override
    public int getCount() {
      return fragmentPagerNum;
    }
  }
}