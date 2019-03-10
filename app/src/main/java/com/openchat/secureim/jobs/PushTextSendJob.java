package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.ApplicationContext;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.storage.OpenchatServiceOpenchatStore;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.EncryptingSmsDatabase;
import com.openchat.secureim.database.NoSuchMessageException;
import com.openchat.secureim.database.SmsDatabase;
import com.openchat.secureim.database.model.SmsMessageRecord;
import com.openchat.secureim.notifications.MessageNotifier;
import com.openchat.secureim.push.OpenchatServiceMessageSenderFactory;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.sms.IncomingIdentityUpdateMessage;
import com.openchat.secureim.transport.InsecureFallbackApprovalException;
import com.openchat.secureim.transport.RetryLaterException;
import com.openchat.secureim.transport.SecureFallbackApprovalException;
import com.openchat.protocal.state.OpenchatStore;
import com.openchat.imservice.api.OpenchatServiceMessageSender;
import com.openchat.imservice.api.messages.OpenchatServiceMessage;
import com.openchat.imservice.crypto.UntrustedIdentityException;
import com.openchat.imservice.push.PushAddress;
import com.openchat.imservice.push.UnregisteredUserException;
import com.openchat.imservice.storage.RecipientDevice;
import com.openchat.imservice.util.InvalidNumberException;

import java.io.IOException;

public class PushTextSendJob extends PushSendJob {

  private static final String TAG = PushTextSendJob.class.getSimpleName();

  private final long messageId;

  public PushTextSendJob(Context context, long messageId, String destination) {
    super(context, constructParameters(context, destination));
    this.messageId = messageId;
  }

  @Override
  public void onAdded() {

  }

  @Override
  public void onRun() throws RequirementNotMetException, NoSuchMessageException, RetryLaterException
  {
    MasterSecret          masterSecret = getMasterSecret();
    EncryptingSmsDatabase database     = DatabaseFactory.getEncryptingSmsDatabase(context);
    SmsMessageRecord      record       = database.getMessage(masterSecret, messageId);
    String                destination  = record.getIndividualRecipient().getNumber();

    try {
      Log.w(TAG, "Sending message: " + messageId);

      deliver(masterSecret, record, destination);

      database.markAsPush(messageId);
      database.markAsSecure(messageId);
      database.markAsSent(messageId);
    } catch (InsecureFallbackApprovalException e) {
      Log.w(TAG, e);
      database.markAsPendingInsecureSmsFallback(record.getId());
      MessageNotifier.notifyMessageDeliveryFailed(context, record.getRecipients(), record.getThreadId());
    } catch (SecureFallbackApprovalException e) {
      Log.w(TAG, e);
      database.markAsPendingSecureSmsFallback(record.getId());
      MessageNotifier.notifyMessageDeliveryFailed(context, record.getRecipients(), record.getThreadId());
    } catch (UntrustedIdentityException e) {
      Log.w(TAG, e);
      IncomingIdentityUpdateMessage identityUpdateMessage = IncomingIdentityUpdateMessage.createFor(e.getE164Number(), e.getIdentityKey());
      database.insertMessageInbox(masterSecret, identityUpdateMessage);
      database.markAsSentFailed(record.getId());
    }
  }

  public void deliver(MasterSecret masterSecret, SmsMessageRecord message, String destination)
      throws UntrustedIdentityException, SecureFallbackApprovalException,
             InsecureFallbackApprovalException, RetryLaterException
  {
    boolean isSmsFallbackSupported = isSmsFallbackSupported(context, destination);

    try {
      PushAddress             address       = getPushAddress(message.getIndividualRecipient());
      OpenchatServiceMessageSender messageSender = OpenchatServiceMessageSenderFactory.create(context, masterSecret);

      if (message.isEndSession()) {
        messageSender.sendMessage(address, new OpenchatServiceMessage(message.getDateSent(), null,
                                                                 null, null, true, true));
      } else {
        messageSender.sendMessage(address, new OpenchatServiceMessage(message.getDateSent(), null,
                                                                 message.getBody().getBody()));
      }
    } catch (InvalidNumberException | UnregisteredUserException e) {
      Log.w(TAG, e);
      if (isSmsFallbackSupported) fallbackOrAskApproval(masterSecret, message, destination);
      else                        DatabaseFactory.getSmsDatabase(context).markAsSentFailed(messageId);
    } catch (IOException e) {
      Log.w(TAG, e);
      if (isSmsFallbackSupported) fallbackOrAskApproval(masterSecret, message, destination);
      else                        throw new RetryLaterException(e);
    }
  }

  @Override
  public boolean onShouldRetry(Throwable throwable) {
    if (throwable instanceof RequirementNotMetException) return true;
    if (throwable instanceof RetryLaterException)        return true;

    return false;
  }

  @Override
  public void onCanceled() {
    DatabaseFactory.getSmsDatabase(context).markAsSentFailed(messageId);

    long       threadId   = DatabaseFactory.getSmsDatabase(context).getThreadIdForMessage(messageId);
    Recipients recipients = DatabaseFactory.getThreadDatabase(context).getRecipientsForThreadId(threadId);

    MessageNotifier.notifyMessageDeliveryFailed(context, recipients, threadId);
  }

  private void fallbackOrAskApproval(MasterSecret masterSecret, SmsMessageRecord smsMessage, String destination)
      throws SecureFallbackApprovalException, InsecureFallbackApprovalException
  {
    Recipient    recipient                     = smsMessage.getIndividualRecipient();
    boolean      isSmsFallbackApprovalRequired = isSmsFallbackApprovalRequired(destination);
    OpenchatStore axolotlStore                  = new OpenchatServiceOpenchatStore(context, masterSecret);

    if (!isSmsFallbackApprovalRequired) {
      Log.w(TAG, "Falling back to SMS");
      DatabaseFactory.getSmsDatabase(context).markAsForcedSms(smsMessage.getId());
      ApplicationContext.getInstance(context).getJobManager().add(new SmsSendJob(context, messageId, destination));
    } else if (!axolotlStore.containsSession(recipient.getRecipientId(), RecipientDevice.DEFAULT_DEVICE_ID)) {
      Log.w(TAG, "Marking message as pending insecure fallback.");
      throw new InsecureFallbackApprovalException("Pending user approval for fallback to insecure SMS");
    } else {
      Log.w(TAG, "Marking message as pending secure fallback.");
      throw new SecureFallbackApprovalException("Pending user approval for fallback to secure SMS");
    }
  }

}
