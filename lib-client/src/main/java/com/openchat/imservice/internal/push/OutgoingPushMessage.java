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

  public OutgoingPushMessage(OpenchatServiceAddress address, int deviceId, PushBody body) {
    this.type                      = body.getType();
    this.destinationDeviceId       = deviceId;
    this.destinationRegistrationId = body.getRemoteRegistrationId();
    this.body                      = Base64.encodeBytes(body.getBody());
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
