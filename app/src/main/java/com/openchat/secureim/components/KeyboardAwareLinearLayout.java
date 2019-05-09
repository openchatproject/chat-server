package com.openchat.secureim.components;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.openchat.secureim.R;

import java.lang.reflect.Field;

public class KeyboardAwareLinearLayout extends LinearLayout {
  private static final String TAG  = KeyboardAwareLinearLayout.class.getSimpleName();
  private static final Rect   rect = new Rect();

  public KeyboardAwareLinearLayout(Context context) {
    super(context);
  }

  public KeyboardAwareLinearLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB)
  public KeyboardAwareLinearLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int res = getResources().getIdentifier("status_bar_height", "dimen", "android");
    int statusBarHeight = res > 0 ? getResources().getDimensionPixelSize(res) : 0;

    final int availableHeight = this.getRootView().getHeight() - statusBarHeight - getViewInset();
    getWindowVisibleDisplayFrame(rect);

    final int keyboardHeight = availableHeight - (rect.bottom - rect.top);

    if (keyboardHeight > getResources().getDimensionPixelSize(R.dimen.min_emoji_drawer_height)) {
      onKeyboardShown(keyboardHeight);
    }

    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  public int getViewInset() {
    if (Build.VERSION.SDK_INT < VERSION_CODES.LOLLIPOP) {
      return 0;
    }

    try {
      Field attachInfoField = View.class.getDeclaredField("mAttachInfo");
      attachInfoField.setAccessible(true);
      Object attachInfo = attachInfoField.get(this);
      if (attachInfo != null) {
        Field stableInsetsField = attachInfo.getClass().getDeclaredField("mStableInsets");
        stableInsetsField.setAccessible(true);
        Rect insets = (Rect)stableInsetsField.get(attachInfo);
        return insets.bottom;
      }
    } catch (NoSuchFieldException nsfe) {
      Log.w(TAG, "field reflection error when measuring view inset", nsfe);
    } catch (IllegalAccessException iae) {
      Log.w(TAG, "access reflection error when measuring view inset", iae);
    }
    return 0;
  }

  protected void onKeyboardShown(int keyboardHeight) {
    Log.w(TAG, "keyboard shown, height " + keyboardHeight);

    WindowManager wm = (WindowManager) getContext().getSystemService(Activity.WINDOW_SERVICE);
    if (wm == null || wm.getDefaultDisplay() == null) {
      return;
    }
    int rotation = wm.getDefaultDisplay().getRotation();

    switch (rotation) {
      case Surface.ROTATION_270:
      case Surface.ROTATION_90:
        setKeyboardLandscapeHeight(keyboardHeight);
        break;
      case Surface.ROTATION_0:
      case Surface.ROTATION_180:
        setKeyboardPortraitHeight(keyboardHeight);
    }
  }

  public int getKeyboardHeight() {
    WindowManager      wm    = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
    if (wm == null || wm.getDefaultDisplay() == null) {
      throw new AssertionError("WindowManager was null or there is no default display");
    }

    int rotation = wm.getDefaultDisplay().getRotation();

    switch (rotation) {
      case Surface.ROTATION_270:
      case Surface.ROTATION_90:
        return getKeyboardLandscapeHeight();
      case Surface.ROTATION_0:
      case Surface.ROTATION_180:
      default:
        return getKeyboardPortraitHeight();
    }
  }

  private int getKeyboardLandscapeHeight() {
    return PreferenceManager.getDefaultSharedPreferences(getContext())
                            .getInt("keyboard_height_landscape",
                                    getResources().getDimensionPixelSize(R.dimen.min_emoji_drawer_height));
  }

  private int getKeyboardPortraitHeight() {
    return PreferenceManager.getDefaultSharedPreferences(getContext())
                            .getInt("keyboard_height_portrait",
                                    getResources().getDimensionPixelSize(R.dimen.min_emoji_drawer_height));
  }

  private void setKeyboardLandscapeHeight(int height) {
    PreferenceManager.getDefaultSharedPreferences(getContext())
                     .edit().putInt("keyboard_height_landscape", height).apply();
  }

  private void setKeyboardPortraitHeight(int height) {
    PreferenceManager.getDefaultSharedPreferences(getContext())
                     .edit().putInt("keyboard_height_portrait", height).apply();
  }

}
