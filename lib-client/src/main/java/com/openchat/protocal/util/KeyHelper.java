package com.openchat.protocal.util;

import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.IdentityKeyPair;
import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.ecc.Curve;
import com.openchat.protocal.ecc.ECKeyPair;
import com.openchat.protocal.state.PreKeyRecord;
import com.openchat.protocal.state.SignedPreKeyRecord;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;


public class KeyHelper {

  private KeyHelper() {}

  
  public static IdentityKeyPair generateIdentityKeyPair() {
    ECKeyPair   keyPair   = Curve.generateKeyPair();
    IdentityKey publicKey = new IdentityKey(keyPair.getPublicKey());
    return new IdentityKeyPair(publicKey, keyPair.getPrivateKey());
  }

  
  public static int generateRegistrationId(boolean extendedRange) {
    try {
      SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
      if (extendedRange) return secureRandom.nextInt(Integer.MAX_VALUE - 1) + 1;
      else               return secureRandom.nextInt(16380) + 1;
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError(e);
    }
  }

  public static int getRandomSequence(int max) {
    try {
      return SecureRandom.getInstance("SHA1PRNG").nextInt(max);
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError(e);
    }
  }

  
  public static List<PreKeyRecord> generatePreKeys(int start, int count) {
    List<PreKeyRecord> results = new LinkedList<>();

    start--;

    for (int i=0;i<count;i++) {
      results.add(new PreKeyRecord(((start + i) % (Medium.MAX_VALUE-1)) + 1, Curve.generateKeyPair()));
    }

    return results;
  }

  
  public static PreKeyRecord generateLastResortPreKey() {
    ECKeyPair keyPair = Curve.generateKeyPair();
    return new PreKeyRecord(Medium.MAX_VALUE, keyPair);
  }

  
  public static SignedPreKeyRecord generateSignedPreKey(IdentityKeyPair identityKeyPair, int signedPreKeyId)
      throws InvalidKeyException
  {
    ECKeyPair keyPair   = Curve.generateKeyPair();
    byte[]    signature = Curve.calculateSignature(identityKeyPair.getPrivateKey(), keyPair.getPublicKey().serialize());

    return new SignedPreKeyRecord(signedPreKeyId, System.currentTimeMillis(), keyPair, signature);
  }


  public static ECKeyPair generateSenderSigningKey() {
    return Curve.generateKeyPair();
  }

  public static byte[] generateSenderKey() {
    try {
      byte[] key = new byte[32];
      SecureRandom.getInstance("SHA1PRNG").nextBytes(key);

      return key;
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError(e);
    }
  }

  public static int generateSenderKeyId() {
    try {
      return SecureRandom.getInstance("SHA1PRNG").nextInt(Integer.MAX_VALUE);
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError(e);
    }
  }

}
