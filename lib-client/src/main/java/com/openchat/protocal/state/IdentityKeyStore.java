package com.openchat.protocal.state;

import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.IdentityKeyPair;


public interface IdentityKeyStore {

  
  public IdentityKeyPair getIdentityKeyPair();

  
  public int             getLocalRegistrationId();

  
  public void            saveIdentity(long recipientId, IdentityKey identityKey);


  
  public boolean         isTrustedIdentity(long recipientId, IdentityKey identityKey);

}
