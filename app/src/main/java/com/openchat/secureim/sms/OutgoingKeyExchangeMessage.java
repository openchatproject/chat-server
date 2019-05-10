package com.openchat.secureim.sms;

import com.openchat.secureim.recipients.Recipients;

public class OutgoingKeyExchangeMessage extends OutgoingTextMessage {

  public OutgoingKeyExchangeMessage(Recipients recipients, String message) {
    super(recipients, message);
  }

  private OutgoingKeyExchangeMessage(OutgoingKeyExchangeMessage base, String body) {
    super(base, body);
  }

  @Override
  public boolean isKeyExchange() {
    return true;
  }

  @Override
  public OutgoingTextMessage withBody(String body) {
    return new OutgoingKeyExchangeMessage(this, body);
  }
}
