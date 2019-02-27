package com.openchat.secureim.util;

import com.openchat.secureim.sms.SmsTransportDetails;

public class EncryptedCharacterCalculator extends CharacterCalculator {

  private CharacterState calculateSingleRecordCharacters(int charactersSpent) {
    int charactersRemaining = SmsTransportDetails.ENCRYPTED_SINGLE_MESSAGE_BODY_MAX_SIZE - charactersSpent;

    return new CharacterState(1, charactersRemaining, SmsTransportDetails.ENCRYPTED_SINGLE_MESSAGE_BODY_MAX_SIZE);
  }

  private CharacterState calculateMultiRecordCharacters(int charactersSpent) {
    int charactersInFirstRecord = SmsTransportDetails.ENCRYPTED_SINGLE_MESSAGE_BODY_MAX_SIZE;
    int spillover               = charactersSpent - charactersInFirstRecord;
    int spilloverMessagesSpent  = spillover / SmsTransportDetails.MULTI_MESSAGE_MAX_BYTES;

    if ((spillover % SmsTransportDetails.MULTI_MESSAGE_MAX_BYTES) > 0)
      spilloverMessagesSpent++;

    int charactersRemaining = (SmsTransportDetails.MULTI_MESSAGE_MAX_BYTES * spilloverMessagesSpent) - spillover;

    return new CharacterState(spilloverMessagesSpent+1, charactersRemaining, SmsTransportDetails.MULTI_MESSAGE_MAX_BYTES);
  }

  @Override
  public CharacterState calculateCharacters(int charactersSpent) {
    if (charactersSpent <= SmsTransportDetails.ENCRYPTED_SINGLE_MESSAGE_BODY_MAX_SIZE){
      return calculateSingleRecordCharacters(charactersSpent);
    } else {
      return calculateMultiRecordCharacters(charactersSpent);
    }
  }
}
