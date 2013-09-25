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
import android.widget.*;

import org.mopcon.MainActivity;
import org.mopcon.R;
import org.mopcon.model.Session;
import org.mopcon.view.ListAdapter_Session;

/**
 * Created by chuck on 13/9/18.
 */
public class FragmentSessionActivity extends Fragment {

  private static ListView listView;
  private boolean mDualPane;
  private int mCurCheckPosition = 0;
  private static ListAdapter_Session listAdapterSession = null;

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
        showDetails(i);
        System.out.println("Item index = " + i);
      }
    });

    listAdapterSession = new ListAdapter_Session(getActivity(),
        R.layout.list_item_session_row, MainActivity.hashMapSession);

    listView.setAdapter(listAdapterSession);

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
      setTheme(R.style.Theme_Sherlock_Light); //Used for theme switching in samples
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

      View view = inflater.inflate(R.layout.session_content,container,false);
      int padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
          4, getActivity().getResources().getDisplayMetrics());
      view.setPadding(padding,padding,padding,padding);
      Session session = FragmentSessionActivity.listAdapterSession.getSession(getShownIndex());
      TextView name = (TextView) view.findViewById(R.id.session_name);
      name.setText(session.name);
      TextView speaker = (TextView) view.findViewById(R.id.session_speaker);
      speaker.setText(session.speaker);
      TextView content = (TextView) view.findViewById(R.id.session_content);
      content.setText(session.content);
      TextView speaker_bio = (TextView) view.findViewById(R.id.session_speaker_bio);
      speaker_bio.setText(session.speaker_bio);
      return view;
    }
  }
}