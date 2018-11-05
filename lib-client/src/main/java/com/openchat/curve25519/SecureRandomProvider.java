package com.openchat.curve25519;

public interface SecureRandomProvider {
  public void nextBytes(byte[] output);
  public int nextInt(int maxValue);
}
