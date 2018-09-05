package com.openchat.secureim.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class IncomingMessageList {

  @JsonProperty
  @NotNull
  @Valid
  private List<IncomingMessage> messages;

  @JsonProperty
  private String relay;

  @JsonProperty
  private long timestamp;

  public IncomingMessageList() {}

  public List<IncomingMessage> getMessages() {
    return messages;
  }

  public String getRelay() {
    return relay;
  }

  public void setRelay(String relay) {
    this.relay = relay;
  }

  public long getTimestamp() {
    return timestamp;
  }
}
