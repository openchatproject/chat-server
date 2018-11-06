package com.openchat.imservice.api.push.exceptions;

public class RateLimitException extends NonSuccessfulResponseCodeException {
  public RateLimitException(String s) {
    super(s);
  }
}
