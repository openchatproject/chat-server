package com.openchat.secureim.push;

import android.content.Context;

import com.openchat.secureim.crypto.SecurityEvent;
import com.openchat.imservice.api.openchatServiceMessageSender;
import com.openchat.imservice.api.push.openchatServiceAddress;

public class SecurityEventListener implements openchatServiceMessageSender.EventListener {

  private static final String TAG = SecurityEventListener.class.getSimpleName();

  private final Context context;

  public SecurityEventListener(Context context) {
    this.context = context.getApplicationContext();
  }

  @Override
  public void onSecurityEvent(openchatServiceAddress textSecureAddress) {
    SecurityEvent.broadcastSecurityUpdateEvent(context);
  }
}
