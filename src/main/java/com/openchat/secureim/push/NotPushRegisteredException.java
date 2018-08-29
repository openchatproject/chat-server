package com.openchat.secureim.push;

public class NotPushRegisteredException extends Exception {
  public NotPushRegisteredException(String s) {
    super(s);
  }

  public NotPushRegisteredException(Exception e) {
    super(e);
  }
}
