package com.openchat.secureim.auth;


import com.openchat.secureim.util.Base64;
import com.openchat.secureim.util.Util;

import java.io.IOException;

public class AuthorizationHeader {

  private final String user;
  private final String password;

  public AuthorizationHeader(String header) throws InvalidAuthorizationHeaderException {
    try {
      if (header == null) {
        throw new InvalidAuthorizationHeaderException("Null header");
      }

      String[] headerParts = header.split(" ");

      if (headerParts == null || headerParts.length < 2) {
        throw new InvalidAuthorizationHeaderException("Invalid authorization header: " + header);
      }

      if (!"Basic".equals(headerParts[0])) {
        throw new InvalidAuthorizationHeaderException("Unsupported authorization method: " + headerParts[0]);
      }

      String concatenatedValues = new String(Base64.decode(headerParts[1]));

      if (Util.isEmpty(concatenatedValues)) {
        throw new InvalidAuthorizationHeaderException("Bad decoded value: " + concatenatedValues);
      }

      String[] credentialParts = concatenatedValues.split(":");

      if (credentialParts == null || credentialParts.length < 2) {
        throw new InvalidAuthorizationHeaderException("Badly formated credentials: " + concatenatedValues);
      }

      this.user     = credentialParts[0];
      this.password = credentialParts[1];

    } catch (IOException ioe) {
      throw new InvalidAuthorizationHeaderException(ioe);
    }
  }

  public String getUserName() {
    return user;
  }

  public String getPassword() {
    return password;
  }
}
