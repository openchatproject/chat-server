package com.openchat.protocal;


import com.openchat.protocal.ecc.Curve;
import com.openchat.protocal.ecc.ECPublicKey;
import com.openchat.protocal.util.Hex;



public class IdentityKey {

  private final ECPublicKey publicKey;

  public IdentityKey(ECPublicKey publicKey) {
    this.publicKey = publicKey;
  }

  public IdentityKey(byte[] bytes, int offset) throws InvalidKeyException {
    this.publicKey = Curve.decodePoint(bytes, offset);
  }

  public ECPublicKey getPublicKey() {
    return publicKey;
  }

  public byte[] serialize() {
    return publicKey.serialize();
  }

  public String getFingerprint() {
    return Hex.toString(publicKey.serialize());
  }
	
  @Override
  public boolean equals(Object other) {
    if (other == null)                   return false;
    if (!(other instanceof IdentityKey)) return false;

    return publicKey.equals(((IdentityKey) other).getPublicKey());
  }
	
  @Override
  public int hashCode() {
    return publicKey.hashCode();
  }
}
