package com.openchat.secureim.push;

public class TransientPushFailureException extends Exception {
  public TransientPushFailureException(String s) {
    super(s);
  }

  public TransientPushFailureException(Exception e) {
    super(e);
  }
}
