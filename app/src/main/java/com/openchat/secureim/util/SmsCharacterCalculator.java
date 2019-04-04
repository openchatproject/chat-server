package com.openchat.secureim.util;

import com.openchat.secureim.sms.SmsTransportDetails;

public class SmsCharacterCalculator extends CharacterCalculator {

  @Override
  public CharacterState calculateCharacters(int charactersSpent) {
    int maxMessageSize;

    if (charactersSpent <= SmsTransportDetails.SMS_SIZE) {
      maxMessageSize = SmsTransportDetails.SMS_SIZE;
    } else {
      maxMessageSize = SmsTransportDetails.MULTIPART_SMS_SIZE;
    }

    int messagesSpent = charactersSpent / maxMessageSize;

    if (((charactersSpent % maxMessageSize) > 0) || (messagesSpent == 0))
      messagesSpent++;

    int charactersRemaining = (maxMessageSize * messagesSpent) - charactersSpent;

    return new CharacterState(messagesSpent, charactersRemaining, maxMessageSize);
  }
}

