package com.openchat.secureim.jobs;


import android.content.Context;
import android.util.Log;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.ProfileKeyUtil;
import com.openchat.secureim.dependencies.InjectableType;
import com.openchat.secureim.util.TextSecurePreferences;
import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.requirements.NetworkRequirement;
import com.openchat.libim.util.guava.Optional;
import com.openchat.imservice.api.openchatServiceMessageSender;
import com.openchat.imservice.api.crypto.UntrustedIdentityException;
import com.openchat.imservice.api.messages.openchatServiceAttachment;
import com.openchat.imservice.api.messages.openchatServiceAttachmentStream;
import com.openchat.imservice.api.messages.multidevice.ContactsMessage;
import com.openchat.imservice.api.messages.multidevice.DeviceContact;
import com.openchat.imservice.api.messages.multidevice.DeviceContactsOutputStream;
import com.openchat.imservice.api.messages.multidevice.openchatServiceSyncMessage;
import com.openchat.imservice.api.messages.multidevice.VerifiedMessage;
import com.openchat.imservice.api.push.exceptions.PushNetworkException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

public class MultiDeviceProfileKeyUpdateJob extends MasterSecretJob implements InjectableType {

  private static final long serialVersionUID = 1L;
  private static final String TAG = MultiDeviceProfileKeyUpdateJob.class.getSimpleName();

  @Inject transient openchatServiceMessageSender messageSender;

  public MultiDeviceProfileKeyUpdateJob(Context context) {
    super(context, JobParameters.newBuilder()
                                .withRequirement(new NetworkRequirement(context))
                                .withPersistence()
                                .withGroupId(MultiDeviceProfileKeyUpdateJob.class.getSimpleName())
                                .create());
  }

  @Override
  public void onRun(MasterSecret masterSecret) throws IOException, UntrustedIdentityException {
    if (!TextSecurePreferences.isMultiDevice(getContext())) {
      Log.w(TAG, "Not multi device...");
      return;
    }

    Optional<byte[]>           profileKey = Optional.of(ProfileKeyUtil.getProfileKey(getContext()));
    ByteArrayOutputStream      baos       = new ByteArrayOutputStream();
    DeviceContactsOutputStream out        = new DeviceContactsOutputStream(baos);

    out.write(new DeviceContact(TextSecurePreferences.getLocalNumber(getContext()),
                                Optional.<String>absent(),
                                Optional.<openchatServiceAttachmentStream>absent(),
                                Optional.<String>absent(),
                                Optional.<VerifiedMessage>absent(),
                                profileKey));

    out.close();

    openchatServiceAttachmentStream attachmentStream = openchatServiceAttachment.newStreamBuilder()
                                                                            .withStream(new ByteArrayInputStream(baos.toByteArray()))
                                                                            .withContentType("application/octet-stream")
                                                                            .withLength(baos.toByteArray().length)
                                                                            .build();

    openchatServiceSyncMessage      syncMessage      = openchatServiceSyncMessage.forContacts(new ContactsMessage(attachmentStream, false));

    messageSender.sendMessage(syncMessage);
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
    Log.w(TAG, "Profile key sync failed!");
  }
}
