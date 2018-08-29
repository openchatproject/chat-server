package com.openchat.secureim.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AcknowledgeWebsocketMessage extends IncomingWebsocketMessage {

  @JsonProperty
  private long id;

  public long getId() {
    return id;
  }

}
