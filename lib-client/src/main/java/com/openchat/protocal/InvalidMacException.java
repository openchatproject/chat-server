package com.openchat.protocal;

public class InvalidMacException extends Exception {

  public InvalidMacException(String detailMessage) {
    super(detailMessage);
  }

  public InvalidMacException(Throwable throwable) {
    super(throwable);
  }
}
