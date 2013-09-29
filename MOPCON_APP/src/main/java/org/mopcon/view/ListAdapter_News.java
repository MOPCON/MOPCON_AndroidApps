package org.mopcon.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.mopcon.R;
import org.mopcon.model.News;

import java.util.HashMap;

/**
 * Created by chuck on 13/9/29.
 */
public class ListAdapter_News extends ArrayAdapter<News>{

  private HashMap<Integer,News> hashMap;
  private Object[] objects;
  private LayoutInflater inflator;

  public ListAdapter_News(Context context, int textViewResourceId) {
    super(context, textViewResourceId);
  }

  public ListAdapter_News(Context context, int textViewResourceId ,HashMap<Integer,News> map){
    this(context, textViewResourceId);
    inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    hashMap = map;
    objects = map.keySet().toArray();
  }

  public News getNews(int position){
    return hashMap.get(objects[position]);
  }

  @Override
  public int getCount() {
    return hashMap.size();
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    convertView = inflator.inflate(R.layout.list_item_news_row,null);
    News news = getNews(position);
    TextView title = (TextView) convertView.findViewById(R.id.title);
    title.setText(news.title);
    TextView text = (TextView) convertView.findViewById(R.id.text);
    text.setText(news.publisher);
    return convertView;
  }
}
