package com.openchat.protocal.fingerprint;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.fingerprint.FingerprintProtos.CombinedFingerprint;
import com.openchat.protocal.fingerprint.FingerprintProtos.FingerprintData;

import java.security.MessageDigest;

public class ScannableFingerprint {

  private final CombinedFingerprint combinedFingerprint;

  public ScannableFingerprint(int version,
                              String localStableIdentifier, IdentityKey localIdentityKey,
                              String remoteStableIdentifier, IdentityKey remoteIdentityKey)
  {
    this.combinedFingerprint = CombinedFingerprint.newBuilder()
                                                  .setVersion(version)
                                                  .setLocalFingerprint(FingerprintData.newBuilder()
                                                                                      .setIdentifier(ByteString.copyFrom(localStableIdentifier.getBytes()))
                                                                                      .setPublicKey(ByteString.copyFrom(localIdentityKey.serialize())))
                                                  .setRemoteFingerprint(FingerprintData.newBuilder()
                                                                                       .setIdentifier(ByteString.copyFrom(remoteStableIdentifier.getBytes()))
                                                                                       .setPublicKey(ByteString.copyFrom(remoteIdentityKey.serialize())))
                                                  .build();
  }

  
  public byte[] getSerialized() {
    return combinedFingerprint.toByteArray();
  }

  
  public boolean compareTo(byte[] scannedFingerprintData)
      throws FingerprintVersionMismatchException, FingerprintIdentifierMismatchException
  {
    try {
      CombinedFingerprint scannedFingerprint = CombinedFingerprint.parseFrom(scannedFingerprintData);

      if (!scannedFingerprint.hasRemoteFingerprint() || !scannedFingerprint.hasLocalFingerprint() ||
          !scannedFingerprint.hasVersion() || scannedFingerprint.getVersion() != combinedFingerprint.getVersion())
      {
        throw new FingerprintVersionMismatchException();
      }

      if (!combinedFingerprint.getLocalFingerprint().getIdentifier().equals(scannedFingerprint.getRemoteFingerprint().getIdentifier()) ||
          !combinedFingerprint.getRemoteFingerprint().getIdentifier().equals(scannedFingerprint.getLocalFingerprint().getIdentifier()))
      {
        throw new FingerprintIdentifierMismatchException(combinedFingerprint.getLocalFingerprint().getIdentifier().toString(),
                                                         combinedFingerprint.getRemoteFingerprint().getIdentifier().toString(),
                                                         scannedFingerprint.getLocalFingerprint().getIdentifier().toString(),
                                                         scannedFingerprint.getRemoteFingerprint().getIdentifier().toString());
      }

      return MessageDigest.isEqual(combinedFingerprint.getLocalFingerprint().toByteArray(), scannedFingerprint.getRemoteFingerprint().toByteArray()) &&
             MessageDigest.isEqual(combinedFingerprint.getRemoteFingerprint().toByteArray(), scannedFingerprint.getLocalFingerprint().toByteArray());
    } catch (InvalidProtocolBufferException e) {
      throw new FingerprintVersionMismatchException(e);
    }
  }
}
