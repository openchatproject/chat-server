package com.openchat.imservice.internal.contacts.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.openchat.imservice.internal.util.Hex;

public class DiscoveryRequest {

  @JsonProperty
  private int addressCount;

  @JsonProperty
  private byte[] requestId;

  @JsonProperty
  private byte[] iv;

  @JsonProperty
  private byte[] data;

  @JsonProperty
  private byte[] mac;

  public DiscoveryRequest() {

  }

  public DiscoveryRequest(int addressCount, byte[] requestId, byte[] iv, byte[] data, byte[] mac) {
    this.addressCount = addressCount;
    this.requestId    = requestId;
    this.iv           = iv;
    this.data         = data;
    this. mac         = mac;
  }

  public byte[] getRequestId() {
    return requestId;
  }

  public byte[] getIv() {
    return iv;
  }

  public byte[] getData() {
    return data;
  }

  public byte[] getMac() {
    return mac;
  }

  public int getAddressCount() {
    return addressCount;
  }

  public String toString() {
    return "{ addressCount: " + addressCount + ", ticket: " + Hex.toString(requestId) + ", iv: " + Hex.toString(iv) + ", data: " + Hex.toString(data) + ", mac: " + Hex.toString(mac) + "}";
  }

}
