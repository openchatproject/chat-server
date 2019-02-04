package com.openchat.secureim.jobs;


import android.content.Context;
import android.util.Log;

import com.openchat.secureim.database.Address;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.GroupDatabase;
import com.openchat.secureim.database.GroupDatabase.GroupRecord;
import com.openchat.secureim.dependencies.InjectableType;
import com.openchat.secureim.util.GroupUtil;
import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.requirements.NetworkRequirement;
import com.openchat.libim.util.guava.Optional;
import com.openchat.imservice.api.openchatServiceMessageSender;
import com.openchat.imservice.api.crypto.UntrustedIdentityException;
import com.openchat.imservice.api.messages.openchatServiceAttachment;
import com.openchat.imservice.api.messages.openchatServiceAttachmentStream;
import com.openchat.imservice.api.messages.openchatServiceDataMessage;
import com.openchat.imservice.api.messages.openchatServiceGroup;
import com.openchat.imservice.api.messages.openchatServiceGroup.Type;
import com.openchat.imservice.api.push.openchatServiceAddress;
import com.openchat.imservice.api.push.exceptions.PushNetworkException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

public class PushGroupUpdateJob extends ContextJob implements InjectableType {

  private static final String TAG = PushGroupUpdateJob.class.getSimpleName();

  private static final long serialVersionUID = 0L;

  @Inject transient openchatServiceMessageSender messageSender;

  private final String source;
  private final byte[] groupId;


  public PushGroupUpdateJob(Context context, String source, byte[] groupId) {
    super(context, JobParameters.newBuilder()
                                .withPersistence()
                                .withRequirement(new NetworkRequirement(context))
                                .withRetryCount(50)
                                .create());

    this.source  = source;
    this.groupId = groupId;
  }

  @Override
  public void onAdded() {}

  @Override
  public void onRun() throws IOException, UntrustedIdentityException {
    GroupDatabase           groupDatabase = DatabaseFactory.getGroupDatabase(context);
    Optional<GroupRecord>   record        = groupDatabase.getGroup(GroupUtil.getEncodedId(groupId, false));
    openchatServiceAttachment avatar        = null;

    if (record == null) {
      Log.w(TAG, "No information for group record info request: " + new String(groupId));
      return;
    }

    if (record.get().getAvatar() != null) {
      avatar = openchatServiceAttachmentStream.newStreamBuilder()
                                            .withContentType("image/jpeg")
                                            .withStream(new ByteArrayInputStream(record.get().getAvatar()))
                                            .withLength(record.get().getAvatar().length)
                                            .build();
    }

    List<String> members = new LinkedList<>();

    for (Address member : record.get().getMembers()) {
      members.add(member.serialize());
    }

    openchatServiceGroup groupContext = openchatServiceGroup.newBuilder(Type.UPDATE)
                                                        .withAvatar(avatar)
                                                        .withId(groupId)
                                                        .withMembers(members)
                                                        .withName(record.get().getTitle())
                                                        .build();

    openchatServiceDataMessage message = openchatServiceDataMessage.newBuilder()
                                                               .asGroupMessage(groupContext)
                                                               .withTimestamp(System.currentTimeMillis())
                                                               .build();

    messageSender.sendMessage(new openchatServiceAddress(source), message);
  }

  @Override
  public boolean onShouldRetry(Exception e) {
    Log.w(TAG, e);
    return e instanceof PushNetworkException;
  }

  @Override
  public void onCanceled() {

  }
}
