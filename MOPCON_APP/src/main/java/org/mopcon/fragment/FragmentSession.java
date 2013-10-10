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
import android.widget.ListView;
import android.widget.TextView;

import org.mopcon.R;
import org.mopcon.model.Session;
import org.mopcon.view.ListAdapter_Session;

/**
 * Created by chuck on 13/9/26.
 */
public class FragmentSession extends Fragment {
  private ListView listView;
  private static ListAdapter_Session[] listAdapterSession = new ListAdapter_Session[2];
  private boolean mDualPane;
  private int mCurCheckPosition = 1;
  private int nowFragmentNum;

  public static Fragment newInstance(int position){
    FragmentSession fragmentSession = new FragmentSession();
    Bundle bundle = new Bundle();
    bundle.putInt("num",position);
    fragmentSession.setArguments(bundle);
    return fragmentSession;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    nowFragmentNum = getArguments() != null ? getArguments().getInt("num"):1;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.list_fragment,container,false);
    listView = (ListView) view.findViewById(R.id.titles);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        showDetails(i,nowFragmentNum);
      }
    });

    listAdapterSession[nowFragmentNum] = new ListAdapter_Session(getActivity(),
        R.layout.list_item_session_row, FragmentSessionPager.keyList[nowFragmentNum]);

    listView.setAdapter(listAdapterSession[nowFragmentNum]);

    View detailsFrame = view.findViewById(R.id.details);
    mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

    if (savedInstanceState != null) {
      mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
    }

    if (mDualPane) {
      listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
      showDetails(mCurCheckPosition,nowFragmentNum);
    }
    return view;
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt("curChoice", mCurCheckPosition);
  }

  void showDetails(int index,int day) {
    mCurCheckPosition = index;
    if (mDualPane) {
      listView.setItemChecked(index, true);
      DetailsFragment details = (DetailsFragment)
          getFragmentManager().findFragmentById(R.id.details);
      if (details == null || details.getShownIndex() != index) {
        details = DetailsFragment.newInstance(index,day);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.details, details);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
      }
    } else {
      Intent intent = new Intent();
      intent.setClass(getActivity(), DetailsActivity.class);
      intent.putExtra("index", index);
      intent.putExtra("day", day);
      startActivity(intent);
    }
  }

  public static class DetailsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      setTheme(R.style.Theme_Sherlock_Light); //Used for theme switching in samples
      super.onCreate(savedInstanceState);

      if((getResources().getConfiguration().screenLayout &
          Configuration.SCREENLAYOUT_SIZE_MASK) >=
          Configuration.SCREENLAYOUT_SIZE_LARGE &&
          getResources().getConfiguration().orientation ==
          Configuration.ORIENTATION_LANDSCAPE){
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
    public static DetailsFragment newInstance(int index,int day) {
      DetailsFragment f = new DetailsFragment();

      Bundle args = new Bundle();
      args.putInt("index", index);
      args.putInt("day",day);
      f.setArguments(args);

      return f;
    }

    public int getShownIndex() {
      return getArguments().getInt("index", 0);
    }

    public int getShownDay(){
      return getArguments().getInt("day",0);
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
      Session session = listAdapterSession[getShownDay()].getSession(getShownIndex());
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

