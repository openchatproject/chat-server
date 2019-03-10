package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.ApplicationContext;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.storage.OpenchatServiceOpenchatStore;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.MmsDatabase;
import com.openchat.secureim.database.NoSuchMessageException;
import com.openchat.secureim.mms.PartParser;
import com.openchat.secureim.push.OpenchatServiceCommunicationFactory;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.RecipientFormattingException;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.sms.IncomingIdentityUpdateMessage;
import com.openchat.secureim.transport.InsecureFallbackApprovalException;
import com.openchat.secureim.transport.RetryLaterException;
import com.openchat.secureim.transport.SecureFallbackApprovalException;
import com.openchat.protocal.state.OpenchatStore;
import com.openchat.imservice.api.OpenchatServiceMessageSender;
import com.openchat.imservice.api.messages.OpenchatServiceAttachment;
import com.openchat.imservice.api.messages.OpenchatServiceMessage;
import com.openchat.imservice.api.crypto.UntrustedIdentityException;
import com.openchat.imservice.push.PushAddress;
import com.openchat.imservice.push.UnregisteredUserException;
import com.openchat.imservice.storage.RecipientDevice;
import com.openchat.imservice.util.InvalidNumberException;

import java.io.IOException;
import java.util.List;

import ws.com.google.android.mms.MmsException;
import ws.com.google.android.mms.pdu.SendReq;

public class PushMediaSendJob extends PushSendJob {

  private static final String TAG = PushMediaSendJob.class.getSimpleName();

  private final long messageId;

  public PushMediaSendJob(Context context, long messageId, String destination) {
    super(context, constructParameters(context, destination));
    this.messageId = messageId;
  }

  @Override
  public void onAdded() {

  }

  @Override
  public void onRun()
      throws RequirementNotMetException, RetryLaterException, MmsException, NoSuchMessageException
  {
    MasterSecret masterSecret = getMasterSecret();
    MmsDatabase  database     = DatabaseFactory.getMmsDatabase(context);
    SendReq      message      = database.getOutgoingMessage(masterSecret, messageId);

    try {
      deliver(masterSecret, message);

      database.markAsPush(messageId);
      database.markAsSecure(messageId);
      database.markAsSent(messageId, "push".getBytes(), 0);
    } catch (InsecureFallbackApprovalException ifae) {
      Log.w(TAG, ifae);
      database.markAsPendingInsecureSmsFallback(messageId);
      notifyMediaMessageDeliveryFailed(context, messageId);
    } catch (SecureFallbackApprovalException sfae) {
      Log.w(TAG, sfae);
      database.markAsPendingSecureSmsFallback(messageId);
      notifyMediaMessageDeliveryFailed(context, messageId);
    } catch (UntrustedIdentityException uie) {
      IncomingIdentityUpdateMessage identityUpdateMessage = IncomingIdentityUpdateMessage.createFor(message.getTo()[0].getString(), uie.getIdentityKey());
      DatabaseFactory.getEncryptingSmsDatabase(context).insertMessageInbox(masterSecret, identityUpdateMessage);
      database.markAsSentFailed(messageId);
    }
  }

  @Override
  public void onCanceled() {
    DatabaseFactory.getMmsDatabase(context).markAsSentFailed(messageId);
    notifyMediaMessageDeliveryFailed(context, messageId);
  }

  @Override
  public boolean onShouldRetry(Throwable throwable) {
    if (throwable instanceof RetryLaterException)        return true;
    if (throwable instanceof RequirementNotMetException) return true;
    return false;
  }

  private void deliver(MasterSecret masterSecret, SendReq message)
      throws RetryLaterException, SecureFallbackApprovalException,
             InsecureFallbackApprovalException, UntrustedIdentityException
  {
    MmsDatabase             database               = DatabaseFactory.getMmsDatabase(context);
    OpenchatServiceMessageSender messageSender          = OpenchatServiceCommunicationFactory.createSender(context, masterSecret);
    String                  destination            = message.getTo()[0].getString();
    boolean                 isSmsFallbackSupported = isSmsFallbackSupported(context, destination);

    try {
      Recipients                 recipients   = RecipientFactory.getRecipientsFromString(context, destination, false);
      PushAddress                address      = getPushAddress(recipients.getPrimaryRecipient());
      List<OpenchatServiceAttachment> attachments  = getAttachments(message);
      String                     body         = PartParser.getMessageText(message.getBody());
      OpenchatServiceMessage          mediaMessage = new OpenchatServiceMessage(message.getSentTimestamp(), attachments, body);

      messageSender.sendMessage(address, mediaMessage);
    } catch (InvalidNumberException | UnregisteredUserException e) {
      Log.w(TAG, e);
      if (isSmsFallbackSupported) fallbackOrAskApproval(masterSecret, message, destination);
      else                        database.markAsSentFailed(messageId);
    } catch (IOException | RecipientFormattingException e) {
      Log.w(TAG, e);
      if (isSmsFallbackSupported) fallbackOrAskApproval(masterSecret, message, destination);
      else                        throw new RetryLaterException(e);
    }
  }

  private void fallbackOrAskApproval(MasterSecret masterSecret, SendReq mediaMessage, String destination)
      throws SecureFallbackApprovalException, InsecureFallbackApprovalException
  {
    try {
      Recipient    recipient                     = RecipientFactory.getRecipientsFromString(context, destination, false).getPrimaryRecipient();
      boolean      isSmsFallbackApprovalRequired = isSmsFallbackApprovalRequired(destination);
      OpenchatStore axolotlStore                  = new OpenchatServiceOpenchatStore(context, masterSecret);

      if (!isSmsFallbackApprovalRequired) {
        Log.w(TAG, "Falling back to MMS");
        DatabaseFactory.getMmsDatabase(context).markAsForcedSms(mediaMessage.getDatabaseMessageId());
        ApplicationContext.getInstance(context).getJobManager().add(new MmsSendJob(context, messageId));
      } else if (!axolotlStore.containsSession(recipient.getRecipientId(), RecipientDevice.DEFAULT_DEVICE_ID)) {
        Log.w(TAG, "Marking message as pending insecure SMS fallback");
        throw new InsecureFallbackApprovalException("Pending user approval for fallback to insecure SMS");
      } else {
        Log.w(TAG, "Marking message as pending secure SMS fallback");
        throw new SecureFallbackApprovalException("Pending user approval for fallback secure to SMS");
      }
    } catch (RecipientFormattingException rfe) {
      Log.w(TAG, rfe);
      DatabaseFactory.getMmsDatabase(context).markAsSentFailed(messageId);
    }
  }

}
