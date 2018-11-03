package com.openchat.protocal.fingerprint;

import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.devices.DeviceConsistencySignature;
import com.openchat.protocal.util.ByteArrayComparator;
import com.openchat.protocal.util.ByteUtil;
import com.openchat.protocal.util.IdentityKeyComparator;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NumericFingerprintGenerator implements FingerprintGenerator {

  private final int iterations;

  
  public NumericFingerprintGenerator(int iterations) {
    this.iterations = iterations;
  }

  
  @Override
  public Fingerprint createFor(String localStableIdentifier, IdentityKey localIdentityKey,
                               String remoteStableIdentifier, IdentityKey remoteIdentityKey)
  {
    DisplayableFingerprint displayableFingerprint = new DisplayableFingerprint(iterations,
                                                                               localStableIdentifier,
                                                                               localIdentityKey,
                                                                               remoteStableIdentifier,
                                                                               remoteIdentityKey);

    ScannableFingerprint scannableFingerprint = new ScannableFingerprint(localStableIdentifier, localIdentityKey,
                                                                         remoteStableIdentifier, remoteIdentityKey);

    return new Fingerprint(displayableFingerprint, scannableFingerprint);
  }

  
  public Fingerprint createFor(String localStableIdentifier, List<IdentityKey> localIdentityKeys,
                               String remoteStableIdentifier, List<IdentityKey> remoteIdentityKeys)
  {
    DisplayableFingerprint displayableFingerprint = new DisplayableFingerprint(iterations,
                                                                               localStableIdentifier,
                                                                               localIdentityKeys,
                                                                               remoteStableIdentifier,
                                                                               remoteIdentityKeys);

    ScannableFingerprint scannableFingerprint = new ScannableFingerprint(localStableIdentifier, localIdentityKeys,
                                                                         remoteStableIdentifier, remoteIdentityKeys);

    return new Fingerprint(displayableFingerprint, scannableFingerprint);
  }


}
