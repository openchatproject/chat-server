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

  public IncomingMessageList() {}

  public List<IncomingMessage> getMessages() {
    return messages;
  }
}
