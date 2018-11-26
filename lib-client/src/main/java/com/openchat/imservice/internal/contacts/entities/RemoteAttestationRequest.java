package com.openchat.imservice.internal.contacts.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RemoteAttestationRequest {

  @JsonProperty
  private byte[] clientPublic;

  public RemoteAttestationRequest() {}

  public RemoteAttestationRequest(byte[] clientPublic) {
    this.clientPublic = clientPublic;
  }

  public byte[] getClientPublic() {
    return clientPublic;
  }

}
