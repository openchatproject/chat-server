package com.openchat.secureim.dependencies;

import android.content.Context;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.storage.OpenchatServiceOpenchatStore;
import com.openchat.secureim.jobs.CleanPreKeysJob;
import com.openchat.protocal.state.SignedPreKeyStore;

import dagger.Module;
import dagger.Provides;

@Module (complete = false, injects = {CleanPreKeysJob.class})
public class OpenchatStorageModule {

  private final Context context;

  public OpenchatStorageModule(Context context) {
    this.context = context;
  }

  @Provides SignedPreKeyStoreFactory provideSignedPreKeyStoreFactory() {
    return new SignedPreKeyStoreFactory() {
      @Override
      public SignedPreKeyStore create(MasterSecret masterSecret) {
        return new OpenchatServiceOpenchatStore(context, masterSecret);
      }
    };
  }

  public static interface SignedPreKeyStoreFactory {
    public SignedPreKeyStore create(MasterSecret masterSecret);
  }
}
