package com.openchat.secureim.jobs;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.Address;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.GroupDatabase;
import com.openchat.secureim.dependencies.InjectableType;
import com.openchat.secureim.jobs.requirements.MasterSecretRequirement;
import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.requirements.NetworkRequirement;
import com.openchat.libim.util.guava.Optional;
import com.openchat.imservice.api.openchatServiceMessageSender;
import com.openchat.imservice.api.crypto.UntrustedIdentityException;
import com.openchat.imservice.api.messages.openchatServiceAttachment;
import com.openchat.imservice.api.messages.openchatServiceAttachmentStream;
import com.openchat.imservice.api.messages.multidevice.DeviceGroup;
import com.openchat.imservice.api.messages.multidevice.DeviceGroupsOutputStream;
import com.openchat.imservice.api.messages.multidevice.openchatServiceSyncMessage;
import com.openchat.imservice.api.push.exceptions.PushNetworkException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

public class MultiDeviceGroupUpdateJob extends MasterSecretJob implements InjectableType {

  private static final long serialVersionUID = 1L;
  private static final String TAG = MultiDeviceGroupUpdateJob.class.getSimpleName();

  @Inject transient openchatServiceMessageSender messageSender;

  public MultiDeviceGroupUpdateJob(Context context) {
    super(context, JobParameters.newBuilder()
                                .withRequirement(new NetworkRequirement(context))
                                .withRequirement(new MasterSecretRequirement(context))
                                .withGroupId(MultiDeviceGroupUpdateJob.class.getSimpleName())
                                .withPersistence()
                                .create());
  }

  @Override
  public void onRun(MasterSecret masterSecret) throws Exception {
    File                 contactDataFile = createTempFile("multidevice-contact-update");
    GroupDatabase.Reader reader          = null;

    GroupDatabase.GroupRecord record;

    try {
      DeviceGroupsOutputStream out = new DeviceGroupsOutputStream(new FileOutputStream(contactDataFile));

      reader = DatabaseFactory.getGroupDatabase(context).getGroups();

      while ((record = reader.getNext()) != null) {
        if (!record.isMms()) {
          List<String> members = new LinkedList<>();

          for (Address member : record.getMembers()) {
            members.add(member.serialize());
          }

          out.write(new DeviceGroup(record.getId(), Optional.fromNullable(record.getTitle()),
                                    members, getAvatar(record.getAvatar()),
                                    record.isActive()));
        }
      }

      out.close();

      if (contactDataFile.exists() && contactDataFile.length() > 0) {
        sendUpdate(messageSender, contactDataFile);
      } else {
        Log.w(TAG, "No groups present for sync message...");
      }

    } finally {
      if (contactDataFile != null) contactDataFile.delete();
      if (reader != null)          reader.close();
    }

  }

  @Override
  public boolean onShouldRetryThrowable(Exception exception) {
    if (exception instanceof PushNetworkException) return true;
    return false;
  }

  @Override
  public void onAdded() {

  }

  @Override
  public void onCanceled() {

  }

  private void sendUpdate(openchatServiceMessageSender messageSender, File contactsFile)
      throws IOException, UntrustedIdentityException
  {
    FileInputStream               contactsFileStream = new FileInputStream(contactsFile);
    openchatServiceAttachmentStream attachmentStream   = openchatServiceAttachment.newStreamBuilder()
                                                                              .withStream(contactsFileStream)
                                                                              .withContentType("application/octet-stream")
                                                                              .withLength(contactsFile.length())
                                                                              .build();

    messageSender.sendMessage(openchatServiceSyncMessage.forGroups(attachmentStream));
  }


  private Optional<openchatServiceAttachmentStream> getAvatar(@Nullable byte[] avatar) {
    if (avatar == null) return Optional.absent();

    return Optional.of(openchatServiceAttachment.newStreamBuilder()
                                              .withStream(new ByteArrayInputStream(avatar))
                                              .withContentType("image/*")
                                              .withLength(avatar.length)
                                              .build());
  }

  private File createTempFile(String prefix) throws IOException {
    File file = File.createTempFile(prefix, "tmp", context.getCacheDir());
    file.deleteOnExit();

    return file;
  }


}
