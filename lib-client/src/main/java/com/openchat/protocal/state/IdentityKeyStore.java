package com.openchat.protocal.state;

import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.IdentityKeyPair;
import com.openchat.protocal.OpenchatProtocolAddress;


public interface IdentityKeyStore {

  public enum Direction {
    SENDING, RECEIVING
  }

  
  public IdentityKeyPair getIdentityKeyPair();

  
  public int             getLocalRegistrationId();

  
  public boolean         saveIdentity(OpenchatProtocolAddress address, IdentityKey identityKey);


  
  public boolean         isTrustedIdentity(OpenchatProtocolAddress address, IdentityKey identityKey, Direction direction);

}
