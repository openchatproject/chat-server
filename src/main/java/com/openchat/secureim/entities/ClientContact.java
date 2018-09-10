package com.openchat.secureim.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.openchat.secureim.util.ByteArrayAdapter;

import java.util.Arrays;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ClientContact {

  @JsonSerialize(using = ByteArrayAdapter.Serializing.class)
  @JsonDeserialize(using = ByteArrayAdapter.Deserializing.class)
  @JsonProperty
  private byte[]  token;

  private String  relay;
  private boolean inactive;
  private boolean supportsSms;

  public ClientContact(byte[] token, String relay, boolean supportsSms) {
    this.token       = token;
    this.relay       = relay;
    this.supportsSms = supportsSms;
  }

  public ClientContact() {}

  public byte[] getToken() {
    return token;
  }

  public String getRelay() {
    return relay;
  }

  public void setRelay(String relay) {
    this.relay = relay;
  }

  public boolean isSupportsSms() {
    return supportsSms;
  }

  public boolean isInactive() {
    return inactive;
  }

  public void setInactive(boolean inactive) {
    this.inactive = inactive;
  }

//  public String toString() {
//    return new Gson().toJson(this);
//  }

  @Override
  public boolean equals(Object other) {
    if (other == null) return false;
    if (!(other instanceof ClientContact)) return false;

    ClientContact that = (ClientContact)other;

    return
        Arrays.equals(this.token, that.token) &&
        this.supportsSms == that.supportsSms &&
        this.inactive == that.inactive &&
        (this.relay == null ? (that.relay == null) : this.relay.equals(that.relay));
  }

  public int hashCode() {
    return Arrays.hashCode(this.token);
  }

}
