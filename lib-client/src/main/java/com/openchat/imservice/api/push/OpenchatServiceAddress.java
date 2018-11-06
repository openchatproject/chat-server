package com.openchat.imservice.api.push;

import com.openchat.protocal.util.guava.Optional;

public class OpenchatServiceAddress {

  public static final int DEFAULT_DEVICE_ID = 1;

  private final String e164number;
  private final Optional<String> relay;

  
  public OpenchatServiceAddress(String e164number, Optional<String> relay) {
    this.e164number  = e164number;
    this.relay       = relay;
  }

  public OpenchatServiceAddress(String e164number) {
    this(e164number, Optional.<String>absent());
  }

  public String getNumber() {
    return e164number;
  }

  public Optional<String> getRelay() {
    return relay;
  }

  @Override
  public boolean equals(Object other) {
    if (other == null || !(other instanceof OpenchatServiceAddress)) return false;

    OpenchatServiceAddress that = (OpenchatServiceAddress)other;

    return equals(this.e164number, that.e164number) &&
           equals(this.relay, that.relay);
  }

  @Override
  public int hashCode() {
    int hashCode = 0;

    if (this.e164number != null) hashCode ^= this.e164number.hashCode();
    if (this.relay.isPresent())  hashCode ^= this.relay.get().hashCode();

    return hashCode;
  }

  private boolean equals(String one, String two) {
    if (one == null) return two == null;
    return one.equals(two);
  }

  private boolean equals(Optional<String> one, Optional<String> two) {
    if (one.isPresent()) return two.isPresent() && one.get().equals(two.get());
    else                 return !two.isPresent();
  }
}
