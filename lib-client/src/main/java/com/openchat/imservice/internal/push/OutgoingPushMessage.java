package com.openchat.imservice.internal.push;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.openchat.imservice.api.push.OpenchatServiceAddress;
import com.openchat.imservice.internal.util.Base64;

public class OutgoingPushMessage {

  @JsonProperty
  private int    type;
  @JsonProperty
  private int    destinationDeviceId;
  @JsonProperty
  private int    destinationRegistrationId;
  @JsonProperty
  private String body;

  public OutgoingPushMessage(int type,
                             int destinationDeviceId,
                             int destinationRegistrationId,
                             String body)
  {
    this.type                      = type;
    this.destinationDeviceId       = destinationDeviceId;
    this.destinationRegistrationId = destinationRegistrationId;
    this.body                      = body;
  }

  public int getDestinationDeviceId() {
    return destinationDeviceId;
  }

  public String getBody() {
    return body;
  }

  public int getType() {
    return type;
  }

  public int getDestinationRegistrationId() {
    return destinationRegistrationId;
  }
}
