package com.openchat.secureim.mms;

public class MediaTooLargeException extends Exception {

  public MediaTooLargeException() {
  }

  public MediaTooLargeException(String detailMessage) {
    super(detailMessage);
  }

  public MediaTooLargeException(Throwable throwable) {
    super(throwable);
  }

  public MediaTooLargeException(String detailMessage, Throwable throwable) {
    super(detailMessage, throwable);
  }

}
