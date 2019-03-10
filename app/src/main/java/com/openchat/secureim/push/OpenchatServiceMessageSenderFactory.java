package com.openchat.secureim.push;

import android.content.Context;

import com.openchat.secureim.Release;
import com.openchat.secureim.crypto.SecurityEvent;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.storage.OpenchatServiceOpenchatStore;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.OpenchatServiceMessageSender;

import static com.openchat.imservice.api.OpenchatServiceMessageSender.EventListener;

public class OpenchatServiceMessageSenderFactory {
  public static OpenchatServiceMessageSender create(Context context, MasterSecret masterSecret) {
    return new OpenchatServiceMessageSender(context, Release.PUSH_URL,
                                       new OpenchatServicePushTrustStore(context),
                                       OpenchatServicePreferences.getLocalNumber(context),
                                       OpenchatServicePreferences.getPushServerPassword(context),
                                       new OpenchatServiceOpenchatStore(context, masterSecret),
                                       Optional.of((EventListener)new SecurityEventListener(context)));
  }

  private static class SecurityEventListener implements EventListener {

    private final Context context;

    public SecurityEventListener(Context context) {
      this.context = context.getApplicationContext();
    }

    @Override
    public void onSecurityEvent(long recipientId) {
      Recipients recipients = RecipientFactory.getRecipientsForIds(context, String.valueOf(recipientId), false);
      long       threadId   = DatabaseFactory.getThreadDatabase(context).getThreadIdFor(recipients);
      SecurityEvent.broadcastSecurityUpdateEvent(context, threadId);
    }
  }
}
