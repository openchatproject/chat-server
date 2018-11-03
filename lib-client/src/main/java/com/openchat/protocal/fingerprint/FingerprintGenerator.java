package com.openchat.protocal.fingerprint;

import com.openchat.protocal.IdentityKey;

import java.util.List;

public interface FingerprintGenerator {
  public Fingerprint createFor(String localStableIdentifier, IdentityKey localIdentityKey,
                               String remoteStableIdentifier, IdentityKey remoteIdentityKey);

  public Fingerprint createFor(String localStableIdentifier, List<IdentityKey> localIdentityKey,
                               String remoteStableIdentifier, List<IdentityKey> remoteIdentityKey);
}
