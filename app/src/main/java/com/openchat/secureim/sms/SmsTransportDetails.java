package com.openchat.secureim.sms;

import android.util.Log;

import com.openchat.secureim.protocol.WirePrefix;
import com.openchat.secureim.util.Base64;
import com.openchat.protocal.protocol.CiphertextMessage;

import java.io.IOException;

public class SmsTransportDetails {

  public static final int SMS_SIZE           = 160;
  public static final int MULTIPART_SMS_SIZE = 153;

  public static final int BASE_MAX_BYTES                = Base64.getEncodedBytesForTarget(SMS_SIZE - WirePrefix.PREFIX_SIZE);
  public static final int SINGLE_MESSAGE_MAX_BYTES      = BASE_MAX_BYTES - MultipartSmsTransportMessage.SINGLE_MESSAGE_MULTIPART_OVERHEAD;
  public static final int MULTI_MESSAGE_MAX_BYTES       = BASE_MAX_BYTES - MultipartSmsTransportMessage.MULTI_MESSAGE_MULTIPART_OVERHEAD;
  public static final int FIRST_MULTI_MESSAGE_MAX_BYTES = BASE_MAX_BYTES - MultipartSmsTransportMessage.FIRST_MULTI_MESSAGE_MULTIPART_OVERHEAD;

  public static final int ENCRYPTED_SINGLE_MESSAGE_BODY_MAX_SIZE = SINGLE_MESSAGE_MAX_BYTES - CiphertextMessage.ENCRYPTED_MESSAGE_OVERHEAD;

  public byte[] getEncodedMessage(byte[] messageWithMac) {
    String encodedMessage = Base64.encodeBytesWithoutPadding(messageWithMac);
    Log.w("SmsTransportDetails", "Encoded Message Length: " + encodedMessage.length());
    return encodedMessage.getBytes();
  }

  public byte[] getDecodedMessage(byte[] encodedMessageBytes) throws IOException {
    String encodedMessage = new String(encodedMessageBytes);
    return Base64.decodeWithoutPadding(encodedMessage);
  }

  public byte[] getStrippedPaddingMessageBody(byte[] messageWithPadding) {
    int paddingBeginsIndex = 0;

    for (int i=1;i<messageWithPadding.length;i++) {
      if (messageWithPadding[i] == (byte)0x00) {
        paddingBeginsIndex = i;
        break;
      }
    }

    if (paddingBeginsIndex == 0)
      return messageWithPadding;

    byte[] message = new byte[paddingBeginsIndex];
    System.arraycopy(messageWithPadding, 0, message, 0, message.length);

    return message;
  }

  public byte[] getPaddedMessageBody(byte[] messageBody) {
    int paddedBodySize = getMaxBodySizeForBytes(messageBody.length);
    Log.w("SmsTransportDetails", "Padding message body out to: " + paddedBodySize);

    byte[] paddedBody = new byte[paddedBodySize];
    System.arraycopy(messageBody, 0, paddedBody, 0, messageBody.length);

    return paddedBody;
  }

  private int getMaxBodySizeForBytes(int bodyLength) {
    int encryptedBodyLength   = bodyLength + CiphertextMessage.ENCRYPTED_MESSAGE_OVERHEAD;
    int messageRecordsForBody = getMessageCountForBytes(encryptedBodyLength);

    if (messageRecordsForBody == 1) {
      return ENCRYPTED_SINGLE_MESSAGE_BODY_MAX_SIZE;
    } else {
      return
          FIRST_MULTI_MESSAGE_MAX_BYTES +
          (MULTI_MESSAGE_MAX_BYTES * (messageRecordsForBody-1)) -
              CiphertextMessage.ENCRYPTED_MESSAGE_OVERHEAD;
    }
  }

  public int getMessageCountForBytes(int bytes) {
    if (bytes <= SINGLE_MESSAGE_MAX_BYTES)
      return 1;

    bytes = Math.max(bytes - FIRST_MULTI_MESSAGE_MAX_BYTES, 0);

    int messageCount = 1 + (bytes / MULTI_MESSAGE_MAX_BYTES);
    int remainder    = bytes % MULTI_MESSAGE_MAX_BYTES;

    if (remainder > 0)
      messageCount++;

    return messageCount;
  }
}
