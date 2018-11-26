package com.openchat.imservice.internal.contacts.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.codec.binary.Hex;

public class DiscoveryResponse {

  @JsonProperty
  private byte[] iv;

  @JsonProperty
  private byte[] data;

  @JsonProperty
  private byte[] mac;

  public DiscoveryResponse() {}

  public DiscoveryResponse(byte[] iv, byte[] data, byte[] mac) {
    this.iv   = iv;
    this.data = data;
    this.mac  = mac;
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

  public String toString() {
    return "{iv: " + (iv == null ? null : Hex.encodeHexString(iv)) + ", data: " + (data == null ? null: Hex.encodeHexString(data)) + ", mac: " + (mac == null ? null :Hex.encodeHexString(mac)) + "}";
  }
}
