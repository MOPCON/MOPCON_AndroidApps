package org.mopcon.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.mopcon.R;
import org.mopcon.model.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by chuck on 13/9/24.
 */
public class ListAdapter_Session extends ArrayAdapter<Session>{

  private HashMap<Integer,Session> hashMap;
  private LayoutInflater inflator;
  private Object[] keyObj = null;

  public ListAdapter_Session(Context context, int textViewResourceId) {
    super(context, textViewResourceId);
  }

  public ListAdapter_Session(Context context,int textViewResourceId,HashMap<Integer, Session> map){
    super(context,textViewResourceId);
    inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    hashMap = map;
    Set<Integer> key = hashMap.keySet();
    keyObj = key.toArray();
  }

  public Session getSession(int position){
    int index = ((Integer)keyObj[position]).intValue();
    System.out.println("Index = " + index);
    return hashMap.get(keyObj[position]);
  }

  @Override
  public int getCount() {
    return hashMap.size();
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    convertView = inflator.inflate(R.layout.list_item_session_row, null);
    TextView titleView = (TextView) convertView.findViewById(R.id.title);
    TextView textView = (TextView) convertView.findViewById(R.id.text);
    ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
    Session session = getSession(position);
    convertView.setTag(session);

    titleView.setText(session.name);
    textView.setText(session.keyword);
    imageView.setImageDrawable(getContext().getResources()
        .getDrawable(R.drawable.ic_01));
    return convertView;
  }
}
