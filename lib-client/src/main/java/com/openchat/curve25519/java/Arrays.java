package com.openchat.curve25519.java;

public class Arrays {
  
  public static void fill(byte[] a, byte val) {
    for (int i = 0, len = a.length; i < len; i++)
      a[i] = val;
  }

}
