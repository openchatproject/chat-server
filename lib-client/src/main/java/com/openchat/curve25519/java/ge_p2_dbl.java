package com.openchat.curve25519.java;

public class ge_p2_dbl {

public static void ge_p2_dbl(ge_p1p1 r,ge_p2 p)
{
  int[] t0 = new int[10];

fe_sq.fe_sq(r.X,p.X);

fe_sq.fe_sq(r.Z,p.Y);

fe_sq2.fe_sq2(r.T,p.Z);

fe_add.fe_add(r.Y,p.X,p.Y);

fe_sq.fe_sq(t0,r.Y);

fe_add.fe_add(r.Y,r.Z,r.X);

fe_sub.fe_sub(r.Z,r.Z,r.X);

fe_sub.fe_sub(r.X,t0,r.Y);

fe_sub.fe_sub(r.T,r.T,r.Z);

}

}
