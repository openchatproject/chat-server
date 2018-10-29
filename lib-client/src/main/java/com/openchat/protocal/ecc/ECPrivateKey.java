package com.openchat.protocal.ecc;

public interface ECPrivateKey {
  public byte[] serialize();
  public int getType();
}
