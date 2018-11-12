package com.openchat.imservice.api.messages.multidevice;

import java.util.LinkedList;
import java.util.List;

public class ReadMessage {

  private final List<Long> timestamps;

  public ReadMessage(long timestamp) {
    this.timestamps = new LinkedList<>();
    this.timestamps.add(timestamp);
  }

  public ReadMessage(List<Long> timestamps) {
    this.timestamps = timestamps;
  }

  public List<Long> getTimestamps() {
    return timestamps;
  }

}
