package com.openchat.secureim;

import com.openchat.imservice.crypto.MasterSecret;

public interface PassphraseRequiredActivity {
  public void onMasterSecretCleared();
  public void onNewMasterSecret(MasterSecret masterSecret);
}
