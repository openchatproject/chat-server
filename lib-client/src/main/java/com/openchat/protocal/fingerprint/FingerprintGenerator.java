package com.openchat.protocal.fingerprint;

import com.openchat.protocal.IdentityKey;

public interface FingerprintGenerator {
  public Fingerprint createFor(String localStableIdentifier, IdentityKey localIdentityKey,
                               String remoteStableIdentifier, IdentityKey remoteIdentityKey);
}
