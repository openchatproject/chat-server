package com.openchat.secureim.auth;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.auth.basic.BasicCredentials;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Meter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.configuration.FederationConfiguration;
import com.openchat.secureim.federation.FederatedPeer;

import java.util.List;
import java.util.concurrent.TimeUnit;


public class FederatedPeerAuthenticator implements Authenticator<BasicCredentials, FederatedPeer> {

  private final Meter authenticationFailedMeter    = Metrics.newMeter(FederatedPeerAuthenticator.class,
                                                                      "authentication", "failed",
                                                                      TimeUnit.MINUTES);

  private final Meter authenticationSucceededMeter = Metrics.newMeter(FederatedPeerAuthenticator.class,
                                                                      "authentication", "succeeded",
                                                                      TimeUnit.MINUTES);

  private final Logger logger = LoggerFactory.getLogger(FederatedPeerAuthenticator.class);

  private final List<FederatedPeer> peers;

  public FederatedPeerAuthenticator(FederationConfiguration config) {
    this.peers = config.getPeers();
  }

  @Override
  public Optional<FederatedPeer> authenticate(BasicCredentials basicCredentials)
      throws AuthenticationException
  {

    if (peers == null) {
      authenticationFailedMeter.mark();
      return Optional.absent();
    }

    for (FederatedPeer peer : peers) {
      if (basicCredentials.getUsername().equals(peer.getName()) &&
          basicCredentials.getPassword().equals(peer.getAuthenticationToken()))
      {
        authenticationSucceededMeter.mark();
        return Optional.of(peer);
      }
    }

    authenticationFailedMeter.mark();
    return Optional.absent();
  }
}
