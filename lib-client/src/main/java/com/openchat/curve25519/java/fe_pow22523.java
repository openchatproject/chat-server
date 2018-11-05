package com.openchat.curve25519.java;

public class fe_pow22523 {

public static void fe_pow22523(int[] out,int[] z)
{
  int[] t0 = new int[10];
  int[] t1 = new int[10];
  int[] t2 = new int[10];
  int i;

fe_sq.fe_sq(t0,z); for (i = 1;i < 1;++i) fe_sq.fe_sq(t0,t0);

fe_sq.fe_sq(t1,t0); for (i = 1;i < 2;++i) fe_sq.fe_sq(t1,t1);

fe_mul.fe_mul(t1,z,t1);

fe_mul.fe_mul(t0,t0,t1);

fe_sq.fe_sq(t0,t0); for (i = 1;i < 1;++i) fe_sq.fe_sq(t0,t0);

fe_mul.fe_mul(t0,t1,t0);

fe_sq.fe_sq(t1,t0); for (i = 1;i < 5;++i) fe_sq.fe_sq(t1,t1);

fe_mul.fe_mul(t0,t1,t0);

fe_sq.fe_sq(t1,t0); for (i = 1;i < 10;++i) fe_sq.fe_sq(t1,t1);

fe_mul.fe_mul(t1,t1,t0);

fe_sq.fe_sq(t2,t1); for (i = 1;i < 20;++i) fe_sq.fe_sq(t2,t2);

fe_mul.fe_mul(t1,t2,t1);

fe_sq.fe_sq(t1,t1); for (i = 1;i < 10;++i) fe_sq.fe_sq(t1,t1);

fe_mul.fe_mul(t0,t1,t0);

fe_sq.fe_sq(t1,t0); for (i = 1;i < 50;++i) fe_sq.fe_sq(t1,t1);

fe_mul.fe_mul(t1,t1,t0);

fe_sq.fe_sq(t2,t1); for (i = 1;i < 100;++i) fe_sq.fe_sq(t2,t2);

fe_mul.fe_mul(t1,t2,t1);

fe_sq.fe_sq(t1,t1); for (i = 1;i < 50;++i) fe_sq.fe_sq(t1,t1);

fe_mul.fe_mul(t0,t1,t0);

fe_sq.fe_sq(t0,t0); for (i = 1;i < 2;++i) fe_sq.fe_sq(t0,t0);

fe_mul.fe_mul(out,t0,z);

  return;
}

}
