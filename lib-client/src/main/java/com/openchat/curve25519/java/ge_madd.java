package com.openchat.curve25519.java;

public class ge_madd {

public static void ge_madd(ge_p1p1 r,ge_p3 p,ge_precomp q)
{
  int[] t0 = new int[10];

fe_add.fe_add(r.X,p.Y,p.X);

fe_sub.fe_sub(r.Y,p.Y,p.X);

fe_mul.fe_mul(r.Z,r.X,q.yplusx);

fe_mul.fe_mul(r.Y,r.Y,q.yminusx);

fe_mul.fe_mul(r.T,q.xy2d,p.T);

fe_add.fe_add(t0,p.Z,p.Z);

fe_sub.fe_sub(r.X,r.Z,r.Y);

fe_add.fe_add(r.Y,r.Z,r.Y);

fe_add.fe_add(r.Z,t0,r.T);

fe_sub.fe_sub(r.T,t0,r.T);

}

}
