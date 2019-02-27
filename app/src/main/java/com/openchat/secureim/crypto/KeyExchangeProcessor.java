package com.openchat.secureim.crypto;

import android.content.Context;
import android.content.Intent;

import com.openchat.secureim.crypto.protocol.KeyExchangeMessage;
import com.openchat.secureim.service.KeyCachingService;
import com.openchat.protocal.InvalidMessageException;
import com.openchat.imservice.crypto.MasterSecret;
import com.openchat.imservice.storage.RecipientDevice;

public abstract class KeyExchangeProcessor {

  public static final String SECURITY_UPDATE_EVENT = "com.openchat.secureim.KEY_EXCHANGE_UPDATE";

  public abstract boolean isStale(KeyExchangeMessage message);
  public abstract boolean isTrusted(KeyExchangeMessage message);
  public abstract void processKeyExchangeMessage(KeyExchangeMessage message, long threadid)
      throws InvalidMessageException;

  public static KeyExchangeProcessor createFor(Context context, MasterSecret masterSecret,
                                               RecipientDevice recipientDevice,
                                               KeyExchangeMessage message)
  {
    return new KeyExchangeProcessorV2(context, masterSecret, recipientDevice);
  }

  public static void broadcastSecurityUpdateEvent(Context context, long threadId) {
    Intent intent = new Intent(SECURITY_UPDATE_EVENT);
    intent.putExtra("thread_id", threadId);
    intent.setPackage(context.getPackageName());
    context.sendBroadcast(intent, KeyCachingService.KEY_PERMISSION);
  }

}
