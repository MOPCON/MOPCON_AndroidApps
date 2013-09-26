package org.mopcon.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.mopcon.R;

/**
 * Created by chuck on 13/9/18.
 */

public class FragmentSessionPager extends Fragment{

  private ViewPager viewPager;
  private ImageButton previousButton,nextButton;
  private TextView textView;

  private static final String[] sessionData = {"2013/10/26","2013/10/27"};

  private static final int fragmentPagerNum = 2;

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  private SessionFragmentPagerAdapter sessionFragmentPagerAdapter;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.viewpager_session,container,false);
    viewPager = (ViewPager) view.findViewById(R.id.pager);
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

    sessionFragmentPagerAdapter = new SessionFragmentPagerAdapter(
        getChildFragmentManager());
    viewPager.setAdapter(sessionFragmentPagerAdapter);
    return view;
  }

  public class SessionFragmentPagerAdapter extends FragmentPagerAdapter{
    public SessionFragmentPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int i) {
      return FragmentSession.create(i);
    }

    @Override
    public int getCount() {
      return fragmentPagerNum;
    }
  }
}