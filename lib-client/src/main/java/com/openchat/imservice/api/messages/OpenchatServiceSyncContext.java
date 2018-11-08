package com.openchat.imservice.api.messages;

public class OpenchatServiceSyncContext {

  private final String destination;
  private final long   timestamp;

  public OpenchatServiceSyncContext(String destination, long timestamp) {
    this.destination = destination;
    this.timestamp   = timestamp;
  }

  public String getDestination() {
    return destination;
  }

  public long getTimestamp() {
    return timestamp;
  }
}
