package com.openchat.secureim.protocol;

public class KeyExchangeWirePrefix extends WirePrefix {

  @Override
  public String calculatePrefix(String message) {
    return super.calculateKeyExchangePrefix(message);
  }

}
