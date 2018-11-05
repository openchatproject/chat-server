package com.openchat.curve25519;

public class Curve25519 {

  public static final String NATIVE = "native";
  public static final String JAVA   = "java";
  public static final String J2ME   = "j2me";
  public static final String BEST   = "best";

  public static Curve25519 getInstance(String type) throws NoSuchProviderException {
    return getInstance(type, null);
  }

  public static Curve25519 getInstance(String type, SecureRandomProvider random)
      throws NoSuchProviderException
  {
    if      (NATIVE.equals(type)) return new Curve25519(constructNativeProvider(random));
    else if (JAVA.equals(type))   return new Curve25519(constructJavaProvider(random));
    else if (J2ME.equals(type))   return new Curve25519(constructJ2meProvider(random));
    else if (BEST.equals(type))   return new Curve25519(constructOpportunisticProvider(random));
    else                          throw new NoSuchProviderException(type);
  }

  private final Curve25519Provider provider;

  private Curve25519(Curve25519Provider provider) {
    this.provider = provider;
  }

  
  public boolean isNative() {
    return provider.isNative();
  }

  
  public Curve25519KeyPair generateKeyPair() {
    byte[] privateKey = provider.generatePrivateKey();
    byte[] publicKey  = provider.generatePublicKey(privateKey);

    return new Curve25519KeyPair(publicKey, privateKey);
  }

  
  public byte[] calculateAgreement(byte[] publicKey, byte[] privateKey) {
    if (publicKey == null || privateKey == null) {
      throw new IllegalArgumentException("Keys must not be null!");
    }

    if (publicKey.length != 32 || privateKey.length != 32) {
      throw new IllegalArgumentException("Keys must be 32 bytes!");
    }

    return provider.calculateAgreement(privateKey, publicKey);
  }

  
  public byte[] calculateSignature(byte[] privateKey, byte[] message) {
    if (privateKey == null || privateKey.length != 32) {
      throw new IllegalArgumentException("Invalid private key length!");
    }

    byte[] random = provider.getRandom(64);
    return provider.calculateSignature(random, privateKey, message);
  }

  
  public boolean verifySignature(byte[] publicKey, byte[] message, byte[] signature) {
    if (publicKey == null || publicKey.length != 32) {
      throw new IllegalArgumentException("Invalid public key!");
    }

    if (message == null || signature == null || signature.length != 64) {
      return false;
    }

    return provider.verifySignature(publicKey, message, signature);
  }

  
  public byte[] calculateVrfSignature(byte[] privateKey, byte[] message) {
    if (privateKey == null || privateKey.length != 32) {
      throw new IllegalArgumentException("Invalid private key!");
    }

    byte[] random = provider.getRandom(64);
    return provider.calculateVrfSignature(random, privateKey, message);
  }

  
  public byte[] verifyVrfSignature(byte[] publicKey, byte[] message, byte[] signature)
      throws VrfSignatureVerificationFailedException
  {
    if (publicKey == null || publicKey.length != 32) {
      throw new IllegalArgumentException("Invalid public key!");
    }

    if (message == null || signature == null || signature.length != 96) {
      throw new VrfSignatureVerificationFailedException("Invalid message or signature format");
    }

    return provider.verifyVrfSignature(publicKey, message, signature);
  }

  private static Curve25519Provider constructNativeProvider(SecureRandomProvider random) throws NoSuchProviderException {
    return constructClass("NativeCurve25519Provider", random);
  }

  private static Curve25519Provider constructJavaProvider(SecureRandomProvider random) throws NoSuchProviderException {
    return constructClass("JavaCurve25519Provider", random);
  }

  private static Curve25519Provider constructJ2meProvider(SecureRandomProvider random) throws NoSuchProviderException {
    return constructClass("J2meCurve25519Provider", random);
  }

  private static Curve25519Provider constructOpportunisticProvider(SecureRandomProvider random) throws NoSuchProviderException {
    return constructClass("OpportunisticCurve25519Provider", random);
  }

  private static Curve25519Provider constructClass(String name, SecureRandomProvider random) throws NoSuchProviderException {
    try {
      Curve25519Provider provider =  (Curve25519Provider)Class.forName("com.openchat.curve25519." + name).newInstance();

      if (random != null) {
        provider.setRandomProvider(random);
      }

      return provider;
    } catch (InstantiationException e) {
      throw new NoSuchProviderException(e);
    } catch (IllegalAccessException e) {
      throw new NoSuchProviderException(e);
    } catch (ClassNotFoundException e) {
      throw new NoSuchProviderException(e);
    }
  }

}
