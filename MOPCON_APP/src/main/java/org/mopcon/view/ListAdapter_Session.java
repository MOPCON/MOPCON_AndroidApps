package org.mopcon.view;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.mopcon.fragment.FragmentActivity;
import org.mopcon.R;
import org.mopcon.model.Session;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chuck on 13/9/24.
 */
public class ListAdapter_Session extends ArrayAdapter<Session>{

  private HashMap<Integer,Session> hashMap;
  private ArrayList<Integer> keyList;
  private ArrayList<View> viewList;
  private LayoutInflater inflator;
  private String[][] section = {
      {"上午9:05~9:25",
          "上午9:25~10:15",
          "上午10:30~11:10",
          "上午11:20~12:00",
          "下午13:10~14:00",
          "下午14:10~14:50",
          "下午15:10~15:50",
          "下午16:00~16:40"},
      {"上午9:00~9:50",
          "上午10:00~10:40",
          "上午10:50~11:30",
          "上午11:30~12:20",
          "下午13:30~14:20",
          "下午14:30~15:10",
          "下午15:30~16:10",
          "下午16:20~17:30"}
  };

  public ListAdapter_Session(Context context,int textViewResourceId,ArrayList<Integer> keyList){
    super(context,textViewResourceId);
    inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.keyList = keyList;
    hashMap = FragmentActivity.hashMapSession;
    viewList = new ArrayList<View>();
    int day = keyList.get(0) / 100;
    for(int j = 0;j < section[day - 1].length;j++){
      viewList.add(getClassTag(section[day - 1][j]));
      for(int k = 0;k < keyList.size() ;k++){
        int index = keyList.get(k).intValue() % 100;
        if(j == (index / 10)){
          viewList.add(getView(hashMap.get(keyList.get(k))));
        }
      }
    }
  }

  @Override
  public int getCount() {
    return viewList.size();
  }

  private View getClassTag(String str){
    TextView textTag = new TextView(this.getContext());
    textTag.setText(str);
    textTag.setClickable(true);
    textTag.setBackgroundColor(0xffb1b2b2);


    textTag.setVisibility(View.VISIBLE);
    textTag.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
    textTag.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
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
