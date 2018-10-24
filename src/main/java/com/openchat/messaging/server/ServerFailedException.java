package com.openchat.messaging.server;

public class ServerFailedException extends Exception {
  public ServerFailedException(String message) {
    super(message);
  }

  public ServerFailedException(Exception e) {
    super(e);
  }
}
