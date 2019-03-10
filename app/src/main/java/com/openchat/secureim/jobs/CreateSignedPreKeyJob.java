package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.crypto.IdentityKeyUtil;
import com.openchat.secureim.push.PushServiceSocketFactory;
import com.openchat.secureim.util.ParcelUtil;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.jobqueue.EncryptionKeys;
import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.requirements.NetworkRequirement;
import com.openchat.protocal.IdentityKeyPair;
import com.openchat.protocal.state.SignedPreKeyRecord;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.PreKeyUtil;
import com.openchat.imservice.push.PushServiceSocket;

import java.io.IOException;

public class CreateSignedPreKeyJob extends ContextJob {

  private static final String TAG = CreateSignedPreKeyJob.class.getSimpleName();

  public CreateSignedPreKeyJob(Context context, MasterSecret masterSecret) {
    super(context, JobParameters.newBuilder()
                                .withPersistence()
                                .withRequirement(new NetworkRequirement(context))
                                .withEncryption(new EncryptionKeys(ParcelUtil.serialize(masterSecret)))
                                .withGroupId(CreateSignedPreKeyJob.class.getSimpleName())
                                .create());
  }

  @Override
  public void onAdded() {}

  @Override
  public void onRun() throws Throwable {
    MasterSecret masterSecret = ParcelUtil.deserialize(getEncryptionKeys().getEncoded(), MasterSecret.CREATOR);

    if (OpenchatServicePreferences.isSignedPreKeyRegistered(context)) {
      Log.w(TAG, "Signed prekey already registered...");
      return;
    }

    IdentityKeyPair    identityKeyPair    = IdentityKeyUtil.getIdentityKeyPair(context, masterSecret);
    SignedPreKeyRecord signedPreKeyRecord = PreKeyUtil.generateSignedPreKey(context, masterSecret, identityKeyPair);
    PushServiceSocket  socket             = PushServiceSocketFactory.create(context);

    socket.setCurrentSignedPreKey(signedPreKeyRecord);
    OpenchatServicePreferences.setSignedPreKeyRegistered(context, true);
  }

  @Override
  public void onCanceled() {}

  @Override
  public boolean onShouldRetry(Throwable throwable) {
    if (throwable instanceof IOException) {
      return true;
    }

    Log.w(TAG, throwable);
    return false;
  }
}
