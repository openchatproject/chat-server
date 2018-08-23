package com.openchat.secureim.configuration;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.openchat.secureim.federation.FederatedPeer;

import java.util.LinkedList;
import java.util.List;

public class FederationConfiguration {

  @JsonProperty
  private List<FederatedPeer> peers;

  @JsonProperty
  private String name;

  @JsonProperty
  private String herokuPeers;

  public List<FederatedPeer> getPeers() {
    if (peers != null) {
      return peers;
    }

    if (herokuPeers != null) {
      List<FederatedPeer> peers        = new LinkedList<>();
      JsonElement         root         = new JsonParser().parse(herokuPeers);
      JsonArray           peerElements = root.getAsJsonArray();

      for (JsonElement peer : peerElements) {
        String name                = peer.getAsJsonObject().get("name").getAsString();
        String url                 = peer.getAsJsonObject().get("url").getAsString();
        String authenticationToken = peer.getAsJsonObject().get("authenticationToken").getAsString();
        String certificate         = peer.getAsJsonObject().get("certificate").getAsString();

        peers.add(new FederatedPeer(name, url, authenticationToken, certificate));
      }

      return peers;
    }

    return peers;
  }

  public String getName() {
    return name;
  }
}
