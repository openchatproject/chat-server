package com.openchat.secureim.auth;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class AuthenticationCredentials {

  private final Logger logger = LoggerFactory.getLogger(AuthenticationCredentials.class);

  private final String hashedAuthenticationToken;
  private final String salt;

  public AuthenticationCredentials(String hashedAuthenticationToken, String salt) {
    this.hashedAuthenticationToken = hashedAuthenticationToken;
    this.salt                      = salt;
  }

  public AuthenticationCredentials(String authenticationToken) {
    this.salt                      = Math.abs(new SecureRandom().nextInt()) + "";
    this.hashedAuthenticationToken = getHashedValue(salt, authenticationToken);
  }

  public String getHashedAuthenticationToken() {
    return hashedAuthenticationToken;
  }

  public String getSalt() {
    return salt;
  }

  public boolean verify(String authenticationToken) {
    String theirValue = getHashedValue(salt, authenticationToken);

    logger.debug("Comparing: " + theirValue + " , " + this.hashedAuthenticationToken);

    return theirValue.equals(this.hashedAuthenticationToken);
  }

  private static String getHashedValue(String salt, String token) {
    Logger logger = LoggerFactory.getLogger(AuthenticationCredentials.class);
    logger.debug("Getting hashed token: " + salt + " , " + token);

    try {
      return new String(Hex.encodeHex(MessageDigest.getInstance("SHA1").digest((salt + token).getBytes("UTF-8"))));
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      throw new AssertionError(e);
    }
  }

}
