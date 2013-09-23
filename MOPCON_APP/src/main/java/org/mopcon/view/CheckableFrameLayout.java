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

  @Override
  public void setChecked(boolean b) {
    checked = b;
    setBackgroundColor(b ? 0xff0000a0 : 0x00000000);
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
