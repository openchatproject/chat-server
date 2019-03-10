package com.openchat.secureim.push;

import android.content.Context;

import com.openchat.secureim.ApplicationContext;
import com.openchat.secureim.crypto.SecurityEvent;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.jobs.RefreshPreKeysJob;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.imservice.api.OpenchatServiceMessageSender;

public class SecurityEventListener implements OpenchatServiceMessageSender.EventListener {
  private final Context context;

  public SecurityEventListener(Context context) {
    this.context = context.getApplicationContext();
  }

  @Override
  public void onSecurityEvent(long recipientId) {
    Recipients recipients = RecipientFactory.getRecipientsForIds(context, String.valueOf(recipientId), false);
    long       threadId   = DatabaseFactory.getThreadDatabase(context).getThreadIdFor(recipients);

    SecurityEvent.broadcastSecurityUpdateEvent(context, threadId);

    ApplicationContext.getInstance(context)
                      .getJobManager()
                      .add(new RefreshPreKeysJob(context));
  }

}
