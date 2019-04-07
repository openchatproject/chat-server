package com.openchat.secureim.push;

import android.content.Context;

import com.openchat.secureim.crypto.SecurityEvent;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.imservice.api.OpenchatServiceMessageSender;
import com.openchat.imservice.api.push.OpenchatServiceAddress;

public class SecurityEventListener implements OpenchatServiceMessageSender.EventListener {

  private static final String TAG = SecurityEventListener.class.getSimpleName();

  private final Context context;

  public SecurityEventListener(Context context) {
    this.context = context.getApplicationContext();
  }

  @Override
  public void onSecurityEvent(OpenchatServiceAddress openchatServiceAddress) {
    Recipients recipients = RecipientFactory.getRecipientsFromString(context, openchatServiceAddress.getNumber(), false);
    long       threadId   = DatabaseFactory.getThreadDatabase(context).getThreadIdFor(recipients);

    SecurityEvent.broadcastSecurityUpdateEvent(context, threadId);
  }
}
