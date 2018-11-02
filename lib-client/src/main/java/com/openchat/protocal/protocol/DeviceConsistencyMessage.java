package com.openchat.protocal.protocol;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.IdentityKeyPair;
import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.InvalidMessageException;
import com.openchat.protocal.devices.DeviceConsistencyCommitment;
import com.openchat.protocal.devices.DeviceConsistencySignature;
import com.openchat.protocal.ecc.Curve;

public class DeviceConsistencyMessage {

  private final DeviceConsistencySignature signature;
  private final byte[]                     serialized;

  public DeviceConsistencyMessage(DeviceConsistencyCommitment commitment, IdentityKeyPair identityKeyPair) {
    try {
      this.signature  = new DeviceConsistencySignature(Curve.calculateUniqueSignature(identityKeyPair.getPrivateKey(), commitment.toByteArray()));
      this.serialized = OpenchatProtos.DeviceConsistencyCodeMessage.newBuilder()
                                                                  .setGeneration(commitment.getGeneration())
                                                                  .setSignature(ByteString.copyFrom(signature.toByteArray()))
                                                                  .build()
                                                                  .toByteArray();
    } catch (InvalidKeyException e) {
      throw new AssertionError(e);
    }
  }

  public DeviceConsistencyMessage(DeviceConsistencyCommitment commitment, byte[] serialized, IdentityKey identityKey) throws InvalidMessageException {
    try {
      OpenchatProtos.DeviceConsistencyCodeMessage message = OpenchatProtos.DeviceConsistencyCodeMessage.parseFrom(serialized);

      if (!Curve.verifyUniqueSignature(identityKey.getPublicKey(), commitment.toByteArray(), message.getSignature().toByteArray())) {
        throw new InvalidMessageException("Bad signature!");
      }

      this.signature  = new DeviceConsistencySignature(message.getSignature().toByteArray());
      this.serialized = serialized;
    } catch (InvalidProtocolBufferException | InvalidKeyException e) {
      throw new InvalidMessageException(e);
    }
  }

  public byte[] getSerialized() {
    return serialized;
  }

  public DeviceConsistencySignature getSignature() {
    return signature;
  }
}
