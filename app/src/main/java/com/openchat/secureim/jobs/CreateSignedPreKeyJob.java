package com.openchat.secureim.jobs;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.crypto.IdentityKeyUtil;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.PreKeyUtil;
import com.openchat.secureim.dependencies.InjectableType;
import com.openchat.secureim.jobs.requirements.MasterSecretRequirement;
import com.openchat.secureim.util.TextSecurePreferences;
import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.requirements.NetworkRequirement;
import com.openchat.libim.IdentityKeyPair;
import com.openchat.libim.state.SignedPreKeyRecord;
import com.openchat.imservice.api.openchatServiceAccountManager;
import com.openchat.imservice.api.push.exceptions.PushNetworkException;

import java.io.IOException;

import javax.inject.Inject;

public class CreateSignedPreKeyJob extends MasterSecretJob implements InjectableType {

  private static final long serialVersionUID = 1L;

  private static final String TAG = CreateSignedPreKeyJob.class.getSimpleName();

  @Inject transient openchatServiceAccountManager accountManager;

  public CreateSignedPreKeyJob(Context context) {
    super(context, JobParameters.newBuilder()
                                .withPersistence()
                                .withRequirement(new NetworkRequirement(context))
                                .withRequirement(new MasterSecretRequirement(context))
                                .withGroupId(CreateSignedPreKeyJob.class.getSimpleName())
                                .create());
  }

  @Override
  public void onAdded() {}

  @Override
  public void onRun(MasterSecret masterSecret) throws IOException {
    if (TextSecurePreferences.isSignedPreKeyRegistered(context)) {
      Log.w(TAG, "Signed prekey already registered...");
      return;
    }

    if (!TextSecurePreferences.isPushRegistered(context)) {
      Log.w(TAG, "Not yet registered...");
      return;
    }

    IdentityKeyPair    identityKeyPair    = IdentityKeyUtil.getIdentityKeyPair(context);
    SignedPreKeyRecord signedPreKeyRecord = PreKeyUtil.generateSignedPreKey(context, identityKeyPair, true);

    accountManager.setSignedPreKey(signedPreKeyRecord);
    TextSecurePreferences.setSignedPreKeyRegistered(context, true);
  }

  @Override
  public void onCanceled() {}

  @Override
  public boolean onShouldRetryThrowable(Exception exception) {
    if (exception instanceof PushNetworkException) return true;
    return false;
  }
}
