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
import java.util.Set;

/**
 * Created by chuck on 13/9/24.
 */
public class ListAdapter_Session extends ArrayAdapter<Session>{

  private HashMap<Integer,Session> hashMap;
  private LayoutInflater inflator;
  private Object[] keyObj = null;

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
}
