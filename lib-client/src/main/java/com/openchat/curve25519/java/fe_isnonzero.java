package com.openchat.curve25519.java;

public class fe_isnonzero {

static final byte[] zero = new byte[32];

public static int fe_isnonzero(int[] f)
{
  byte[] s = new byte[32];
  fe_tobytes.fe_tobytes(s,f);
  return crypto_verify_32.crypto_verify_32(s,zero);
}

}
