package com.openchat.secureim.federation;


public class NoSuchPeerException extends Exception {
  public NoSuchPeerException(String name) {
    super(name);
  }
}
