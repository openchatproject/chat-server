package com.openchat.secureim.util;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.view.View;

public class ViewUtil {
  public static void setBackgroundSavingPadding(View v, Drawable drawable) {
    final int paddingBottom = v.getPaddingBottom();
    final int paddingLeft = v.getPaddingLeft();
    final int paddingRight = v.getPaddingRight();
    final int paddingTop = v.getPaddingTop();
    v.setBackgroundDrawable(drawable);
    v.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
  }

  public static void setBackgroundSavingPadding(View v, @DrawableRes int resId) {
    final int paddingBottom = v.getPaddingBottom();
    final int paddingLeft = v.getPaddingLeft();
    final int paddingRight = v.getPaddingRight();
    final int paddingTop = v.getPaddingTop();
    v.setBackgroundResource(resId);
    v.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
  }
}
