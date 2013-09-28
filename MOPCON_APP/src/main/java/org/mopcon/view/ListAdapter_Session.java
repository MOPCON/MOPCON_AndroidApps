package org.mopcon.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.mopcon.MainActivity;
import org.mopcon.R;
import org.mopcon.model.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by chuck on 13/9/24.
 */
public class ListAdapter_Session extends ArrayAdapter<Session>{

  private HashMap<Integer,Session> hashMap;
  private ArrayList<Integer> keyList;
  private ArrayList<View> viewList;
  private LayoutInflater inflator;

  public ListAdapter_Session(Context context,int textViewResourceId,ArrayList<Integer> keyList){
    super(context,textViewResourceId);
    inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.keyList = keyList;
    hashMap = MainActivity.hashMapSession;
    viewList = new ArrayList<View>();
    for(int i = 0 ; i < keyList.size();i++){
      viewList.add(getView(hashMap.get(keyList.get(i))));
    }
  }

  @Override
  public int getCount() {
    return viewList.size();
  }

  private View getClassTag(int index){
    TextView textTag = new TextView(this.getContext());
    textTag.setText("Test");
    return textTag;
  }

  public Session getSession(int position){
    View view = viewList.get(position);
    return (Session)view.getTag();
  }

  private View getView(Session session){
    View convertView = inflator.inflate(R.layout.list_item_session_row, null);
    TextView titleView = (TextView) convertView.findViewById(R.id.title);
    TextView textView = (TextView) convertView.findViewById(R.id.text);
    ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
    convertView.setTag(session);

    titleView.setText(session.name);
    textView.setText(session.loc);
    char c = session.catalog.charAt(0);
    switch (c){
      case '1':
        imageView.setImageDrawable(getContext().getResources()
            .getDrawable(R.drawable.ic_01));
        break;
      case '2':
        imageView.setImageDrawable(getContext().getResources()
            .getDrawable(R.drawable.ic_02));
        break;
      case '3':
        imageView.setImageDrawable(getContext().getResources()
            .getDrawable(R.drawable.ic_03));
        break;
      case '4':
        imageView.setImageDrawable(getContext().getResources()
            .getDrawable(R.drawable.ic_04));
        break;
      case '5':
        imageView.setImageDrawable(getContext().getResources()
            .getDrawable(R.drawable.ic_05));
        break;
      case '6':
        imageView.setImageDrawable(getContext().getResources()
            .getDrawable(R.drawable.ic_06));
        break;
      case '7':
        imageView.setImageDrawable(getContext().getResources()
            .getDrawable(R.drawable.ic_07));
        break;
      case '8':
        imageView.setImageDrawable(getContext().getResources()
            .getDrawable(R.drawable.ic_08));
        break;
      case '9':
        imageView.setImageDrawable(getContext().getResources()
            .getDrawable(R.drawable.ic_09));
        break;
    }
    return convertView;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    convertView = viewList.get(position);
    return convertView;
  }
}
