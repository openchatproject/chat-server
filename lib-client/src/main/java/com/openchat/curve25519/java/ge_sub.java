package com.openchat.curve25519.java;

public class ge_sub {

public static void ge_sub(ge_p1p1 r,ge_p3 p,ge_cached q)
{
  int[] t0 = new int[10];

fe_add.fe_add(r.X,p.Y,p.X);

fe_sub.fe_sub(r.Y,p.Y,p.X);

fe_mul.fe_mul(r.Z,r.X,q.YminusX);

fe_mul.fe_mul(r.Y,r.Y,q.YplusX);

fe_mul.fe_mul(r.T,q.T2d,p.T);

fe_mul.fe_mul(r.X,p.Z,q.Z);

fe_add.fe_add(t0,r.X,r.X);

fe_sub.fe_sub(r.X,r.Z,r.Y);

fe_add.fe_add(r.Y,r.Z,r.Y);

fe_sub.fe_sub(r.Z,t0,r.T);

fe_add.fe_add(r.T,t0,r.T);

}

}
