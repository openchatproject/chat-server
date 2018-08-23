package com.openchat.secureim.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class IncomingMessage {

  @JsonProperty
  private int    type;

  @JsonProperty
  @NotEmpty
  private String destination;

  @JsonProperty
  @NotEmpty
  private String body;

  @JsonProperty
  private String relay;

  @JsonProperty
  private long   timestamp;

  @JsonProperty
  private long   destinationDeviceId = 1;

  public String getDestination() {
    return destination;
  }

  public String getBody() {
    return body;
  }

  public int getType() {
    return type;
  }

  public String getRelay() {
    return relay;
  }

  public long getDestinationDeviceId() {
    return destinationDeviceId;
  }

  public void setDestinationDeviceId(long destinationDeviceId) {
    this.destinationDeviceId = destinationDeviceId;
  }
}
