package com.openchat.secureim.mms;

import java.io.IOException;

import com.openchat.imservice.crypto.TransportDetails;
import com.openchat.imservice.util.Base64;

public class TextTransport implements TransportDetails {

  @Override
  public byte[] getDecodedMessage(byte[] encodedMessageBytes) throws IOException {
    return Base64.decode(encodedMessageBytes);
  }

  @Override
  public byte[] getEncodedMessage(byte[] messageWithMac) {
    return Base64.encodeBytes(messageWithMac).getBytes();
  }

  @Override
  public byte[] getPaddedMessageBody(byte[] messageBody) {
    return messageBody;
  }

  @Override
  public byte[] getStrippedPaddingMessageBody(byte[] messageWithPadding) {
    return messageWithPadding;
  }

}
