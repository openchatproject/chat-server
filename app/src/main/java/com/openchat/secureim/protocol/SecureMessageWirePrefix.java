package com.openchat.secureim.protocol;

public class SecureMessageWirePrefix extends WirePrefix {

  @Override
  public String calculatePrefix(String message) {
    return super.calculateEncryptedMesagePrefix(message);
  }

}
