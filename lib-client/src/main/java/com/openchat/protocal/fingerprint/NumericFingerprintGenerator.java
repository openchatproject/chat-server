package com.openchat.protocal.fingerprint;

import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.util.ByteUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NumericFingerprintGenerator implements FingerprintGenerator {

  private static final int VERSION = 0;

  private final long iterations;

  
  public NumericFingerprintGenerator(long iterations) {
    this.iterations = iterations;
  }

  
  @Override
  public Fingerprint createFor(String localStableIdentifier, IdentityKey localIdentityKey,
                               String remoteStableIdentifier, IdentityKey remoteIdentityKey)
  {
    DisplayableFingerprint displayableFingerprint = new DisplayableFingerprint(getDisplayStringFor(localStableIdentifier, localIdentityKey),
                                                                               getDisplayStringFor(remoteStableIdentifier, remoteIdentityKey));

    ScannableFingerprint scannableFingerprint = new ScannableFingerprint(VERSION,
                                                                         localStableIdentifier, localIdentityKey,
                                                                         remoteStableIdentifier, remoteIdentityKey);

    return new Fingerprint(displayableFingerprint, scannableFingerprint);
  }

  private String getDisplayStringFor(String stableIdentifier, IdentityKey identityKey) {
    try {
      MessageDigest digest    = MessageDigest.getInstance("SHA-512");
      byte[]        publicKey = identityKey.getPublicKey().serialize();
      byte[]        hash      = ByteUtil.combine(ByteUtil.shortToByteArray(VERSION),
                                                 publicKey, stableIdentifier.getBytes());

      for (int i=0;i<iterations;i++) {
        digest.update(hash);
        hash = digest.digest(publicKey);
      }

      return getEncodedChunk(hash, 0) +
          getEncodedChunk(hash, 5) +
          getEncodedChunk(hash, 10) +
          getEncodedChunk(hash, 15) +
          getEncodedChunk(hash, 20) +
          getEncodedChunk(hash, 25);
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError(e);
    }
  }

  private String getEncodedChunk(byte[] hash, int offset) {
    long chunk = ByteUtil.byteArray5ToLong(hash, offset) % 100000;
    return String.format("%05d", chunk);
  }

}
