package com.openchat.protocal;

public class InvalidKeyException extends Exception {

  public InvalidKeyException() {}

  public InvalidKeyException(String detailMessage) {
    super(detailMessage);
  }

  public InvalidKeyException(Throwable throwable) {
    super(throwable);
  }

  public InvalidKeyException(String detailMessage, Throwable throwable) {
    super(detailMessage, throwable);
  }
}
