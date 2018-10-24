package com.openchat.secureim.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AttachmentDescriptor {

  @JsonProperty
  private long id;

  @JsonProperty
  private String idString;

  @JsonProperty
  private String location;

  public AttachmentDescriptor(long id, String location) {
    this.id       = id;
    this.idString = String.valueOf(id);
    this.location = location;
  }

  public AttachmentDescriptor() {}

  public long getId() {
    return id;
  }

  public String getLocation() {
    return location;
  }

  public String getIdString() {
    return idString;
  }
}
