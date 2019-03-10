package com.openchat.secureim.jobs;

import android.content.Context;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.util.ParcelUtil;
import com.openchat.jobqueue.EncryptionKeys;
import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.requirements.NetworkRequirement;

public class SmsSendJob extends ContextJob {

  private transient MasterSecret masterSecret;

  private final long messageId;

  public SmsSendJob(Context context, MasterSecret masterSecret, long messageId, String name) {
    super(context, JobParameters.newBuilder()
                                .withPersistence()
                                .withEncryption(new EncryptionKeys(ParcelUtil.serialize(masterSecret)))
                                .withGroupId(name)
                                .create());

    this.messageId = messageId;
  }

  @Override
  public void onAdded() {

  }

  @Override
  public void onRun() {
    MasterSecret masterSecret = ParcelUtil.deserialize(getEncryptionKeys().getEncoded(), MasterSecret.CREATOR);

  }

  @Override
  public void onCanceled() {

  }

  @Override
  public boolean onShouldRetry(Throwable throwable) {
    return false;
  }
}
