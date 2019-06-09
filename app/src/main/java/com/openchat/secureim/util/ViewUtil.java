package com.openchat.secureim.util;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

  public static void swapChildInPlace(ViewGroup parent, View toRemove, View toAdd, int defaultIndex) {
    int childIndex = parent.indexOfChild(toRemove);
    if (childIndex > -1) parent.removeView(toRemove);
    parent.addView(toAdd, childIndex > -1 ? childIndex : defaultIndex);
  }

  public static CharSequence ellipsize(@Nullable CharSequence text, @NonNull TextView view) {
    if (TextUtils.isEmpty(text) || view.getWidth() == 0 || view.getEllipsize() != TruncateAt.END) {
      return text;
    } else {
      return TextUtils.ellipsize(text,
                                 view.getPaint(),
                                 view.getWidth() - view.getPaddingRight() - view.getPaddingLeft(),
                                 TruncateAt.END);
    }
  }
}
