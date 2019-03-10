package com.openchat.secureim.crypto.storage;

import android.content.Context;

import com.openchat.secureim.crypto.IdentityKeyUtil;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.IdentityKeyPair;
import com.openchat.protocal.state.IdentityKeyStore;

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

  @Override
  public boolean isTrustedIdentity(long recipientId, IdentityKey identityKey) {
    return DatabaseFactory.getIdentityDatabase(context)
                          .isValidIdentity(masterSecret, recipientId, identityKey);
  }
}
