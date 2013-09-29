package org.mopcon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

/**
 * Created by chuck on 13/9/18.
 */
public class CheckableFrameLayout extends LinearLayout implements Checkable{
  private boolean checked;
  public CheckableFrameLayout(Context context) {
    super(context);
  }

  public CheckableFrameLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public void setChecked(boolean b) {
    checked = b;
    setBackgroundColor(b ? 0xffffffee : 0x00000000);
  }

  @Override
  public boolean isChecked() {
    return false;
  }

  @Override
  public void toggle() {
    setChecked(!checked);
  }
}
