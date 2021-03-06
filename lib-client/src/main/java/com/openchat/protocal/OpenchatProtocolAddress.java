package com.openchat.protocal;

public class OpenchatProtocolAddress {

  private final String name;
  private final int    deviceId;

  public OpenchatProtocolAddress(String name, int deviceId) {
    this.name     = name;
    this.deviceId = deviceId;
  }

  public String getName() {
    return name;
  }

  public int getDeviceId() {
    return deviceId;
  }

  @Override
  public String toString() {
    return name + ":" + deviceId;
  }

  @Override
  public boolean equals(Object other) {
    if (other == null)                       return false;
    if (!(other instanceof OpenchatProtocolAddress)) return false;

    OpenchatProtocolAddress that = (OpenchatProtocolAddress)other;
    return this.name.equals(that.name) && this.deviceId == that.deviceId;
  }

  @Override
  public int hashCode() {
    return this.name.hashCode() ^ this.deviceId;
  }
}
