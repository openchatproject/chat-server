package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.ApplicationContext;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.SessionUtil;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.EncryptingSmsDatabase;
import com.openchat.secureim.database.NoSuchMessageException;
import com.openchat.secureim.database.SmsDatabase;
import com.openchat.secureim.database.model.SmsMessageRecord;
import com.openchat.secureim.dependencies.InjectableType;
import com.openchat.secureim.notifications.MessageNotifier;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.transport.InsecureFallbackApprovalException;
import com.openchat.secureim.transport.RetryLaterException;
import com.openchat.secureim.transport.SecureFallbackApprovalException;
import com.openchat.imservice.api.OpenchatServiceMessageSender;
import com.openchat.imservice.api.crypto.UntrustedIdentityException;
import com.openchat.imservice.api.messages.OpenchatServiceMessage;
import com.openchat.imservice.api.push.OpenchatServiceAddress;
import com.openchat.imservice.api.push.exceptions.UnregisteredUserException;
import com.openchat.imservice.api.util.InvalidNumberException;

import java.io.IOException;

import javax.inject.Inject;

import static com.openchat.secureim.dependencies.OpenchatServiceCommunicationModule.OpenchatServiceMessageSenderFactory;

public class PushTextSendJob extends PushSendJob implements InjectableType {

  private static final String TAG = PushTextSendJob.class.getSimpleName();

  @Inject transient OpenchatServiceMessageSenderFactory messageSenderFactory;

  private final long messageId;

  public PushTextSendJob(Context context, long messageId, String destination) {
    super(context, constructParameters(context, destination, false));
    this.messageId = messageId;
  }

  @Override
  public void onAdded() {
    SmsDatabase smsDatabase = DatabaseFactory.getSmsDatabase(context);
    smsDatabase.markAsSending(messageId);
    smsDatabase.markAsPush(messageId);
  }

  @Override
  public void onSend(MasterSecret masterSecret) throws NoSuchMessageException, RetryLaterException {
    EncryptingSmsDatabase database    = DatabaseFactory.getEncryptingSmsDatabase(context);
    SmsMessageRecord      record      = database.getMessage(masterSecret, messageId);
    String                destination = record.getIndividualRecipient().getNumber();

    try {
      Log.w(TAG, "Sending message: " + messageId);

      if (deliver(masterSecret, record, destination)) {
        database.markAsPush(messageId);
        database.markAsSecure(messageId);
        database.markAsSent(messageId);
      }
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
      Recipients recipients  = RecipientFactory.getRecipientsFromString(context, e.getE164Number(), false);
      long       recipientId = recipients.getPrimaryRecipient().getRecipientId();

      database.addMismatchedIdentity(record.getId(), recipientId, e.getIdentityKey());
      database.markAsSentFailed(record.getId());
      database.markAsPush(record.getId());
    }
  }

  @Override
  public boolean onShouldRetryThrowable(Exception exception) {
    if (exception instanceof RetryLaterException) return true;

    return false;
  }

  @Override
  public void onCanceled() {
    DatabaseFactory.getSmsDatabase(context).markAsSentFailed(messageId);

    long       threadId   = DatabaseFactory.getSmsDatabase(context).getThreadIdForMessage(messageId);
    Recipients recipients = DatabaseFactory.getThreadDatabase(context).getRecipientsForThreadId(threadId);

    MessageNotifier.notifyMessageDeliveryFailed(context, recipients, threadId);
  }

  private boolean deliver(MasterSecret masterSecret, SmsMessageRecord message, String destination)
      throws UntrustedIdentityException, SecureFallbackApprovalException,
             InsecureFallbackApprovalException, RetryLaterException
  {
    boolean isSmsFallbackSupported = isSmsFallbackSupported(context, destination, false);

    try {
      OpenchatServiceAddress       address       = getPushAddress(message.getIndividualRecipient().getNumber());
      OpenchatServiceMessageSender messageSender = messageSenderFactory.create(masterSecret);

      if (message.isEndSession()) {
        messageSender.sendMessage(address, new OpenchatServiceMessage(message.getDateSent(), null,
                                                                 null, null, true, true));
      } else {
        messageSender.sendMessage(address, new OpenchatServiceMessage(message.getDateSent(), message.getBody().getBody()));
      }

      return true;
    } catch (InvalidNumberException | UnregisteredUserException e) {
      Log.w(TAG, e);
      if (isSmsFallbackSupported) fallbackOrAskApproval(masterSecret, message, destination);
      else                        DatabaseFactory.getSmsDatabase(context).markAsSentFailed(messageId);
    } catch (IOException e) {
      Log.w(TAG, e);
      if (isSmsFallbackSupported) fallbackOrAskApproval(masterSecret, message, destination);
      else                        throw new RetryLaterException(e);
    }

    return false;
  }

  private void fallbackOrAskApproval(MasterSecret masterSecret, SmsMessageRecord smsMessage, String destination)
      throws SecureFallbackApprovalException, InsecureFallbackApprovalException
  {
    Recipient recipient                     = smsMessage.getIndividualRecipient();
    boolean   isSmsFallbackApprovalRequired = isSmsFallbackApprovalRequired(destination, false);

    if (!isSmsFallbackApprovalRequired) {
      Log.w(TAG, "Falling back to SMS");
      DatabaseFactory.getSmsDatabase(context).markAsForcedSms(smsMessage.getId());
      ApplicationContext.getInstance(context).getJobManager().add(new SmsSendJob(context, messageId, destination));
    } else if (!SessionUtil.hasSession(context, masterSecret, recipient)) {
      Log.w(TAG, "Marking message as pending insecure fallback.");
      throw new InsecureFallbackApprovalException("Pending user approval for fallback to insecure SMS");
    } else {
      Log.w(TAG, "Marking message as pending secure fallback.");
      throw new SecureFallbackApprovalException("Pending user approval for fallback to secure SMS");
    }
  }

}
