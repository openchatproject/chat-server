package com.openchat.protocal.fingerprint;

import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.util.ByteUtil;
import com.openchat.protocal.util.IdentityKeyComparator;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class NumericFingerprintGenerator implements FingerprintGenerator {

  private static final int FINGERPRINT_VERSION = 0;

  private final int iterations;

  
  public NumericFingerprintGenerator(int iterations) {
    this.iterations = iterations;
  }

  
  @Override
  public Fingerprint createFor(String localStableIdentifier, final IdentityKey localIdentityKey,
                               String remoteStableIdentifier, final IdentityKey remoteIdentityKey)
  {
    return createFor(localStableIdentifier,
                     new LinkedList<IdentityKey>() {{
                       add(localIdentityKey);
                     }},
                     remoteStableIdentifier,
                     new LinkedList<IdentityKey>() {{
                       add(remoteIdentityKey);
                     }});
  }

  
  public Fingerprint createFor(String localStableIdentifier, List<IdentityKey> localIdentityKeys,
                               String remoteStableIdentifier, List<IdentityKey> remoteIdentityKeys)
  {
    byte[] localFingerprint  = getFingerprint(iterations, localStableIdentifier, localIdentityKeys);
    byte[] remoteFingerprint = getFingerprint(iterations, remoteStableIdentifier, remoteIdentityKeys);

    DisplayableFingerprint displayableFingerprint = new DisplayableFingerprint(localFingerprint,
                                                                               remoteFingerprint);

    ScannableFingerprint   scannableFingerprint   = new ScannableFingerprint(localFingerprint,
                                                                             remoteFingerprint);

    return new Fingerprint(displayableFingerprint, scannableFingerprint);
  }

  private byte[] getFingerprint(int iterations, String stableIdentifier, List<IdentityKey> unsortedIdentityKeys) {
    try {
      MessageDigest digest    = MessageDigest.getInstance("SHA-512");
      byte[]        publicKey = getLogicalKeyBytes(unsortedIdentityKeys);
      byte[]        hash      = ByteUtil.combine(ByteUtil.shortToByteArray(FINGERPRINT_VERSION),
                                                 publicKey, stableIdentifier.getBytes());

      for (int i=0;i<iterations;i++) {
        digest.update(hash);
        hash = digest.digest(publicKey);
      }

      return hash;
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError(e);
    }
  }

  private byte[] getLogicalKeyBytes(List<IdentityKey> identityKeys) {
    ArrayList<IdentityKey> sortedIdentityKeys = new ArrayList<>(identityKeys);
    Collections.sort(sortedIdentityKeys, new IdentityKeyComparator());

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    for (IdentityKey identityKey : sortedIdentityKeys) {
      byte[] publicKeyBytes = identityKey.getPublicKey().serialize();
      baos.write(publicKeyBytes, 0, publicKeyBytes.length);
    }

    return baos.toByteArray();
  }


}
