package com.openchat.secureim.auth;


public class InvalidAuthorizationHeaderException extends Exception {
  public InvalidAuthorizationHeaderException(String s) {
    super(s);
  }

  public InvalidAuthorizationHeaderException(Exception e) {
    super(e);
  }
}
