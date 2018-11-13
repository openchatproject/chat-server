package com.openchat.imservice.internal.push;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OutgoingPushMessage {

  @JsonProperty
  private int    type;
  @JsonProperty
  private int    destinationDeviceId;
  @JsonProperty
  private int    destinationRegistrationId;
  @JsonProperty
  private String body;
  @JsonProperty
  private String content;

  public OutgoingPushMessage(int type,
                             int destinationDeviceId,
                             int destinationRegistrationId,
                             String legacyMessage, String content)
  {
    this.type                      = type;
    this.destinationDeviceId       = destinationDeviceId;
    this.destinationRegistrationId = destinationRegistrationId;
    this.body                      = legacyMessage;
    this.content                   = content;
  }
}
