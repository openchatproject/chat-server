package com.openchat.protocal.state.impl;

import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.IdentityKeyPair;
import com.openchat.protocal.ecc.Curve;
import com.openchat.protocal.ecc.ECKeyPair;
import com.openchat.protocal.state.IdentityKeyStore;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class InMemoryIdentityKeyStore implements IdentityKeyStore {

  private final Map<String, IdentityKey> trustedKeys = new HashMap<>();

  private final IdentityKeyPair identityKeyPair;
  private final int             localRegistrationId;

  public InMemoryIdentityKeyStore(IdentityKeyPair identityKeyPair, int localRegistrationId) {
    this.identityKeyPair     = identityKeyPair;
    this.localRegistrationId = localRegistrationId;
  }

  @Override
  public IdentityKeyPair getIdentityKeyPair() {
    return identityKeyPair;
  }

  @Override
  public int getLocalRegistrationId() {
    return localRegistrationId;
  }

  @Override
  public void saveIdentity(String name, IdentityKey identityKey) {
    trustedKeys.put(name, identityKey);
  }

  @Override
  public boolean isTrustedIdentity(String name, IdentityKey identityKey) {
    IdentityKey trusted = trustedKeys.get(name);
    return (trusted == null || trusted.equals(identityKey));
  }
}
