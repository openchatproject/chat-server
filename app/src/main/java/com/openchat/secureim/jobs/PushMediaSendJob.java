package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.ApplicationContext;
import com.openchat.secureim.attachments.Attachment;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.Address;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.MmsDatabase;
import com.openchat.secureim.database.NoSuchMessageException;
import com.openchat.secureim.dependencies.InjectableType;
import com.openchat.secureim.mms.MediaConstraints;
import com.openchat.secureim.mms.MmsException;
import com.openchat.secureim.mms.OutgoingMediaMessage;
import com.openchat.secureim.service.ExpiringMessageManager;
import com.openchat.secureim.transport.InsecureFallbackApprovalException;
import com.openchat.secureim.transport.RetryLaterException;
import com.openchat.secureim.transport.UndeliverableMessageException;
import com.openchat.libim.util.guava.Optional;
import com.openchat.imservice.api.openchatServiceMessageSender;
import com.openchat.imservice.api.crypto.UntrustedIdentityException;
import com.openchat.imservice.api.messages.openchatServiceAttachment;
import com.openchat.imservice.api.messages.openchatServiceDataMessage;
import com.openchat.imservice.api.push.openchatServiceAddress;
import com.openchat.imservice.api.push.exceptions.UnregisteredUserException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

public class PushMediaSendJob extends PushSendJob implements InjectableType {

  private static final long serialVersionUID = 1L;

  private static final String TAG = PushMediaSendJob.class.getSimpleName();

  @Inject transient openchatServiceMessageSender messageSender;

  private final long messageId;

  public PushMediaSendJob(Context context, long messageId, Address destination) {
    super(context, constructParameters(context, destination));
    this.messageId = messageId;
  }

  @Override
  public void onAdded() {

  }

  @Override
  public void onPushSend(MasterSecret masterSecret)
      throws RetryLaterException, MmsException, NoSuchMessageException,
             UndeliverableMessageException
  {
    ExpiringMessageManager expirationManager = ApplicationContext.getInstance(context).getExpiringMessageManager();
    MmsDatabase            database          = DatabaseFactory.getMmsDatabase(context);
    OutgoingMediaMessage   message           = database.getOutgoingMessage(masterSecret, messageId);

    try {
      deliver(masterSecret, message);
      database.markAsSent(messageId, true);
      markAttachmentsUploaded(messageId, message.getAttachments());

      if (message.getExpiresIn() > 0 && !message.isExpirationUpdate()) {
        database.markExpireStarted(messageId);
        expirationManager.scheduleDeletion(messageId, true, message.getExpiresIn());
      }

    } catch (InsecureFallbackApprovalException ifae) {
      Log.w(TAG, ifae);
      database.markAsPendingInsecureSmsFallback(messageId);
      notifyMediaMessageDeliveryFailed(context, messageId);
      ApplicationContext.getInstance(context).getJobManager().add(new DirectoryRefreshJob(context, false));
    } catch (UntrustedIdentityException uie) {
      Log.w(TAG, uie);
      database.addMismatchedIdentity(messageId, Address.fromSerialized(uie.getE164Number()), uie.getIdentityKey());
      database.markAsSentFailed(messageId);
    }
  }

  @Override
  public boolean onShouldRetryThrowable(Exception exception) {
    if (exception instanceof RequirementNotMetException) return true;
    if (exception instanceof RetryLaterException)        return true;

    return false;
  }

  @Override
  public void onCanceled() {
    DatabaseFactory.getMmsDatabase(context).markAsSentFailed(messageId);
    notifyMediaMessageDeliveryFailed(context, messageId);
  }

  private void deliver(MasterSecret masterSecret, OutgoingMediaMessage message)
      throws RetryLaterException, InsecureFallbackApprovalException, UntrustedIdentityException,
             UndeliverableMessageException
  {
    if (message.getRecipient() == null) {
      throw new UndeliverableMessageException("No destination address.");
    }

    try {
      openchatServiceAddress          address           = getPushAddress(message.getRecipient().getAddress());
      MediaConstraints              mediaConstraints  = MediaConstraints.getPushMediaConstraints();
      List<Attachment>              scaledAttachments = scaleAttachments(masterSecret, mediaConstraints, message.getAttachments());
      List<openchatServiceAttachment> attachmentStreams = getAttachmentsFor(masterSecret, scaledAttachments);
      Optional<byte[]>              profileKey        = getProfileKey(message.getRecipient());
      openchatServiceDataMessage      mediaMessage      = openchatServiceDataMessage.newBuilder()
                                                                                .withBody(message.getBody())
                                                                                .withAttachments(attachmentStreams)
                                                                                .withTimestamp(message.getSentTimeMillis())
                                                                                .withExpiration((int)(message.getExpiresIn() / 1000))
                                                                                .withProfileKey(profileKey.orNull())
                                                                                .asExpirationUpdate(message.isExpirationUpdate())
                                                                                .build();

      messageSender.sendMessage(address, mediaMessage);
    } catch (UnregisteredUserException e) {
      Log.w(TAG, e);
      throw new InsecureFallbackApprovalException(e);
    } catch (FileNotFoundException e) {
      Log.w(TAG, e);
      throw new UndeliverableMessageException(e);
    } catch (IOException e) {
      Log.w(TAG, e);
      throw new RetryLaterException(e);
    }
  }
}
