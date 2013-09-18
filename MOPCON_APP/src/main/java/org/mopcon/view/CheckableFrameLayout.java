package org.mopcon.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.FrameLayout;

/**
 * Created by chuck on 13/9/18.
 */
public class CheckableFrameLayout extends FrameLayout implements Checkable{
  private boolean checked;
  public CheckableFrameLayout(Context context) {
    super(context);
  }

  public CheckableFrameLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  @Override
  public void setChecked(boolean b) {
    checked = b;
    setBackground(b ? new ColorDrawable(0xff0000a0) : null);
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
