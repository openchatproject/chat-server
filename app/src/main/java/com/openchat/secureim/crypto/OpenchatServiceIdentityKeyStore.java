package com.openchat.secureim.crypto;

import android.content.Context;

import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.IdentityKeyPair;
import com.openchat.protocal.state.IdentityKeyStore;
import com.openchat.imservice.crypto.MasterSecret;

public class OpenchatServiceIdentityKeyStore implements IdentityKeyStore {

  private final Context      context;
  private final MasterSecret masterSecret;

  public OpenchatServiceIdentityKeyStore(Context context, MasterSecret masterSecret) {
    this.context      = context;
    this.masterSecret = masterSecret;
  }

  @Override
  public IdentityKeyPair getIdentityKeyPair() {
    return IdentityKeyUtil.getIdentityKeyPair(context, masterSecret);
  }

  @Override
  public int getLocalRegistrationId() {
    return OpenchatServicePreferences.getLocalRegistrationId(context);
  }

  @Override
  public void saveIdentity(long recipientId, IdentityKey identityKey) {
    DatabaseFactory.getIdentityDatabase(context).saveIdentity(masterSecret, recipientId, identityKey);
  }
}
