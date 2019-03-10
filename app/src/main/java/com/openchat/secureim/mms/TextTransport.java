package com.openchat.secureim.mms;

import java.io.IOException;

import com.openchat.imservice.util.Base64;

public class TextTransport {

  public byte[] getDecodedMessage(byte[] encodedMessageBytes) throws IOException {
    return Base64.decode(encodedMessageBytes);
  }

  public byte[] getEncodedMessage(byte[] messageWithMac) {
    return Base64.encodeBytes(messageWithMac).getBytes();
  }
}
