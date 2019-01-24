package com.openchat.secureim.sms;

import com.openchat.secureim.database.Address;
import com.openchat.libim.util.guava.Optional;
import com.openchat.imservice.api.messages.openchatServiceGroup;

public class IncomingJoinedMessage extends IncomingTextMessage {

  public IncomingJoinedMessage(Address sender) {
    super(sender, 1, System.currentTimeMillis(), null, Optional.<openchatServiceGroup>absent(), 0);
  }

  @Override
  public boolean isJoined() {
    return true;
  }

  @Override
  public boolean isSecureMessage() {
    return true;
  }

}
