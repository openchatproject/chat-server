package com.openchat.imservice.api.messages.multidevice;

import com.openchat.protocal.util.guava.Optional;

public class ConfigurationMessage {

  private final Optional<Boolean> readReceipts;

  public ConfigurationMessage(Optional<Boolean> readReceipts) {
    this.readReceipts = readReceipts;
  }

  public Optional<Boolean> getReadReceipts() {
    return readReceipts;
  }
}
