package com.openchat.protocal.protocol;

import com.google.protobuf.ByteString;

import com.openchat.protocal.ecc.ECPublicKey;
import com.openchat.protocal.util.ByteUtil;

public class SenderKeyDistributionMessage implements CiphertextMessage {

  private final int         id;
  private final int         iteration;
  private final byte[]      chainKey;
  private final ECPublicKey signatureKey;
  private final byte[]      serialized;

  public SenderKeyDistributionMessage(int id, int iteration, byte[] chainKey, ECPublicKey signatureKey) {
    byte[] version = {ByteUtil.intsToByteHighAndLow(CURRENT_VERSION, CURRENT_VERSION)};

    this.id           = id;
    this.iteration    = iteration;
    this.chainKey     = chainKey;
    this.signatureKey = signatureKey;
    this.serialized   = OpenchatProtos.SenderKeyDistributionMessage.newBuilder()
                                                                  .setId(id)
                                                                  .setIteration(iteration)
                                                                  .setChainKey(ByteString.copyFrom(chainKey))
                                                                  .setSigningKey(ByteString.copyFrom(signatureKey.serialize()))
                                                                  .build().toByteArray();
  }

  @Override
  public byte[] serialize() {
    return serialized;
  }

  @Override
  public int getType() {
    return SENDERKEY_DISTRIBUTION_TYPE;
  }

  public int getIteration() {
    return iteration;
  }

  public byte[] getChainKey() {
    return chainKey;
  }

  public ECPublicKey getSignatureKey() {
    return signatureKey;
  }

  public int getId() {
    return id;
  }
}
