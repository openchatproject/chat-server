package com.openchat.curve25519.java;

public class fe_tobytes {

public static void fe_tobytes(byte[] s,int[] h)
{
  int h0 = h[0];
  int h1 = h[1];
  int h2 = h[2];
  int h3 = h[3];
  int h4 = h[4];
  int h5 = h[5];
  int h6 = h[6];
  int h7 = h[7];
  int h8 = h[8];
  int h9 = h[9];
  int q;
  int carry0;
  int carry1;
  int carry2;
  int carry3;
  int carry4;
  int carry5;
  int carry6;
  int carry7;
  int carry8;
  int carry9;

  q = (19 * h9 + (((int) 1) << 24)) >> 25;
  q = (h0 + q) >> 26;
  q = (h1 + q) >> 25;
  q = (h2 + q) >> 26;
  q = (h3 + q) >> 25;
  q = (h4 + q) >> 26;
  q = (h5 + q) >> 25;
  q = (h6 + q) >> 26;
  q = (h7 + q) >> 25;
  q = (h8 + q) >> 26;
  q = (h9 + q) >> 25;

  
  h0 += 19 * q;
  

  carry0 = h0 >> 26; h1 += carry0; h0 -= carry0 << 26;
  carry1 = h1 >> 25; h2 += carry1; h1 -= carry1 << 25;
  carry2 = h2 >> 26; h3 += carry2; h2 -= carry2 << 26;
  carry3 = h3 >> 25; h4 += carry3; h3 -= carry3 << 25;
  carry4 = h4 >> 26; h5 += carry4; h4 -= carry4 << 26;
  carry5 = h5 >> 25; h6 += carry5; h5 -= carry5 << 25;
  carry6 = h6 >> 26; h7 += carry6; h6 -= carry6 << 26;
  carry7 = h7 >> 25; h8 += carry7; h7 -= carry7 << 25;
  carry8 = h8 >> 26; h9 += carry8; h8 -= carry8 << 26;
  carry9 = h9 >> 25;               h9 -= carry9 << 25;
                  

  

  s[0] = (byte)(h0 >> 0);
  s[1] = (byte)(h0 >> 8);
  s[2] = (byte)(h0 >> 16);
  s[3] = (byte)((h0 >> 24) | (h1 << 2));
  s[4] = (byte)(h1 >> 6);
  s[5] = (byte)(h1 >> 14);
  s[6] = (byte)((h1 >> 22) | (h2 << 3));
  s[7] = (byte)(h2 >> 5);
  s[8] = (byte)(h2 >> 13);
  s[9] = (byte)((h2 >> 21) | (h3 << 5));
  s[10] = (byte)(h3 >> 3);
  s[11] = (byte)(h3 >> 11);
  s[12] = (byte)((h3 >> 19) | (h4 << 6));
  s[13] = (byte)(h4 >> 2);
  s[14] = (byte)(h4 >> 10);
  s[15] = (byte)(h4 >> 18);
  s[16] = (byte)(h5 >> 0);
  s[17] = (byte)(h5 >> 8);
  s[18] = (byte)(h5 >> 16);
  s[19] = (byte)((h5 >> 24) | (h6 << 1));
  s[20] = (byte)(h6 >> 7);
  s[21] = (byte)(h6 >> 15);
  s[22] = (byte)((h6 >> 23) | (h7 << 3));
  s[23] = (byte)(h7 >> 5);
  s[24] = (byte)(h7 >> 13);
  s[25] = (byte)((h7 >> 21) | (h8 << 4));
  s[26] = (byte)(h8 >> 4);
  s[27] = (byte)(h8 >> 12);
  s[28] = (byte)((h8 >> 20) | (h9 << 6));
  s[29] = (byte)(h9 >> 2);
  s[30] = (byte)(h9 >> 10);
  s[31] = (byte)(h9 >> 18);
}

}
