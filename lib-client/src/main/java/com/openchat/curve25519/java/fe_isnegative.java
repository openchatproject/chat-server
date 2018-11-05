package com.openchat.curve25519.java;

public class fe_isnegative {

public static int fe_isnegative(int[] f)
{
  byte[] s = new byte[32];
  fe_tobytes.fe_tobytes(s,f);
  return s[0] & 1;
}

}
