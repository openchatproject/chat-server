package com.openchat.secureim.groups;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.protobuf.ByteString;

import com.openchat.secureim.ApplicationContext;
import com.openchat.secureim.crypto.MasterSecretUnion;
import com.openchat.secureim.database.Address;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.EncryptingSmsDatabase;
import com.openchat.secureim.database.GroupDatabase;
import com.openchat.secureim.database.MessagingDatabase.InsertResult;
import com.openchat.secureim.database.MmsDatabase;
import com.openchat.secureim.jobs.AvatarDownloadJob;
import com.openchat.secureim.jobs.PushGroupUpdateJob;
import com.openchat.secureim.mms.MmsException;
import com.openchat.secureim.mms.OutgoingGroupMediaMessage;
import com.openchat.secureim.notifications.MessageNotifier;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.sms.IncomingGroupMessage;
import com.openchat.secureim.sms.IncomingTextMessage;
import com.openchat.secureim.util.Base64;
import com.openchat.secureim.util.GroupUtil;
import com.openchat.libim.util.guava.Optional;
import com.openchat.imservice.api.messages.openchatServiceAttachment;
import com.openchat.imservice.api.messages.openchatServiceDataMessage;
import com.openchat.imservice.api.messages.openchatServiceEnvelope;
import com.openchat.imservice.api.messages.openchatServiceGroup;
import com.openchat.imservice.api.messages.openchatServiceGroup.Type;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.openchat.secureim.database.GroupDatabase.GroupRecord;
import static com.openchat.imservice.internal.push.openchatServiceProtos.AttachmentPointer;
import static com.openchat.imservice.internal.push.openchatServiceProtos.GroupContext;

public class GroupMessageProcessor {

  private static final String TAG = GroupMessageProcessor.class.getSimpleName();

  public static @Nullable Long process(@NonNull Context context,
                                       @NonNull MasterSecretUnion masterSecret,
                                       @NonNull openchatServiceEnvelope envelope,
                                       @NonNull openchatServiceDataMessage message,
                                       boolean outgoing)
  {
    if (!message.getGroupInfo().isPresent() || message.getGroupInfo().get().getGroupId() == null) {
      Log.w(TAG, "Received group message with no id! Ignoring...");
      return null;
    }

    GroupDatabase         database = DatabaseFactory.getGroupDatabase(context);
    openchatServiceGroup    group    = message.getGroupInfo().get();
    String                id       = GroupUtil.getEncodedId(group.getGroupId(), false);
    Optional<GroupRecord> record   = database.getGroup(id);

    if (record.isPresent() && group.getType() == Type.UPDATE) {
      return handleGroupUpdate(context, masterSecret, envelope, group, record.get(), outgoing);
    } else if (!record.isPresent() && group.getType() == Type.UPDATE) {
      return handleGroupCreate(context, masterSecret, envelope, group, outgoing);
    } else if (record.isPresent() && group.getType() == Type.QUIT) {
      return handleGroupLeave(context, masterSecret, envelope, group, record.get(), outgoing);
    } else if (record.isPresent() && group.getType() == Type.REQUEST_INFO) {
      return handleGroupInfoRequest(context, envelope, group, record.get());
    } else {
      Log.w(TAG, "Received unknown type, ignoring...");
      return null;
    }
  }

  private static @Nullable Long handleGroupCreate(@NonNull Context context,
                                                  @NonNull MasterSecretUnion masterSecret,
                                                  @NonNull openchatServiceEnvelope envelope,
                                                  @NonNull openchatServiceGroup group,
                                                  boolean outgoing)
  {
    GroupDatabase        database = DatabaseFactory.getGroupDatabase(context);
    String               id       = GroupUtil.getEncodedId(group.getGroupId(), false);
    GroupContext.Builder builder  = createGroupContext(group);
    builder.setType(GroupContext.Type.UPDATE);

    openchatServiceAttachment avatar  = group.getAvatar().orNull();
    List<Address>           members = group.getMembers().isPresent() ? new LinkedList<Address>() : null;

    if (group.getMembers().isPresent()) {
      for (String member : group.getMembers().get()) {
        members.add(Address.fromExternal(context, member));
      }
    }

    database.create(id, group.getName().orNull(), members,
                    avatar != null && avatar.isPointer() ? avatar.asPointer() : null,
                    envelope.getRelay());

    return storeMessage(context, masterSecret, envelope, group, builder.build(), outgoing);
  }

  private static @Nullable Long handleGroupUpdate(@NonNull Context context,
                                                  @NonNull MasterSecretUnion masterSecret,
                                                  @NonNull openchatServiceEnvelope envelope,
                                                  @NonNull openchatServiceGroup group,
                                                  @NonNull GroupRecord groupRecord,
                                                  boolean outgoing)
  {

    GroupDatabase database = DatabaseFactory.getGroupDatabase(context);
    String        id       = GroupUtil.getEncodedId(group.getGroupId(), false);

    Set<Address> recordMembers = new HashSet<>(groupRecord.getMembers());
    Set<Address> messageMembers = new HashSet<>();

    for (String messageMember : group.getMembers().get()) {
      messageMembers.add(Address.fromExternal(context, messageMember));
    }

    Set<Address> addedMembers = new HashSet<>(messageMembers);
    addedMembers.removeAll(recordMembers);

    Set<Address> missingMembers = new HashSet<>(recordMembers);
    missingMembers.removeAll(messageMembers);

    GroupContext.Builder builder = createGroupContext(group);
    builder.setType(GroupContext.Type.UPDATE);

    if (addedMembers.size() > 0) {
      Set<Address> unionMembers = new HashSet<>(recordMembers);
      unionMembers.addAll(messageMembers);
      database.updateMembers(id, new LinkedList<>(unionMembers));

      builder.clearMembers();

      for (Address addedMember : addedMembers) {
        builder.addMembers(addedMember.serialize());
      }
    } else {
      builder.clearMembers();
    }

    if (missingMembers.size() > 0) {
      // TODO We should tell added and missing about each-other.
    }

    if (group.getName().isPresent() || group.getAvatar().isPresent()) {
      openchatServiceAttachment avatar = group.getAvatar().orNull();
      database.update(id, group.getName().orNull(), avatar != null ? avatar.asPointer() : null);
    }

    if (group.getName().isPresent() && group.getName().get().equals(groupRecord.getTitle())) {
      builder.clearName();
    }

    if (!groupRecord.isActive()) database.setActive(id, true);

    return storeMessage(context, masterSecret, envelope, group, builder.build(), outgoing);
  }

  private static Long handleGroupInfoRequest(@NonNull Context context,
                                             @NonNull openchatServiceEnvelope envelope,
                                             @NonNull openchatServiceGroup group,
                                             @NonNull GroupRecord record)
  {
    if (record.getMembers().contains(Address.fromExternal(context, envelope.getSource()))) {
      ApplicationContext.getInstance(context)
                        .getJobManager()
                        .add(new PushGroupUpdateJob(context, envelope.getSource(), group.getGroupId()));
    }

    return null;
  }

  private static Long handleGroupLeave(@NonNull Context               context,
                                       @NonNull MasterSecretUnion     masterSecret,
                                       @NonNull openchatServiceEnvelope envelope,
                                       @NonNull openchatServiceGroup    group,
                                       @NonNull GroupRecord           record,
                                       boolean  outgoing)
  {
    GroupDatabase database = DatabaseFactory.getGroupDatabase(context);
    String        id       = GroupUtil.getEncodedId(group.getGroupId(), false);
    List<Address> members  = record.getMembers();

    GroupContext.Builder builder = createGroupContext(group);
    builder.setType(GroupContext.Type.QUIT);

    if (members.contains(Address.fromExternal(context, envelope.getSource()))) {
      database.remove(id, Address.fromExternal(context, envelope.getSource()));
      if (outgoing) database.setActive(id, false);

      return storeMessage(context, masterSecret, envelope, group, builder.build(), outgoing);
    }

    return null;
  }


  private static @Nullable Long storeMessage(@NonNull Context context,
                                             @NonNull MasterSecretUnion masterSecret,
                                             @NonNull openchatServiceEnvelope envelope,
                                             @NonNull openchatServiceGroup group,
                                             @NonNull GroupContext storage,
                                             boolean  outgoing)
  {
    if (group.getAvatar().isPresent()) {
      ApplicationContext.getInstance(context).getJobManager()
                        .add(new AvatarDownloadJob(context, group.getGroupId()));
    }

    try {
      if (outgoing) {
        MmsDatabase               mmsDatabase     = DatabaseFactory.getMmsDatabase(context);
        Address                   addres          = Address.fromExternal(context, GroupUtil.getEncodedId(group.getGroupId(), false));
        Recipient                 recipient       = Recipient.from(context, addres, false);
        OutgoingGroupMediaMessage outgoingMessage = new OutgoingGroupMediaMessage(recipient, storage, null, envelope.getTimestamp(), 0);
        long                      threadId        = DatabaseFactory.getThreadDatabase(context).getThreadIdFor(recipient);
        long                      messageId       = mmsDatabase.insertMessageOutbox(masterSecret, outgoingMessage, threadId, false, null);

        mmsDatabase.markAsSent(messageId, true);

        return threadId;
      } else {
        EncryptingSmsDatabase smsDatabase  = DatabaseFactory.getEncryptingSmsDatabase(context);
        String                body         = Base64.encodeBytes(storage.toByteArray());
        IncomingTextMessage   incoming     = new IncomingTextMessage(Address.fromExternal(context, envelope.getSource()), envelope.getSourceDevice(), envelope.getTimestamp(), body, Optional.of(group), 0);
        IncomingGroupMessage  groupMessage = new IncomingGroupMessage(incoming, storage, body);

        Optional<InsertResult> insertResult = smsDatabase.insertMessageInbox(masterSecret, groupMessage);

        if (insertResult.isPresent()) {
          MessageNotifier.updateNotification(context, masterSecret.getMasterSecret().orNull(), insertResult.get().getThreadId());
          return insertResult.get().getThreadId();
        } else {
          return null;
        }
      }
    } catch (MmsException e) {
      Log.w(TAG, e);
    }

    return null;
  }

  private static GroupContext.Builder createGroupContext(openchatServiceGroup group) {
    GroupContext.Builder builder = GroupContext.newBuilder();
    builder.setId(ByteString.copyFrom(group.getGroupId()));

    if (group.getAvatar().isPresent() && group.getAvatar().get().isPointer()) {
      builder.setAvatar(AttachmentPointer.newBuilder()
                                         .setId(group.getAvatar().get().asPointer().getId())
                                         .setKey(ByteString.copyFrom(group.getAvatar().get().asPointer().getKey()))
                                         .setContentType(group.getAvatar().get().getContentType()));
    }

    if (group.getName().isPresent()) {
      builder.setName(group.getName().get());
    }

    if (group.getMembers().isPresent()) {
      builder.addAllMembers(group.getMembers().get());
    }

    return builder;
  }

}
