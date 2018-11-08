package com.openchat.imservice.api.messages;

import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.messages.multidevice.OpenchatServiceSyncMessage;

public class OpenchatServiceContent {

  private final Optional<OpenchatServiceDataMessage> message;
  private final Optional<OpenchatServiceSyncMessage> synchronizeMessage;

  public OpenchatServiceContent() {
    this.message            = Optional.absent();
    this.synchronizeMessage = Optional.absent();
  }

  public OpenchatServiceContent(OpenchatServiceDataMessage message) {
    this.message = Optional.fromNullable(message);
    this.synchronizeMessage = Optional.absent();
  }

  public OpenchatServiceContent(OpenchatServiceSyncMessage synchronizeMessage) {
    this.message            = Optional.absent();
    this.synchronizeMessage = Optional.fromNullable(synchronizeMessage);
  }

  public Optional<OpenchatServiceDataMessage> getDataMessage() {
    return message;
  }

  public Optional<OpenchatServiceSyncMessage> getSyncMessage() {
    return synchronizeMessage;
  }
}
