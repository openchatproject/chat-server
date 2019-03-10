package com.openchat.secureim.crypto.storage;

import android.content.Context;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.IdentityKeyPair;
import com.openchat.protocal.InvalidKeyIdException;
import com.openchat.protocal.state.OpenchatStore;
import com.openchat.protocal.state.IdentityKeyStore;
import com.openchat.protocal.state.PreKeyRecord;
import com.openchat.protocal.state.PreKeyStore;
import com.openchat.protocal.state.SessionRecord;
import com.openchat.protocal.state.SessionStore;
import com.openchat.protocal.state.SignedPreKeyRecord;
import com.openchat.protocal.state.SignedPreKeyStore;

import java.util.List;

public class OpenchatServiceOpenchatStore implements OpenchatStore {

  private final PreKeyStore       preKeyStore;
  private final SignedPreKeyStore signedPreKeyStore;
  private final IdentityKeyStore  identityKeyStore;
  private final SessionStore      sessionStore;

  public OpenchatServiceOpenchatStore(Context context, MasterSecret masterSecret) {
    this.preKeyStore       = new OpenchatServicePreKeyStore(context, masterSecret);
    this.signedPreKeyStore = new OpenchatServicePreKeyStore(context, masterSecret);
    this.identityKeyStore  = new OpenchatServiceIdentityKeyStore(context, masterSecret);
    this.sessionStore      = new OpenchatServiceSessionStore(context, masterSecret);
  }

  @Override
  public IdentityKeyPair getIdentityKeyPair() {
    return identityKeyStore.getIdentityKeyPair();
  }

  @Override
  public int getLocalRegistrationId() {
    return identityKeyStore.getLocalRegistrationId();
  }

  @Override
  public void saveIdentity(long recipientId, IdentityKey identityKey) {
    identityKeyStore.saveIdentity(recipientId, identityKey);
  }

  @Override
  public boolean isTrustedIdentity(long recipientId, IdentityKey identityKey) {
    return identityKeyStore.isTrustedIdentity(recipientId, identityKey);
  }

  @Override
  public PreKeyRecord loadPreKey(int preKeyId) throws InvalidKeyIdException {
    return preKeyStore.loadPreKey(preKeyId);
  }

  @Override
  public void storePreKey(int preKeyId, PreKeyRecord record) {
    preKeyStore.storePreKey(preKeyId, record);
  }

  @Override
  public boolean containsPreKey(int preKeyId) {
    return preKeyStore.containsPreKey(preKeyId);
  }

  @Override
  public void removePreKey(int preKeyId) {
    preKeyStore.removePreKey(preKeyId);
  }

  @Override
  public SessionRecord loadSession(long recipientId, int deviceId) {
    return sessionStore.loadSession(recipientId, deviceId);
  }

  @Override
  public List<Integer> getSubDeviceSessions(long recipientId) {
    return sessionStore.getSubDeviceSessions(recipientId);
  }

  @Override
  public void storeSession(long recipientId, int deviceId, SessionRecord record) {
    sessionStore.storeSession(recipientId, deviceId, record);
  }

  @Override
  public boolean containsSession(long recipientId, int deviceId) {
    return sessionStore.containsSession(recipientId, deviceId);
  }

  @Override
  public void deleteSession(long recipientId, int deviceId) {
    sessionStore.deleteSession(recipientId, deviceId);
  }

  @Override
  public void deleteAllSessions(long recipientId) {
    sessionStore.deleteAllSessions(recipientId);
  }

  @Override
  public SignedPreKeyRecord loadSignedPreKey(int signedPreKeyId) throws InvalidKeyIdException {
    return signedPreKeyStore.loadSignedPreKey(signedPreKeyId);
  }

  @Override
  public List<SignedPreKeyRecord> loadSignedPreKeys() {
    return signedPreKeyStore.loadSignedPreKeys();
  }

  @Override
  public void storeSignedPreKey(int signedPreKeyId, SignedPreKeyRecord record) {
    signedPreKeyStore.storeSignedPreKey(signedPreKeyId, record);
  }

  @Override
  public boolean containsSignedPreKey(int signedPreKeyId) {
    return signedPreKeyStore.containsSignedPreKey(signedPreKeyId);
  }

  @Override
  public void removeSignedPreKey(int signedPreKeyId) {
    signedPreKeyStore.removeSignedPreKey(signedPreKeyId);
  }
}
