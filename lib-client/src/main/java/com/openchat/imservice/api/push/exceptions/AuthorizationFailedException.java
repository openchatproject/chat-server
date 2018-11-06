package com.openchat.imservice.api.push.exceptions;

public class AuthorizationFailedException extends NonSuccessfulResponseCodeException {
  public AuthorizationFailedException(String s) {
    super(s);
  }
}
