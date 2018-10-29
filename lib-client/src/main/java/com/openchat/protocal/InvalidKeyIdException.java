package com.openchat.protocal;

public class InvalidKeyIdException extends Exception {
  public InvalidKeyIdException(String detailMessage) {
    super(detailMessage);
  }

  public InvalidKeyIdException(Throwable throwable) {
    super(throwable);
  }
}
