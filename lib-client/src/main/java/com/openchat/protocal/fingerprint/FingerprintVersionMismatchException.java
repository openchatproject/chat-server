package com.openchat.protocal.fingerprint;

public class FingerprintVersionMismatchException extends Exception {

  private final int theirVersion;
  private final int ourVersion;

  public FingerprintVersionMismatchException(int theirVersion, int ourVersion) {
    super();
    this.theirVersion = theirVersion;
    this.ourVersion   = ourVersion;
  }

  public int getTheirVersion() {
    return theirVersion;
  }

  public int getOurVersion() {
    return ourVersion;
  }
}
