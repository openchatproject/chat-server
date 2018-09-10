package com.openchat.secureim.configuration;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.openchat.secureim.federation.FederatedPeer;

import java.util.List;

public class FederationConfiguration {

  @JsonProperty
  private List<FederatedPeer> peers;

  @JsonProperty
  private String name;

  public List<FederatedPeer> getPeers() {
    return peers;
  }

  public String getName() {
    return name;
  }
}
