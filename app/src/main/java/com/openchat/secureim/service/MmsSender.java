package com.openchat.secureim.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.openchat.secureim.R;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.MmsDatabase;
import com.openchat.secureim.database.ThreadDatabase;
import com.openchat.secureim.mms.MmsSendResult;
import com.openchat.secureim.notifications.MessageNotifier;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.service.SendReceiveService.ToastHandler;
import com.openchat.secureim.sms.IncomingIdentityUpdateMessage;
import com.openchat.secureim.transport.InsecureFallbackApprovalException;
import com.openchat.secureim.transport.RetryLaterException;
import com.openchat.secureim.transport.SecureFallbackApprovalException;
import com.openchat.secureim.transport.UndeliverableMessageException;
import com.openchat.secureim.transport.UniversalTransport;
import com.openchat.imservice.crypto.UntrustedIdentityException;
import com.openchat.secureim.crypto.MasterSecret;

import ws.com.google.android.mms.MmsException;
import ws.com.google.android.mms.pdu.SendReq;

public class MmsSender {

  private final Context             context;
  private final SystemStateListener systemStateListener;
  private final ToastHandler        toastHandler;

  public MmsSender(Context context, SystemStateListener systemStateListener, ToastHandler toastHandler) {
    this.context             = context;
    this.systemStateListener = systemStateListener;
    this.toastHandler        = toastHandler;
  }

  public void process(MasterSecret masterSecret, Intent intent) {
    Log.w("MmsSender", "Got intent action: " + intent.getAction());
    if (SendReceiveService.SEND_MMS_ACTION.equals(intent.getAction())) {
      handleSendMms(masterSecret, intent);
    }
  }

  private void handleSendMms(MasterSecret masterSecret, Intent intent) {
    long               messageId = intent.getLongExtra("message_id", -1);
    MmsDatabase        database  = DatabaseFactory.getMmsDatabase(context);
    ThreadDatabase     threads   = DatabaseFactory.getThreadDatabase(context);
    UniversalTransport transport = new UniversalTransport(context, masterSecret);

    try {
      SendReq[] messages = database.getOutgoingMessages(masterSecret, messageId);

      for (SendReq message : messages) {
        long threadId = database.getThreadIdForMessage(message.getDatabaseMessageId());

        try {
          Log.w("MmsSender", "Passing to MMS transport: " + message.getDatabaseMessageId());
          database.markAsSending(message.getDatabaseMessageId());
          MmsSendResult result = transport.deliver(message);

          if (result.isUpgradedSecure()) database.markAsSecure(message.getDatabaseMessageId());
          if (result.isPush())           database.markAsPush(message.getDatabaseMessageId());

          database.markAsSent(message.getDatabaseMessageId(), result.getMessageId(),
                              result.getResponseStatus());

          systemStateListener.unregisterForConnectivityChange();
        } catch (InsecureFallbackApprovalException ifae) {
          Log.w("MmsSender", ifae);
          database.markAsPendingInsecureSmsFallback(message.getDatabaseMessageId());
          notifyMessageDeliveryFailed(context, threads, threadId);
        } catch (SecureFallbackApprovalException sfae) {
          Log.w("MmsSender", sfae);
          database.markAsPendingSecureSmsFallback(message.getDatabaseMessageId());
          notifyMessageDeliveryFailed(context, threads, threadId);
        } catch (UndeliverableMessageException e) {
          Log.w("MmsSender", e);
          database.markAsSentFailed(message.getDatabaseMessageId());
          notifyMessageDeliveryFailed(context, threads, threadId);
        } catch (UntrustedIdentityException uie) {
          IncomingIdentityUpdateMessage identityUpdateMessage = IncomingIdentityUpdateMessage.createFor(message.getTo()[0].getString(), uie.getIdentityKey());
          DatabaseFactory.getEncryptingSmsDatabase(context).insertMessageInbox(masterSecret, identityUpdateMessage);
          database.markAsSentFailed(messageId);
        } catch (RetryLaterException e) {
          Log.w("MmsSender", e);
          database.markAsOutbox(message.getDatabaseMessageId());

          if (systemStateListener.isConnected()) scheduleQuickRetryAlarm();
          else                                   systemStateListener.registerForConnectivityChange();

          toastHandler
              .obtainMessage(0, context.getString(R.string.SmsReceiver_currently_unable_to_send_your_sms_message))
              .sendToTarget();
        }
      }
    } catch (MmsException e) {
      Log.w("MmsSender", e);
      if (messageId != -1)
        database.markAsSentFailed(messageId);
    }
  }

  private static void notifyMessageDeliveryFailed(Context context, ThreadDatabase threads, long threadId) {
    Recipients recipients = threads.getRecipientsForThreadId(threadId);
    MessageNotifier.notifyMessageDeliveryFailed(context, recipients, threadId);
  }

  private void scheduleQuickRetryAlarm() {
    ((AlarmManager)context.getSystemService(Context.ALARM_SERVICE))
        .set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (30 * 1000),
             PendingIntent.getService(context, 0,
                                      new Intent(SendReceiveService.SEND_MMS_ACTION,
                                                 null, context, SendReceiveService.class),
                                      PendingIntent.FLAG_UPDATE_CURRENT));
  }
}
