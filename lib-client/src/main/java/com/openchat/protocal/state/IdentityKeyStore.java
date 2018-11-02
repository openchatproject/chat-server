package com.openchat.protocal.state;

import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.IdentityKeyPair;
import com.openchat.protocal.OpenchatProtocolAddress;


public interface IdentityKeyStore {

  
  public IdentityKeyPair getIdentityKeyPair();

  
  public int             getLocalRegistrationId();

  
  public void            saveIdentity(OpenchatProtocolAddress address, IdentityKey identityKey);


  
  public boolean         isTrustedIdentity(OpenchatProtocolAddress address, IdentityKey identityKey);

}
