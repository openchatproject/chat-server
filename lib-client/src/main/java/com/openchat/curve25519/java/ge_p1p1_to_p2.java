package com.openchat.curve25519.java;

public class ge_p1p1_to_p2 {

public static void ge_p1p1_to_p2(ge_p2 r,ge_p1p1 p)
{
  fe_mul.fe_mul(r.X,p.X,p.T);
  fe_mul.fe_mul(r.Y,p.Y,p.Z);
  fe_mul.fe_mul(r.Z,p.Z,p.T);
}

}
