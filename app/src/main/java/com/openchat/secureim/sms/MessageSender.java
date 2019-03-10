package com.openchat.secureim.sms;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.EncryptingSmsDatabase;
import com.openchat.secureim.database.MmsDatabase;
import com.openchat.secureim.mms.OutgoingMediaMessage;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.service.SendReceiveService;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.secureim.util.Util;
import com.openchat.imservice.util.InvalidNumberException;

import java.util.List;

import ws.com.google.android.mms.MmsException;

public class MessageSender {

  public static long send(Context context, MasterSecret masterSecret,
                          OutgoingTextMessage message, long threadId,
                          boolean forceSms)
  {
    EncryptingSmsDatabase database = DatabaseFactory.getEncryptingSmsDatabase(context);

    if (threadId == -1) {
      threadId = DatabaseFactory.getThreadDatabase(context).getThreadIdFor(message.getRecipients());
    }

    List<Long> messageIds = database.insertMessageOutbox(masterSecret, threadId, message, forceSms);

    if (!forceSms && isSelfSend(context, message.getRecipients())) {
      for (long messageId : messageIds) {
        database.markAsSent(messageId);
        database.markAsPush(messageId);

        Pair<Long, Long> messageAndThreadId = database.copyMessageInbox(messageId);
        database.markAsPush(messageAndThreadId.first);
      }
    } else {
      for (long messageId : messageIds) {
        Log.w("SMSSender", "Got message id for new message: " + messageId);

        Intent intent = new Intent(SendReceiveService.SEND_SMS_ACTION, null,
                                   context, SendReceiveService.class);
        intent.putExtra("message_id", messageId);
        context.startService(intent);
      }
    }

    return threadId;
  }

  public static long send(Context context, MasterSecret masterSecret,
                          OutgoingMediaMessage message,
                          long threadId, boolean forceSms)
      throws MmsException
  {
    MmsDatabase database = DatabaseFactory.getMmsDatabase(context);

    if (threadId == -1) {
      threadId = DatabaseFactory.getThreadDatabase(context).getThreadIdFor(message.getRecipients(), message.getDistributionType());
    }

    long messageId = database.insertMessageOutbox(masterSecret, message, threadId, forceSms);

    if (!forceSms && isSelfSend(context, message.getRecipients())) {
      database.markAsSent(messageId, "self-send".getBytes(), 0);
      database.markAsPush(messageId);
      long newMessageId = database.copyMessageInbox(masterSecret, messageId);
      database.markAsPush(newMessageId);
    } else {
      Intent intent  = new Intent(SendReceiveService.SEND_MMS_ACTION, null,
                                  context, SendReceiveService.class);
      intent.putExtra("message_id", messageId);
      intent.putExtra("thread_id", threadId);

      context.startService(intent);
    }

    return threadId;
  }

  public static void resend(Context context, long messageId, boolean isMms)
  {

    Intent intent;
    if (isMms) {
      DatabaseFactory.getMmsDatabase(context).markAsSending(messageId);
      intent  = new Intent(SendReceiveService.SEND_MMS_ACTION, null,
                           context, SendReceiveService.class);
    } else {
      DatabaseFactory.getSmsDatabase(context).markAsSending(messageId);
      intent  = new Intent(SendReceiveService.SEND_SMS_ACTION, null,
                           context, SendReceiveService.class);
    }
    intent.putExtra("message_id", messageId);
    context.startService(intent);
  }

  private static boolean isSelfSend(Context context, Recipients recipients) {
    try {
      if (!OpenchatServicePreferences.isPushRegistered(context)) {
        return false;
      }

      if (!recipients.isSingleRecipient()) {
        return false;
      }

      String e164number = Util.canonicalizeNumber(context, recipients.getPrimaryRecipient().getNumber());
      return OpenchatServicePreferences.getLocalNumber(context).equals(e164number);
    } catch (InvalidNumberException e) {
      Log.w("MessageSender", e);
      return false;
    }
  }

}
