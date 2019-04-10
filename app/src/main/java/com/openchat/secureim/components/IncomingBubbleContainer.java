package com.openchat.secureim.components;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;

import com.openchat.secureim.R;
import com.openchat.secureim.util.ResUtil;

public class IncomingBubbleContainer extends BubbleContainer {
  private static final String TAG = IncomingBubbleContainer.class.getSimpleName();

  private static final boolean[] CORNERS_MESSAGE_CAPTIONED = new boolean[]{false, true, true, true };
  private static final boolean[] CORNERS_MEDIA_CAPTIONED   = new boolean[]{true,  true, true, false};
  private static final boolean[] CORNERS_ROUNDED           = new boolean[]{true,  true, true, true };

  private int foregroundColor;
  private int triangleTickRes;

  @SuppressWarnings("UnusedDeclaration")
  public IncomingBubbleContainer(Context context) {
    super(context);
  }

  @SuppressWarnings("UnusedDeclaration")
  public IncomingBubbleContainer(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @SuppressWarnings("UnusedDeclaration")
  public IncomingBubbleContainer(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @SuppressWarnings("UnusedDeclaration")
  public IncomingBubbleContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  protected void onCreateView() {
    Log.w(TAG, "onCreateView()");
    LayoutInflater inflater = LayoutInflater.from(getContext());
    inflater.inflate(R.layout.conversation_bubble_incoming, this, true);

    this.foregroundColor = ResUtil.getColor(getContext(), R.attr.conversation_item_received_background);
    this.triangleTickRes = ResUtil.getDrawableRes(getContext(), R.attr.triangle_tick_incoming);
  }

  @Override
  protected int getForegroundColor(@TransportState int transportState) {
    return foregroundColor;
  }

  @Override
  protected boolean[] getMessageCorners(@MediaState int mediaState) {
    return mediaState == MEDIA_STATE_CAPTIONED ? CORNERS_MESSAGE_CAPTIONED : CORNERS_ROUNDED;
  }

  @Override
  protected boolean[] getMediaCorners(@MediaState int mediaState) {
    return mediaState == MEDIA_STATE_CAPTIONED ? CORNERS_MEDIA_CAPTIONED : CORNERS_ROUNDED;
  }

  @Override
  protected int getTriangleTickRes(@TransportState int transportState) {
    return triangleTickRes;
  }
}
