package com.openchat.protocal.state;

import com.openchat.protocal.InvalidKeyIdException;


public interface PreKeyStore {

  
  public PreKeyRecord loadPreKey(int preKeyId) throws InvalidKeyIdException;

  
  public void         storePreKey(int preKeyId, PreKeyRecord record);

  
  public boolean      containsPreKey(int preKeyId);

  
  public void         removePreKey(int preKeyId);

}
