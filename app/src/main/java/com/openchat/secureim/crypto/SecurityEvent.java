package com.openchat.secureim.crypto;

import android.content.Context;
import android.content.Intent;

import com.openchat.secureim.service.KeyCachingService;

public class SecurityEvent {

  public static final String SECURITY_UPDATE_EVENT = "com.openchat.secureim.KEY_EXCHANGE_UPDATE";

  public static void broadcastSecurityUpdateEvent(Context context) {
    broadcastSecurityUpdateEvent(context, -2);
  }

  public static void broadcastSecurityUpdateEvent(Context context, long threadId) {
    Intent intent = new Intent(SECURITY_UPDATE_EVENT);
    intent.putExtra("thread_id", threadId);
    intent.setPackage(context.getPackageName());
    context.sendBroadcast(intent, KeyCachingService.KEY_PERMISSION);
  }

}
