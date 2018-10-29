package com.openchat.protocal.state;

import com.google.protobuf.ByteString;

import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.ecc.Curve;
import com.openchat.protocal.ecc.ECKeyPair;
import com.openchat.protocal.ecc.ECPrivateKey;
import com.openchat.protocal.ecc.ECPublicKey;

import java.io.IOException;

import static com.openchat.protocal.state.StorageProtos.PreKeyRecordStructure;

public class PreKeyRecord {

  private PreKeyRecordStructure structure;

  public PreKeyRecord(int id, ECKeyPair keyPair) {
    this.structure = PreKeyRecordStructure.newBuilder()
                                          .setId(id)
                                          .setPublicKey(ByteString.copyFrom(keyPair.getPublicKey()
                                                                                   .serialize()))
                                          .setPrivateKey(ByteString.copyFrom(keyPair.getPrivateKey()
                                                                                    .serialize()))
                                          .build();
  }

  public PreKeyRecord(byte[] serialized) throws IOException {
    this.structure = PreKeyRecordStructure.parseFrom(serialized);
  }

  public int getId() {
    return this.structure.getId();
  }

  public ECKeyPair getKeyPair() {
    try {
      ECPublicKey publicKey = Curve.decodePoint(this.structure.getPublicKey().toByteArray(), 0);
      ECPrivateKey privateKey = Curve.decodePrivatePoint(this.structure.getPrivateKey().toByteArray());

      return new ECKeyPair(publicKey, privateKey);
    } catch (InvalidKeyException e) {
      throw new AssertionError(e);
    }
  }

  public byte[] serialize() {
    return this.structure.toByteArray();
  }
}
