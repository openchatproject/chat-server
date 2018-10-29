package com.openchat.protocal.ratchet;

import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.ecc.Curve;
import com.openchat.protocal.ecc.ECKeyPair;
import com.openchat.protocal.ecc.ECPublicKey;
import com.openchat.protocal.kdf.DerivedRootSecrets;
import com.openchat.protocal.kdf.HKDF;
import com.openchat.protocal.util.ByteUtil;
import com.openchat.protocal.util.Pair;

public class RootKey {

  private final HKDF   kdf;
  private final byte[] key;

  public RootKey(HKDF kdf, byte[] key) {
    this.kdf = kdf;
    this.key = key;
  }

  public byte[] getKeyBytes() {
    return key;
  }

  public Pair<RootKey, ChainKey> createChain(ECPublicKey theirRatchetKey, ECKeyPair ourRatchetKey)
      throws InvalidKeyException
  {
    byte[]             sharedSecret       = Curve.calculateAgreement(theirRatchetKey, ourRatchetKey.getPrivateKey());
    byte[]             derivedSecretBytes = kdf.deriveSecrets(sharedSecret, key, "OpenchatRatchet".getBytes(), DerivedRootSecrets.SIZE);
    DerivedRootSecrets derivedSecrets     = new DerivedRootSecrets(derivedSecretBytes);

    RootKey  newRootKey  = new RootKey(kdf, derivedSecrets.getRootKey());
    ChainKey newChainKey = new ChainKey(kdf, derivedSecrets.getChainKey(), 0);

    return new Pair<>(newRootKey, newChainKey);
  }
}
