package com.openchat.protocal;

public interface DecryptionCallback {
  public void handlePlaintext(byte[] plaintext);
}
