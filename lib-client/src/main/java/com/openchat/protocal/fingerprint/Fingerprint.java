package com.openchat.protocal.fingerprint;

public class Fingerprint {

  private final DisplayableFingerprint displayableFingerprint;
  private final ScannableFingerprint   scannableFingerprint;

  public Fingerprint(DisplayableFingerprint displayableFingerprint,
                     ScannableFingerprint scannableFingerprint)
  {
    this.displayableFingerprint = displayableFingerprint;
    this.scannableFingerprint   = scannableFingerprint;
  }

  
  public DisplayableFingerprint getDisplayableFingerprint() {
    return displayableFingerprint;
  }

  
  public ScannableFingerprint getScannableFingerprint() {
    return scannableFingerprint;
  }
}
