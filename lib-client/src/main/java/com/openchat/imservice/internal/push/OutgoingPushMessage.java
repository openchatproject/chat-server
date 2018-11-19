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
  private String content;
  @JsonProperty
  private boolean silent;

  public OutgoingPushMessage(int type,
                             int destinationDeviceId,
                             int destinationRegistrationId,
                             String content,
                             boolean silent)
  {
    this.type                      = type;
    this.destinationDeviceId       = destinationDeviceId;
    this.destinationRegistrationId = destinationRegistrationId;
    this.content                   = content;
    this.silent                    = silent;
  }
}
