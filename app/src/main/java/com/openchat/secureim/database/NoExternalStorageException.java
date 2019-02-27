package com.openchat.secureim.database;

public class NoExternalStorageException extends Exception {

  public NoExternalStorageException() {
  }

  public NoExternalStorageException(String detailMessage) {
    super(detailMessage);
  }

  public NoExternalStorageException(Throwable throwable) {
    super(throwable);
  }

  public NoExternalStorageException(String detailMessage, Throwable throwable) {
    super(detailMessage, throwable);
  }

}
