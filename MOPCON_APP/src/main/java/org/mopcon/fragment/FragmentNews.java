package org.mopcon.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.util.Linkify;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.mopcon.R;
import org.mopcon.model.News;
import org.mopcon.view.ListAdapter_News;

/**
 * Created by chuck on 13/9/26.
 */
public class FragmentNews extends Fragment {
  private static ListAdapter_News listAdapterNews;
  private ListView listView;
  private boolean mDualPane;
  private int mCurCheckPosition = 0;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.list_fragment,container,false);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt("news_curChoice", mCurCheckPosition);
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    listView = (ListView) getActivity().findViewById(R.id.titles);
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        showDetails(i);
      }
    });

    listAdapterNews = new ListAdapter_News(getActivity(),
        R.layout.list_item_news_row, FragmentActivity.hashMapNews);

    listView.setAdapter(listAdapterNews);

    View detailsFrame = getActivity().findViewById(R.id.details);
    mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

    if (savedInstanceState != null) {
      mCurCheckPosition = savedInstanceState.getInt("news_curChoice", 0);
    }

    if (mDualPane) {
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
      intent.setClass(getActivity(), DetailsActivity.class);
      intent.putExtra("news_index", index);
      startActivity(intent);
    }
  }

  public static class DetailsActivity extends android.support.v4.app.FragmentActivity {

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
    public static DetailsFragment newInstance(int index) {
      DetailsFragment f = new DetailsFragment();

      Bundle args = new Bundle();
      args.putInt("news_index", index);
      f.setArguments(args);

      return f;
    }

    public int getShownIndex() {
      return getArguments().getInt("news_index", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      if (container == null) {
        return null;
      }

      View view = inflater.inflate(R.layout.news_content,container,false);
      int padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
          4, getActivity().getResources().getDisplayMetrics());
      view.setPadding(padding,padding,padding,padding);
      News news = listAdapterNews.getNews(getShownIndex());
      TextView title = (TextView) view.findViewById(R.id.news_title);
      title.setText(news.title);
      TextView content = (TextView) view.findViewById(R.id.news_content);
      content.setText(news.content);
      Linkify.addLinks(content,Linkify.ALL);
      return view;
    }
  }
}

