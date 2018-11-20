package com.openchat.imservice.api.messages;

import java.util.List;

public class OpenchatServiceReceiptMessage {

  public enum Type {
    UNKNOWN, DELIVERY, READ
  }

  private final Type       type;
  private final List<Long> timestamps;
  private final long       when;

  public OpenchatServiceReceiptMessage(Type type, List<Long> timestamps, long when) {
    this.type       = type;
    this.timestamps = timestamps;
    this.when       = when;
  }

  public Type getType() {
    return type;
  }

  public List<Long> getTimestamps() {
    return timestamps;
  }

  public long getWhen() {
    return when;
  }

  public boolean isDeliveryReceipt() {
    return type == Type.DELIVERY;
  }

  public boolean isReadReceipt() {
    return type == Type.READ;
  }
}
