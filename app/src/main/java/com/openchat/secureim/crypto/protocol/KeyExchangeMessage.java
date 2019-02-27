package com.openchat.secureim.crypto.protocol;

import com.openchat.imservice.crypto.IdentityKey;
import com.openchat.imservice.crypto.InvalidKeyException;
import com.openchat.imservice.crypto.InvalidMessageException;
import com.openchat.imservice.crypto.InvalidVersionException;
import com.openchat.imservice.crypto.LegacyMessageException;

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
