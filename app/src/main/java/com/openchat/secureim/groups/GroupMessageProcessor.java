package com.openchat.secureim.groups;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.google.protobuf.ByteString;

import com.openchat.secureim.ApplicationContext;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.EncryptingSmsDatabase;
import com.openchat.secureim.database.GroupDatabase;
import com.openchat.secureim.jobs.AvatarDownloadJob;
import com.openchat.secureim.notifications.MessageNotifier;
import com.openchat.secureim.sms.IncomingGroupMessage;
import com.openchat.secureim.sms.IncomingTextMessage;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.messages.OpenchatServiceAttachment;
import com.openchat.imservice.api.messages.OpenchatServiceGroup;
import com.openchat.imservice.api.messages.OpenchatServiceMessage;
import com.openchat.imservice.push.IncomingPushMessage;
import com.openchat.imservice.util.Base64;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.openchat.secureim.database.GroupDatabase.GroupRecord;
import static com.openchat.imservice.push.PushMessageProtos.PushMessageContent.AttachmentPointer;
import static com.openchat.imservice.push.PushMessageProtos.PushMessageContent.GroupContext;

public class GroupMessageProcessor {

  private static final String TAG = GroupMessageProcessor.class.getSimpleName();

  public static void process(Context context,
                             MasterSecret masterSecret,
                             IncomingPushMessage push,
                             OpenchatServiceMessage message)
  {
    if (!message.getGroupInfo().isPresent() || message.getGroupInfo().get().getGroupId() == null) {
      Log.w(TAG, "Received group message with no id! Ignoring...");
      return;
    }

    if (!message.isSecure()) {
      Log.w(TAG, "Received insecure group push action! Ignoring...");
      return;
    }

    GroupDatabase database = DatabaseFactory.getGroupDatabase(context);
    OpenchatServiceGroup group    = message.getGroupInfo().get();
    byte[]        id       = group.getGroupId();
    GroupRecord   record   = database.getGroup(id);

    if (record != null && group.getType() == OpenchatServiceGroup.Type.UPDATE) {
      handleGroupUpdate(context, masterSecret, push, group, record);
    } else if (record == null && group.getType() == OpenchatServiceGroup.Type.UPDATE) {
      handleGroupCreate(context, masterSecret, push, group);
    } else if (record != null && group.getType() == OpenchatServiceGroup.Type.QUIT) {
      handleGroupLeave(context, masterSecret, push, group, record);
    } else {
      Log.w(TAG, "Received unknown type, ignoring...");
    }
  }

  private static void handleGroupCreate(Context context,
                                        MasterSecret masterSecret,
                                        IncomingPushMessage message,
                                        OpenchatServiceGroup group)
  {
    GroupDatabase        database = DatabaseFactory.getGroupDatabase(context);
    byte[]               id       = group.getGroupId();
    GroupContext.Builder builder  = createGroupContext(group);
    builder.setType(GroupContext.Type.UPDATE);

    OpenchatServiceAttachment avatar = group.getAvatar().orNull();

    database.create(id, group.getName().orNull(), group.getMembers().orNull(),
                    avatar != null && avatar.isPointer() ? avatar.asPointer() : null,
                    message.getRelay());

    storeMessage(context, masterSecret, message, group, builder.build());
  }

  private static void handleGroupUpdate(Context context,
                                        MasterSecret masterSecret,
                                        IncomingPushMessage push,
                                        OpenchatServiceGroup group,
                                        GroupRecord groupRecord)
  {

    GroupDatabase database = DatabaseFactory.getGroupDatabase(context);
    byte[]        id       = group.getGroupId();

    Set<String> recordMembers = new HashSet<>(groupRecord.getMembers());
    Set<String> messageMembers = new HashSet<>(group.getMembers().get());

    Set<String> addedMembers = new HashSet<>(messageMembers);
    addedMembers.removeAll(recordMembers);

    Set<String> missingMembers = new HashSet<>(recordMembers);
    missingMembers.removeAll(messageMembers);

    GroupContext.Builder builder = createGroupContext(group);
    builder.setType(GroupContext.Type.UPDATE);

    if (addedMembers.size() > 0) {
      Set<String> unionMembers = new HashSet<>(recordMembers);
      unionMembers.addAll(messageMembers);
      database.updateMembers(id, new LinkedList<>(unionMembers));

      builder.clearMembers().addAllMembers(addedMembers);
    } else {
      builder.clearMembers();
    }

    if (missingMembers.size() > 0) {
    }

    if (group.getName().isPresent() || group.getAvatar().isPresent()) {
      OpenchatServiceAttachment avatar = group.getAvatar().orNull();
      database.update(id, group.getName().orNull(), avatar != null ? avatar.asPointer() : null);
    }

    if (group.getName().isPresent() && group.getName().get().equals(groupRecord.getTitle())) {
      builder.clearName();
    }

    if (!groupRecord.isActive()) database.setActive(id, true);

    storeMessage(context, masterSecret, push, group, builder.build());
  }

  private static void handleGroupLeave(Context             context,
                                       MasterSecret        masterSecret,
                                       IncomingPushMessage message,
                                       OpenchatServiceGroup     group,
                                       GroupRecord         record)
  {
    GroupDatabase database = DatabaseFactory.getGroupDatabase(context);
    byte[]        id       = group.getGroupId();
    List<String>  members  = record.getMembers();

    GroupContext.Builder builder = createGroupContext(group);
    builder.setType(GroupContext.Type.QUIT);

    if (members.contains(message.getSource())) {
      database.remove(id, message.getSource());

      storeMessage(context, masterSecret, message, group, builder.build());
    }
  }

  private static void storeMessage(Context context, MasterSecret masterSecret,
                                   IncomingPushMessage push, OpenchatServiceGroup group,
                                   GroupContext storage)
  {
    if (group.getAvatar().isPresent()) {
      ApplicationContext.getInstance(context).getJobManager()
                        .add(new AvatarDownloadJob(context, group.getGroupId()));
    }

    EncryptingSmsDatabase smsDatabase  = DatabaseFactory.getEncryptingSmsDatabase(context);
    String                body         = Base64.encodeBytes(storage.toByteArray());
    IncomingTextMessage   incoming     = new IncomingTextMessage(push.getSource(), push.getSourceDevice(), push.getTimestampMillis(), body, Optional.of(group));
    IncomingGroupMessage  groupMessage = new IncomingGroupMessage(incoming, storage, body);

    Pair<Long, Long> messageAndThreadId = smsDatabase.insertMessageInbox(masterSecret, groupMessage);
    MessageNotifier.updateNotification(context, masterSecret, messageAndThreadId.second);
  }

  private static GroupContext.Builder createGroupContext(OpenchatServiceGroup group) {
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
