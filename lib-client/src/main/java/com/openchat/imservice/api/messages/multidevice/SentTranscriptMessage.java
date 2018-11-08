package com.openchat.imservice.api.messages.multidevice;

import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.messages.OpenchatServiceDataMessage;

public class SentTranscriptMessage {

  private final Optional<String>      destination;
  private final long                  timestamp;
  private final OpenchatServiceDataMessage message;

  public SentTranscriptMessage(String destination, long timestamp, OpenchatServiceDataMessage message) {
    this.destination = Optional.of(destination);
    this.timestamp   = timestamp;
    this.message     = message;
  }

  public SentTranscriptMessage(long timestamp, OpenchatServiceDataMessage message) {
    this.destination = Optional.absent();
    this.timestamp   = timestamp;
    this.message     = message;
  }

  public Optional<String> getDestination() {
    return destination;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public OpenchatServiceDataMessage getMessage() {
    return message;
  }
}
