package com.openchat.protocal.protocol;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import com.openchat.curve25519.VrfSignatureVerificationFailedException;
import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.IdentityKeyPair;
import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.InvalidMessageException;
import com.openchat.protocal.devices.DeviceConsistencyCommitment;
import com.openchat.protocal.devices.DeviceConsistencySignature;
import com.openchat.protocal.ecc.Curve;

public class DeviceConsistencyMessage {

  private final DeviceConsistencySignature  signature;
  private final int                         generation;
  private final byte[]                      serialized;

  public DeviceConsistencyMessage(DeviceConsistencyCommitment commitment, IdentityKeyPair identityKeyPair) {
    try {
      byte[] signatureBytes = Curve.calculateVrfSignature(identityKeyPair.getPrivateKey(), commitment.toByteArray());
      byte[] vrfOutputBytes = Curve.verifyVrfSignature(identityKeyPair.getPublicKey().getPublicKey(), commitment.toByteArray(), signatureBytes);

      this.generation = commitment.getGeneration();
      this.signature  = new DeviceConsistencySignature(signatureBytes, vrfOutputBytes);
      this.serialized = OpenchatProtos.DeviceConsistencyCodeMessage.newBuilder()
                                                                  .setGeneration(commitment.getGeneration())
                                                                  .setSignature(ByteString.copyFrom(signature.getSignature()))
                                                                  .build()
                                                                  .toByteArray();
    } catch (InvalidKeyException | VrfSignatureVerificationFailedException e) {
      throw new AssertionError(e);
    }
  }

  public DeviceConsistencyMessage(DeviceConsistencyCommitment commitment, byte[] serialized, IdentityKey identityKey) throws InvalidMessageException {
    try {
      OpenchatProtos.DeviceConsistencyCodeMessage message = OpenchatProtos.DeviceConsistencyCodeMessage.parseFrom(serialized);
      byte[] vrfOutputBytes = Curve.verifyVrfSignature(identityKey.getPublicKey(), commitment.toByteArray(), message.getSignature().toByteArray());

      this.generation = message.getGeneration();
      this.signature  = new DeviceConsistencySignature(message.getSignature().toByteArray(), vrfOutputBytes);
      this.serialized = serialized;
    } catch (InvalidProtocolBufferException | InvalidKeyException | VrfSignatureVerificationFailedException e) {
      throw new InvalidMessageException(e);
    }
  }

  public byte[] getSerialized() {
    return serialized;
  }

  public DeviceConsistencySignature getSignature() {
    return signature;
  }

  public int getGeneration() {
    return generation;
  }
}
