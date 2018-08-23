package com.openchat.secureim.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.validator.constraints.NotEmpty;
import com.openchat.secureim.util.ByteArrayAdapter;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class RelayMessage {

  @JsonProperty
  @NotEmpty
  private String destination;

  @JsonProperty
  @NotNull
  @JsonSerialize(using = ByteArrayAdapter.Serializing.class)
  @JsonDeserialize(using = ByteArrayAdapter.Deserializing.class)
  private byte[] outgoingMessageSignal;

  public RelayMessage() {}

  public RelayMessage(String destination, byte[] outgoingMessageSignal) {
    this.destination           = destination;
    this.outgoingMessageSignal = outgoingMessageSignal;
  }

  public String getDestination() {
    return destination;
  }

  public byte[] getOutgoingMessageSignal() {
    return outgoingMessageSignal;
  }
}
