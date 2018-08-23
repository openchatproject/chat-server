package com.openchat.secureim.federation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.configuration.FederationConfiguration;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class FederatedClientManager {

  private final Logger logger = LoggerFactory.getLogger(FederatedClientManager.class);

  private final HashMap<String, FederatedClient> clients = new HashMap<>();

  public FederatedClientManager(FederationConfiguration federationConfig)
      throws IOException
  {
    List<FederatedPeer> peers    = federationConfig.getPeers();
    String              identity = federationConfig.getName();

    if (peers != null) {
      for (FederatedPeer peer : peers) {
        logger.info("Adding peer: " + peer.getName());
        clients.put(peer.getName(), new FederatedClient(identity, peer));
      }
    }
  }

  public FederatedClient getClient(String name) throws NoSuchPeerException {
    FederatedClient client = clients.get(name);

    if (client == null) {
      throw new NoSuchPeerException(name);
    }

    return client;
  }

  public List<FederatedClient> getClients() {
    return new LinkedList<>(clients.values());
  }

}
