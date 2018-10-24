package com.openchat.secureim.auth;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.dropwizard.simpleauth.Authenticator;
import com.openchat.secureim.configuration.FederationConfiguration;
import com.openchat.secureim.federation.FederatedPeer;
import com.openchat.secureim.util.Constants;

import java.util.List;

import static com.codahale.metrics.MetricRegistry.name;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.basic.BasicCredentials;


public class FederatedPeerAuthenticator implements Authenticator<BasicCredentials, FederatedPeer> {

  private final MetricRegistry metricRegistry = SharedMetricRegistries.getOrCreate(Constants.METRICS_NAME);

  private final Meter authenticationFailedMeter    = metricRegistry.meter(name(getClass(),
                                                                               "authentication",
                                                                               "failed"));

  private final Meter authenticationSucceededMeter = metricRegistry.meter(name(getClass(),
                                                                               "authentication",
                                                                               "succeeded"));

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
