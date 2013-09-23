package org.mopcon.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import org.mopcon.R;

/**
 * Created by chuck on 13/9/18.
 */
public class FragmentSessionActivity extends Fragment {

  private static ListView listView;
  private boolean mDualPane;
  private int mCurCheckPosition = 0;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_session,container,false);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt("curChoice", mCurCheckPosition);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    listView = (ListView) getActivity().findViewById(R.id.titles);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        System.out.println("Test 456");
        showDetails(i);
      }
    });

    System.out.println("Test 123");

    String[] titleArray = {"test1","test2","test3"};

    listView.setAdapter(new ArrayAdapter<String>(getActivity(),
        R.layout.simple_list_item_checkable_1,
        android.R.id.text1, titleArray));

    View detailsFrame = getActivity().findViewById(R.id.details);
    mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

    if (savedInstanceState != null) {
      mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
    }

    if (mDualPane) {
      System.out.println("mDualPane = true");
      listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
      showDetails(mCurCheckPosition);
    }
  }


  void showDetails(int index) {
    mCurCheckPosition = index;
    if (mDualPane) {
      listView.setItemChecked(index, true);
      DetailsFragment details = (DetailsFragment)
          getFragmentManager().findFragmentById(R.id.details);
      if (details == null || details.getShownIndex() != index) {
        details = DetailsFragment.newInstance(index);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.details, details);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
      }
    } else {
      Intent intent = new Intent();
      intent.setClass(getActivity(), FragmentSessionActivity.DetailsActivity.class);
      intent.putExtra("index", index);
      startActivity(intent);
    }
  }


  public static class DetailsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      setTheme(R.style.Theme_Sherlock); //Used for theme switching in samples
      super.onCreate(savedInstanceState);

      if (getResources().getConfiguration().orientation
          == Configuration.ORIENTATION_LANDSCAPE) {
        finish();
        return;
      }

      if (savedInstanceState == null) {
        DetailsFragment details = new DetailsFragment();
        details.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(
            android.R.id.content, details).commit();
      }
    }
  }

  public static class DetailsFragment extends Fragment {
    public static DetailsFragment newInstance(int index) {
      DetailsFragment f = new DetailsFragment();

      Bundle args = new Bundle();
      args.putInt("index", index);
      f.setArguments(args);

      return f;
    }

    public int getShownIndex() {
      return getArguments().getInt("index", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      if (container == null) {
        return null;
      }

      ScrollView scroller = new ScrollView(getActivity());
      TextView text = new TextView(getActivity());
      int padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
          4, getActivity().getResources().getDisplayMetrics());
      text.setPadding(padding, padding, padding, padding);
      scroller.addView(text);
      text.setText("test");
      return scroller;
    }
  }
}