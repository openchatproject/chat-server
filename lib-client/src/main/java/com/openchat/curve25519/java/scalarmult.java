package com.openchat.curve25519.java;

public class scalarmult {

public static int crypto_scalarmult(byte[] q,
  byte[] n,
  byte[] p)
{
  byte[] e = new byte[32];
  int i;
  int[] x1 = new int[10];
  int[] x2 = new int[10];
  int[] z2 = new int[10];
  int[] x3 = new int[10];
  int[] z3 = new int[10];
  int[] tmp0 = new int[10];
  int[] tmp1 = new int[10];
  int pos;
  int swap;
  int b;

  for (i = 0;i < 32;++i) e[i] = n[i];

  fe_frombytes.fe_frombytes(x1,p);
  fe_1.fe_1(x2);
  fe_0.fe_0(z2);
  fe_copy.fe_copy(x3,x1);
  fe_1.fe_1(z3);

  swap = 0;
  for (pos = 254;pos >= 0;--pos) {
    b = e[pos / 8] >>> (pos & 7);
    b &= 1;
    swap ^= b;
    fe_cswap.fe_cswap(x2,x3,swap);
    fe_cswap.fe_cswap(z2,z3,swap);
    swap = b;

fe_sub.fe_sub(tmp0,x3,z3);

fe_sub.fe_sub(tmp1,x2,z2);

fe_add.fe_add(x2,x2,z2);

fe_add.fe_add(z2,x3,z3);

fe_mul.fe_mul(z3,tmp0,x2);

fe_mul.fe_mul(z2,z2,tmp1);

fe_sq.fe_sq(tmp0,tmp1);

fe_sq.fe_sq(tmp1,x2);

fe_add.fe_add(x3,z3,z2);

fe_sub.fe_sub(z2,z3,z2);

fe_mul.fe_mul(x2,tmp1,tmp0);

fe_sub.fe_sub(tmp1,tmp1,tmp0);

fe_sq.fe_sq(z2,z2);

fe_mul121666.fe_mul121666(z3,tmp1);

fe_sq.fe_sq(x3,x3);

fe_add.fe_add(tmp0,tmp0,z3);

fe_mul.fe_mul(z3,x1,z2);

fe_mul.fe_mul(z2,tmp1,tmp0);

  }
  fe_cswap.fe_cswap(x2,x3,swap);
  fe_cswap.fe_cswap(z2,z3,swap);

  fe_invert.fe_invert(z2,z2);
  fe_mul.fe_mul(x2,x2,z2);
  fe_tobytes.fe_tobytes(q,x2);
  return 0;
}

}
