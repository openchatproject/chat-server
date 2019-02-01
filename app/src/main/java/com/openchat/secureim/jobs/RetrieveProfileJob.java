package com.openchat.secureim.jobs;


import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.openchat.secureim.ApplicationContext;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.dependencies.InjectableType;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.service.MessageRetrievalService;
import com.openchat.secureim.util.Base64;
import com.openchat.secureim.util.IdentityUtil;
import com.openchat.secureim.util.Util;
import com.openchat.jobqueue.JobParameters;
import com.openchat.libim.IdentityKey;
import com.openchat.libim.InvalidKeyException;
import com.openchat.imservice.api.openchatServiceMessagePipe;
import com.openchat.imservice.api.openchatServiceMessageReceiver;
import com.openchat.imservice.api.crypto.ProfileCipher;
import com.openchat.imservice.api.profiles.openchatServiceProfile;
import com.openchat.imservice.api.push.openchatServiceAddress;
import com.openchat.imservice.api.util.InvalidNumberException;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

public class RetrieveProfileJob extends ContextJob implements InjectableType {

  private static final String TAG = RetrieveProfileJob.class.getSimpleName();

  @Inject transient openchatServiceMessageReceiver receiver;

  private final Recipient recipient;

  public RetrieveProfileJob(Context context, Recipient recipient) {
    super(context, JobParameters.newBuilder()
                                .withRetryCount(3)
                                .create());

    this.recipient = recipient;
  }

  @Override
  public void onAdded() {}

  @Override
  public void onRun() throws IOException, InvalidKeyException {
    try {
      if (recipient.isGroupRecipient()) handleGroupRecipient(recipient);
      else                              handleIndividualRecipient(recipient);
    } catch (InvalidNumberException e) {
      Log.w(TAG, e);
    }
  }

  @Override
  public boolean onShouldRetry(Exception e) {
    return false;
  }

  @Override
  public void onCanceled() {}

  private void handleIndividualRecipient(Recipient recipient)
      throws IOException, InvalidKeyException, InvalidNumberException
  {
    String               number  = recipient.getAddress().toPhoneString();
    openchatServiceProfile profile = retrieveProfile(number);

    setIdentityKey(recipient, profile.getIdentityKey());
    setProfileName(recipient, profile.getName());
    setProfileAvatar(recipient, profile.getAvatar());
  }

  private void handleGroupRecipient(Recipient group)
      throws IOException, InvalidKeyException, InvalidNumberException
  {
    List<Recipient> recipients = DatabaseFactory.getGroupDatabase(context).getGroupMembers(group.getAddress().toGroupString(), false);

    for (Recipient recipient : recipients) {
      handleIndividualRecipient(recipient);
    }
  }

  private openchatServiceProfile retrieveProfile(@NonNull String number) throws IOException {
    openchatServiceMessagePipe pipe = MessageRetrievalService.getPipe();

    if (pipe != null) {
      try {
        return pipe.getProfile(new openchatServiceAddress(number));
      } catch (IOException e) {
        Log.w(TAG, e);
      }
    }

    return receiver.retrieveProfile(new openchatServiceAddress(number));
  }

  private void setIdentityKey(Recipient recipient, String identityKeyValue) {
    try {
      if (TextUtils.isEmpty(identityKeyValue)) {
        Log.w(TAG, "Identity key is missing on profile!");
        return;
      }

      IdentityKey identityKey = new IdentityKey(Base64.decode(identityKeyValue), 0);

      if (!DatabaseFactory.getIdentityDatabase(context)
                          .getIdentity(recipient.getAddress())
                          .isPresent())
      {
        Log.w(TAG, "Still first use...");
        return;
      }

      IdentityUtil.saveIdentity(context, recipient.getAddress().toPhoneString(), identityKey);
    } catch (InvalidKeyException | IOException e) {
      Log.w(TAG, e);
    }
  }

  private void setProfileName(Recipient recipient, String profileName) {
    try {
      byte[] profileKey = recipient.getProfileKey();
      if (profileKey == null) return;

      String plaintextProfileName = null;

      if (profileName != null) {
        ProfileCipher profileCipher = new ProfileCipher(profileKey);
        plaintextProfileName = new String(profileCipher.decryptName(Base64.decode(profileName)));
      }

      if (!Util.equals(plaintextProfileName, recipient.getProfileName())) {
        DatabaseFactory.getRecipientDatabase(context).setProfileName(recipient, plaintextProfileName);
      }
    } catch (ProfileCipher.InvalidCiphertextException | IOException e) {
      Log.w(TAG, e);
    }
  }

  private void setProfileAvatar(Recipient recipient, String profileAvatar) {
    if (recipient.getProfileKey() == null) return;

    if (!Util.equals(profileAvatar, recipient.getProfileAvatar())) {
      ApplicationContext.getInstance(context)
                        .getJobManager()
                        .add(new RetrieveProfileAvatarJob(context, recipient, profileAvatar));
    }
  }
}
