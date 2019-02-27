package com.openchat.secureim.crypto;

import com.openchat.imservice.crypto.InvalidKeyException;
import com.openchat.imservice.crypto.InvalidMessageException;
import com.openchat.imservice.crypto.MasterCipher;
import com.openchat.imservice.crypto.MasterSecret;
import com.openchat.imservice.crypto.PublicKey;
import com.openchat.imservice.crypto.ecc.Curve;
import com.openchat.imservice.crypto.ecc.ECKeyPair;
import com.openchat.imservice.crypto.ecc.ECPrivateKey;
import com.openchat.imservice.crypto.ecc.ECPublicKey;
import com.openchat.imservice.util.Base64;
import com.openchat.imservice.util.Conversions;
import com.openchat.imservice.util.Util;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class AsymmetricMasterCipher {

  private final AsymmetricMasterSecret asymmetricMasterSecret;

  public AsymmetricMasterCipher(AsymmetricMasterSecret asymmetricMasterSecret) {
    this.asymmetricMasterSecret = asymmetricMasterSecret;
  }
	
  public String decryptBody(String body) throws IOException, InvalidMessageException {
    try {
      byte[]    combined       = Base64.decode(body);
      byte[][]  parts          = Util.split(combined, PublicKey.KEY_SIZE, combined.length - PublicKey.KEY_SIZE);
      PublicKey theirPublicKey = new PublicKey(parts[0], 0);

      ECPrivateKey ourPrivateKey = asymmetricMasterSecret.getPrivateKey();
      byte[]       secret        = Curve.calculateAgreement(theirPublicKey.getKey(), ourPrivateKey);
      MasterCipher masterCipher  = getMasterCipherForSecret(secret);
      byte[]       decryptedBody = masterCipher.decryptBytes(parts[1]);

      return new String(decryptedBody);
    } catch (InvalidKeyException ike) {
      throw new InvalidMessageException(ike);
    } catch (InvalidMessageException e) {
      throw new InvalidMessageException(e);
    }		
  }
	
  public String encryptBody(String body) {
    try {
      ECPublicKey  theirPublic        = asymmetricMasterSecret.getDjbPublicKey();
      ECKeyPair    ourKeyPair         = Curve.generateKeyPair(true);
      byte[]       secret             = Curve.calculateAgreement(theirPublic, ourKeyPair.getPrivateKey());
      MasterCipher masterCipher       = getMasterCipherForSecret(secret);
      byte[]       encryptedBodyBytes = masterCipher.encryptBytes(body.getBytes());

      PublicKey    ourPublicKey       = new PublicKey(31337, ourKeyPair.getPublicKey());
      byte[]       publicKeyBytes     = ourPublicKey.serialize();
      byte[]       combined           = Util.combine(publicKeyBytes, encryptedBodyBytes);

      return Base64.encodeBytes(combined);
    } catch (InvalidKeyException e) {
      throw new AssertionError(e);
    }
  }
	
  private MasterCipher getMasterCipherForSecret(byte[] secretBytes) {
    SecretKeySpec cipherKey   = deriveCipherKey(secretBytes);
    SecretKeySpec macKey      = deriveMacKey(secretBytes);
    MasterSecret masterSecret = new MasterSecret(cipherKey, macKey);

    return new MasterCipher(masterSecret);		
  }
	
  private SecretKeySpec deriveMacKey(byte[] secretBytes) {
    byte[] digestedBytes = getDigestedBytes(secretBytes, 1);
    byte[] macKeyBytes   = new byte[20];
		
    System.arraycopy(digestedBytes, 0, macKeyBytes, 0, macKeyBytes.length);
    return new SecretKeySpec(macKeyBytes, "HmacSHA1");
  }
	
  private SecretKeySpec deriveCipherKey(byte[] secretBytes) {
    byte[] digestedBytes  = getDigestedBytes(secretBytes, 0);
    byte[] cipherKeyBytes = new byte[16];
		
    System.arraycopy(digestedBytes, 0, cipherKeyBytes, 0, cipherKeyBytes.length);		
    return new SecretKeySpec(cipherKeyBytes, "AES");
  }
	
  private byte[] getDigestedBytes(byte[] secretBytes, int iteration) {
    try {
      Mac mac = Mac.getInstance("HmacSHA256");
      mac.init(new SecretKeySpec(secretBytes, "HmacSHA256"));
      return mac.doFinal(Conversions.intToByteArray(iteration));
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError(e);
    } catch (java.security.InvalidKeyException e) {
      throw new AssertionError(e);
    }
  }
}
