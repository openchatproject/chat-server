package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.ApplicationContext;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.EncryptingSmsDatabase;
import com.openchat.secureim.database.NoSuchMessageException;
import com.openchat.secureim.database.SmsDatabase;
import com.openchat.secureim.database.model.SmsMessageRecord;
import com.openchat.secureim.dependencies.InjectableType;
import com.openchat.secureim.notifications.MessageNotifier;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.transport.InsecureFallbackApprovalException;
import com.openchat.secureim.transport.RetryLaterException;
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
    super(context, constructParameters(context, destination));
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
    EncryptingSmsDatabase database = DatabaseFactory.getEncryptingSmsDatabase(context);
    SmsMessageRecord      record   = database.getMessage(masterSecret, messageId);

    try {
      Log.w(TAG, "Sending message: " + messageId);

      deliver(masterSecret, record);
      database.markAsPush(messageId);
      database.markAsSecure(messageId);
      database.markAsSent(messageId);

    } catch (InsecureFallbackApprovalException e) {
      Log.w(TAG, e);
      database.markAsPendingInsecureSmsFallback(record.getId());
      MessageNotifier.notifyMessageDeliveryFailed(context, record.getRecipients(), record.getThreadId());
      ApplicationContext.getInstance(context).getJobManager().add(new DirectoryRefreshJob(context));
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

    if (threadId != -1 && recipients != null) {
      MessageNotifier.notifyMessageDeliveryFailed(context, recipients, threadId);
    }
  }

  private void deliver(MasterSecret masterSecret, SmsMessageRecord message)
      throws UntrustedIdentityException, InsecureFallbackApprovalException, RetryLaterException
  {
    try {
      OpenchatServiceAddress       address           = getPushAddress(message.getIndividualRecipient().getNumber());
      OpenchatServiceMessageSender messageSender     = messageSenderFactory.create(masterSecret);
      OpenchatServiceMessage       openchatServiceMessage = OpenchatServiceMessage.newBuilder()
                                                                   .withTimestamp(message.getDateSent())
                                                                   .withBody(message.getBody().getBody())
                                                                   .asEndSessionMessage(message.isEndSession())
                                                                   .build();

      messageSender.sendMessage(address, openchatServiceMessage);
    } catch (InvalidNumberException | UnregisteredUserException e) {
      Log.w(TAG, e);
      throw new InsecureFallbackApprovalException(e);
    } catch (IOException e) {
      Log.w(TAG, e);
      throw new RetryLaterException(e);
    }
  }
}
