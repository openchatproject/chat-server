package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.crypto.IdentityKeyUtil;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.PreKeyUtil;
import com.openchat.secureim.jobs.requirements.MasterSecretRequirement;
import com.openchat.secureim.push.OpenchatServiceCommunicationFactory;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.requirements.NetworkRequirement;
import com.openchat.protocal.IdentityKeyPair;
import com.openchat.protocal.state.PreKeyRecord;
import com.openchat.protocal.state.SignedPreKeyRecord;
import com.openchat.imservice.api.OpenchatServiceAccountManager;
import com.openchat.imservice.push.PushServiceSocket;
import com.openchat.imservice.push.exceptions.NonSuccessfulResponseCodeException;
import com.openchat.imservice.push.exceptions.PushNetworkException;

import java.io.IOException;
import java.util.List;

public class RefreshPreKeysJob extends MasterSecretJob {

  private static final String TAG = RefreshPreKeysJob.class.getSimpleName();

  private static final int PREKEY_MINIMUM = 10;

  public RefreshPreKeysJob(Context context) {
    super(context, JobParameters.newBuilder()
                                .withGroupId(RefreshPreKeysJob.class.getSimpleName())
                                .withRequirement(new NetworkRequirement(context))
                                .withRequirement(new MasterSecretRequirement(context))
                                .withRetryCount(5)
                                .create());
  }

  @Override
  public void onAdded() {

  }

  @Override
  public void onRun() throws RequirementNotMetException, IOException {
    if (!OpenchatServicePreferences.isPushRegistered(context)) return;

    MasterSecret             masterSecret   = getMasterSecret();
    OpenchatServiceAccountManager accountManager = OpenchatServiceCommunicationFactory.createManager(context);
    int                      availableKeys  = accountManager.getPreKeysCount();

    if (availableKeys >= PREKEY_MINIMUM && OpenchatServicePreferences.isSignedPreKeyRegistered(context)) {
      Log.w(TAG, "Available keys sufficient: " + availableKeys);
      return;
    }

    List<PreKeyRecord> preKeyRecords       = PreKeyUtil.generatePreKeys(context, masterSecret);
    PreKeyRecord       lastResortKeyRecord = PreKeyUtil.generateLastResortKey(context, masterSecret);
    IdentityKeyPair    identityKey         = IdentityKeyUtil.getIdentityKeyPair(context, masterSecret);
    SignedPreKeyRecord signedPreKeyRecord  = PreKeyUtil.generateSignedPreKey(context, masterSecret, identityKey);

    Log.w(TAG, "Registering new prekeys...");

    accountManager.setPreKeys(identityKey.getPublicKey(), lastResortKeyRecord, signedPreKeyRecord, preKeyRecords);

    OpenchatServicePreferences.setSignedPreKeyRegistered(context, true);
  }

  @Override
  public boolean onShouldRetry(Throwable throwable) {
    if (throwable instanceof RequirementNotMetException)         return true;
    if (throwable instanceof NonSuccessfulResponseCodeException) return false;
    if (throwable instanceof PushNetworkException)               return true;

    return false;
  }

  @Override
  public void onCanceled() {

  }

}
