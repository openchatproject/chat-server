package com.openchat.curve25519.java;

public interface Sha512 {

  public void calculateDigest(byte[] out, byte[] in, long length);

}
