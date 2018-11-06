package com.openchat.imservice.api.push;

public class OpenchatServiceAddress {

  public static final int DEFAULT_DEVICE_ID = 1;

  private final long   recipientId;
  private final String e164number;
  private final String relay;

  
  public OpenchatServiceAddress(long recipientId, String e164number, String relay) {
    this.recipientId = recipientId;
    this.e164number  = e164number;
    this.relay       = relay;
  }

  public String getNumber() {
    return e164number;
  }

  public String getRelay() {
    return relay;
  }

  public long getRecipientId() {
    return recipientId;
  }

  @Override
  public boolean equals(Object other) {
    if (other == null || !(other instanceof OpenchatServiceAddress)) return false;

    OpenchatServiceAddress that = (OpenchatServiceAddress)other;

    return this.recipientId == that.recipientId &&
           equals(this.e164number, that.e164number) &&
           equals(this.relay, that.relay);
  }

  @Override
  public int hashCode() {
    int hashCode = (int)this.recipientId;

    if (this.e164number != null) hashCode ^= this.e164number.hashCode();
    if (this.relay != null)      hashCode ^= this.relay.hashCode();

    return hashCode;
  }

  private boolean equals(String one, String two) {
    if (one == null) return two == null;
    return one.equals(two);
  }
}
