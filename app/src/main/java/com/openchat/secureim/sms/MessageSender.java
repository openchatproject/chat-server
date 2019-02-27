package com.openchat.secureim.sms;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.openchat.secureim.mms.OutgoingMediaMessage;
import com.openchat.imservice.crypto.MasterSecret;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.service.SendReceiveService;

import java.util.List;

import ws.com.google.android.mms.MmsException;

public class MessageSender {

  public static long send(Context context, MasterSecret masterSecret,
                          OutgoingTextMessage message, long threadId,
                          boolean forceSms)
  {
    if (threadId == -1)
      threadId = DatabaseFactory.getThreadDatabase(context).getThreadIdFor(message.getRecipients());

    List<Long> messageIds = DatabaseFactory.getEncryptingSmsDatabase(context)
        .insertMessageOutbox(masterSecret, threadId, message, forceSms);

    for (long messageId : messageIds) {
      Log.w("SMSSender", "Got message id for new message: " + messageId);

      Intent intent = new Intent(SendReceiveService.SEND_SMS_ACTION, null,
                                 context, SendReceiveService.class);
      intent.putExtra("message_id", messageId);
      context.startService(intent);
    }

    return threadId;
  }

  public static long send(Context context, MasterSecret masterSecret,
                          OutgoingMediaMessage message,
                          long threadId, boolean forceSms)
      throws MmsException
  {
    if (threadId == -1)
      threadId = DatabaseFactory.getThreadDatabase(context).getThreadIdFor(message.getRecipients(), message.getDistributionType());

    long messageId = DatabaseFactory.getMmsDatabase(context)
                                    .insertMessageOutbox(masterSecret, message, threadId, forceSms);

    Intent intent  = new Intent(SendReceiveService.SEND_MMS_ACTION, null,
                                context, SendReceiveService.class);
    intent.putExtra("message_id", messageId);
    intent.putExtra("thread_id", threadId);

    context.startService(intent);

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

}
