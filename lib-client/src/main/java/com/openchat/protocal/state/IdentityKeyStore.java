package com.openchat.protocal.state;

import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.IdentityKeyPair;


public interface IdentityKeyStore {

  
  public IdentityKeyPair getIdentityKeyPair();

  
  public int             getLocalRegistrationId();

  
  public void            saveIdentity(String name, IdentityKey identityKey);


  
  public boolean         isTrustedIdentity(String name, IdentityKey identityKey);

}
