package com.openchat.secureim.crypto.protocol;

import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.InvalidMessageException;
import com.openchat.protocal.InvalidVersionException;
import com.openchat.protocal.LegacyMessageException;

public abstract class KeyExchangeMessage {
  public abstract IdentityKey getIdentityKey();
  public abstract boolean     hasIdentityKey();
  public abstract int         getMaxVersion();
  public abstract int         getVersion();

  public static KeyExchangeMessage createFor(String rawMessage)
      throws InvalidMessageException, InvalidKeyException, InvalidVersionException, LegacyMessageException
  {
    return new KeyExchangeMessageV2(rawMessage);
  }
}
