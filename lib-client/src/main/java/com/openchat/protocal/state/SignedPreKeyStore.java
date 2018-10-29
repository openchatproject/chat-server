package com.openchat.protocal.state;

import com.openchat.protocal.InvalidKeyIdException;

import java.util.List;

public interface SignedPreKeyStore {


  
  public SignedPreKeyRecord loadSignedPreKey(int signedPreKeyId) throws InvalidKeyIdException;

  
  public List<SignedPreKeyRecord> loadSignedPreKeys();

  
  public void         storeSignedPreKey(int signedPreKeyId, SignedPreKeyRecord record);

  
  public boolean      containsSignedPreKey(int signedPreKeyId);

  
  public void         removeSignedPreKey(int signedPreKeyId);

}
