package com.openchat.secureim.jobs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.annimon.stream.Stream;

import com.openchat.secureim.ApplicationContext;
import com.openchat.secureim.attachments.Attachment;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.Address;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.GroupReceiptDatabase.GroupReceiptInfo;
import com.openchat.secureim.database.MmsDatabase;
import com.openchat.secureim.database.NoSuchMessageException;
import com.openchat.secureim.database.documents.NetworkFailure;
import com.openchat.secureim.dependencies.InjectableType;
import com.openchat.secureim.jobs.requirements.MasterSecretRequirement;
import com.openchat.secureim.mms.MediaConstraints;
import com.openchat.secureim.mms.MmsException;
import com.openchat.secureim.mms.OutgoingGroupMediaMessage;
import com.openchat.secureim.mms.OutgoingMediaMessage;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.RecipientFormattingException;
import com.openchat.secureim.transport.UndeliverableMessageException;
import com.openchat.secureim.util.GroupUtil;
import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.requirements.NetworkRequirement;
import com.openchat.libim.util.guava.Optional;
import com.openchat.imservice.api.openchatServiceMessageSender;
import com.openchat.imservice.api.crypto.UntrustedIdentityException;
import com.openchat.imservice.api.messages.openchatServiceAttachment;
import com.openchat.imservice.api.messages.openchatServiceDataMessage;
import com.openchat.imservice.api.messages.openchatServiceGroup;
import com.openchat.imservice.api.push.openchatServiceAddress;
import com.openchat.imservice.api.push.exceptions.EncapsulatedExceptions;
import com.openchat.imservice.api.push.exceptions.NetworkFailureException;
import com.openchat.imservice.api.util.InvalidNumberException;
import com.openchat.imservice.internal.push.openchatServiceProtos.GroupContext;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

public class PushGroupSendJob extends PushSendJob implements InjectableType {

  private static final long serialVersionUID = 1L;

  private static final String TAG = PushGroupSendJob.class.getSimpleName();

  @Inject transient openchatServiceMessageSender messageSender;

  private final long   messageId;
  private final long   filterRecipientId; // Deprecated
  private final String filterAddress;

  public PushGroupSendJob(Context context, long messageId, @NonNull Address destination, @Nullable Address filterAddress) {
    super(context, JobParameters.newBuilder()
                                .withPersistence()
                                .withGroupId(destination.toGroupString())
                                .withRequirement(new MasterSecretRequirement(context))
                                .withRequirement(new NetworkRequirement(context))
                                .withRetryCount(5)
                                .create());

    this.messageId         = messageId;
    this.filterAddress     = filterAddress == null ? null :filterAddress.toPhoneString();
    this.filterRecipientId = -1;
  }

  @Override
  public void onAdded() {
  }

  @Override
  public void onPushSend(MasterSecret masterSecret)
      throws MmsException, IOException, NoSuchMessageException
  {
    MmsDatabase          database = DatabaseFactory.getMmsDatabase(context);
    OutgoingMediaMessage message  = database.getOutgoingMessage(masterSecret, messageId);

    try {
      deliver(masterSecret, message, filterAddress == null ? null : Address.fromSerialized(filterAddress));

      database.markAsSent(messageId, true);
      markAttachmentsUploaded(messageId, message.getAttachments());

      if (message.getExpiresIn() > 0 && !message.isExpirationUpdate()) {
        database.markExpireStarted(messageId);
        ApplicationContext.getInstance(context)
                          .getExpiringMessageManager()
                          .scheduleDeletion(messageId, true, message.getExpiresIn());
      }
    } catch (InvalidNumberException | RecipientFormattingException | UndeliverableMessageException e) {
      Log.w(TAG, e);
      database.markAsSentFailed(messageId);
      notifyMediaMessageDeliveryFailed(context, messageId);
    } catch (EncapsulatedExceptions e) {
      Log.w(TAG, e);
      List<NetworkFailure> failures = new LinkedList<>();

      for (NetworkFailureException nfe : e.getNetworkExceptions()) {
        failures.add(new NetworkFailure(Address.fromSerialized(nfe.getE164number())));
      }

      for (UntrustedIdentityException uie : e.getUntrustedIdentityExceptions()) {
        database.addMismatchedIdentity(messageId, Address.fromSerialized(uie.getE164Number()), uie.getIdentityKey());
      }

      database.addFailures(messageId, failures);

      if (e.getNetworkExceptions().isEmpty() && e.getUntrustedIdentityExceptions().isEmpty()) {
        database.markAsSent(messageId, true);
        markAttachmentsUploaded(messageId, message.getAttachments());
      } else {
        database.markAsSentFailed(messageId);
        notifyMediaMessageDeliveryFailed(context, messageId);
      }
    }
  }

  @Override
  public boolean onShouldRetryThrowable(Exception exception) {
    if (exception instanceof IOException) return true;
    return false;
  }

  @Override
  public void onCanceled() {
    DatabaseFactory.getMmsDatabase(context).markAsSentFailed(messageId);
  }

  private void deliver(MasterSecret masterSecret, OutgoingMediaMessage message, @Nullable Address filterAddress)
      throws IOException, RecipientFormattingException, InvalidNumberException,
      EncapsulatedExceptions, UndeliverableMessageException
  {
    String                        groupId           = message.getRecipient().getAddress().toGroupString();
    Optional<byte[]>              profileKey        = getProfileKey(message.getRecipient());
    List<Address>                 recipients        = getGroupMessageRecipients(groupId, messageId);
    MediaConstraints              mediaConstraints  = MediaConstraints.getPushMediaConstraints();
    List<Attachment>              scaledAttachments = scaleAttachments(masterSecret, mediaConstraints, message.getAttachments());
    List<openchatServiceAttachment> attachmentStreams = getAttachmentsFor(masterSecret, scaledAttachments);

    List<openchatServiceAddress>    addresses;

    if (filterAddress != null) addresses = getPushAddresses(filterAddress);
    else                       addresses = getPushAddresses(recipients);

    if (message.isGroup()) {
      OutgoingGroupMediaMessage groupMessage     = (OutgoingGroupMediaMessage) message;
      GroupContext              groupContext     = groupMessage.getGroupContext();
      openchatServiceAttachment   avatar           = attachmentStreams.isEmpty() ? null : attachmentStreams.get(0);
      openchatServiceGroup.Type   type             = groupMessage.isGroupQuit() ? openchatServiceGroup.Type.QUIT : openchatServiceGroup.Type.UPDATE;
      openchatServiceGroup        group            = new openchatServiceGroup(type, GroupUtil.getDecodedId(groupId), groupContext.getName(), groupContext.getMembersList(), avatar);
      openchatServiceDataMessage  groupDataMessage = openchatServiceDataMessage.newBuilder()
                                                                           .withTimestamp(message.getSentTimeMillis())
                                                                           .asGroupMessage(group)
                                                                           .build();

      messageSender.sendMessage(addresses, groupDataMessage);
    } else {
      openchatServiceGroup       group        = new openchatServiceGroup(GroupUtil.getDecodedId(groupId));
      openchatServiceDataMessage groupMessage = openchatServiceDataMessage.newBuilder()
                                                                      .withTimestamp(message.getSentTimeMillis())
                                                                      .asGroupMessage(group)
                                                                      .withAttachments(attachmentStreams)
                                                                      .withBody(message.getBody())
                                                                      .withExpiration((int)(message.getExpiresIn() / 1000))
                                                                      .asExpirationUpdate(message.isExpirationUpdate())
                                                                      .withProfileKey(profileKey.orNull())
                                                                      .build();

      messageSender.sendMessage(addresses, groupMessage);
    }
  }

  private List<openchatServiceAddress> getPushAddresses(Address address) {
    List<openchatServiceAddress> addresses = new LinkedList<>();
    addresses.add(getPushAddress(address));
    return addresses;
  }

  private List<openchatServiceAddress> getPushAddresses(List<Address> addresses) {
    return Stream.of(addresses).map(this::getPushAddress).toList();
  }

  private @NonNull List<Address> getGroupMessageRecipients(String groupId, long messageId) {
    List<GroupReceiptInfo> destinations = DatabaseFactory.getGroupReceiptDatabase(context).getGroupReceiptInfo(messageId);
    if (!destinations.isEmpty()) return Stream.of(destinations).map(GroupReceiptInfo::getAddress).toList();

    List<Recipient> members = DatabaseFactory.getGroupDatabase(context).getGroupMembers(groupId, false);
    return Stream.of(members).map(Recipient::getAddress).toList();
  }
}
