package com.openchat.protocal;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import com.openchat.protocal.ecc.Curve;
import com.openchat.protocal.ecc.ECPrivateKey;

import static com.openchat.protocal.state.StorageProtos.IdentityKeyPairStructure;


public class IdentityKeyPair {

  private final IdentityKey  publicKey;
  private final ECPrivateKey privateKey;

  public IdentityKeyPair(IdentityKey publicKey, ECPrivateKey privateKey) {
    this.publicKey  = publicKey;
    this.privateKey = privateKey;
  }

  public IdentityKeyPair(byte[] serialized) throws InvalidKeyException {
    try {
      IdentityKeyPairStructure structure = IdentityKeyPairStructure.parseFrom(serialized);
      this.publicKey  = new IdentityKey(structure.getPublicKey().toByteArray(), 0);
      this.privateKey = Curve.decodePrivatePoint(structure.getPrivateKey().toByteArray());
    } catch (InvalidProtocolBufferException e) {
      throw new InvalidKeyException(e);
    }
  }

  public IdentityKey getPublicKey() {
    return publicKey;
  }

  public ECPrivateKey getPrivateKey() {
    return privateKey;
  }

  public byte[] serialize() {
    return IdentityKeyPairStructure.newBuilder()
                                   .setPublicKey(ByteString.copyFrom(publicKey.serialize()))
                                   .setPrivateKey(ByteString.copyFrom(privateKey.serialize()))
                                   .build().toByteArray();
  }
}
