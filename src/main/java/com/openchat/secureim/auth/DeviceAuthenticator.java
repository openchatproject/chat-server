package com.openchat.secureim.auth;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.auth.basic.BasicCredentials;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Meter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.storage.Device;
import com.openchat.secureim.storage.AccountsManager;

import java.util.concurrent.TimeUnit;

public class DeviceAuthenticator implements Authenticator<BasicCredentials, Device> {

  private final Meter authenticationFailedMeter    = Metrics.newMeter(DeviceAuthenticator.class,
                                                                      "authentication", "failed",
                                                                      TimeUnit.MINUTES);

  private final Meter authenticationSucceededMeter = Metrics.newMeter(DeviceAuthenticator.class,
                                                                      "authentication", "succeeded",
                                                                      TimeUnit.MINUTES);

  private final Logger logger = LoggerFactory.getLogger(DeviceAuthenticator.class);

  private final AccountsManager accountsManager;

  public DeviceAuthenticator(AccountsManager accountsManager) {
    this.accountsManager = accountsManager;
  }

  @Override
  public Optional<Device> authenticate(BasicCredentials basicCredentials)
      throws AuthenticationException
  {
    AuthorizationHeader authorizationHeader;
    try {
      authorizationHeader = AuthorizationHeader.fromUserAndPassword(basicCredentials.getUsername(), basicCredentials.getPassword());
    } catch (InvalidAuthorizationHeaderException iahe) {
      return Optional.absent();
    }
    Optional<Device> device = accountsManager.get(authorizationHeader.getNumber(), authorizationHeader.getDeviceId());

    if (!device.isPresent()) {
      return Optional.absent();
    }

    if (device.get().getAuthenticationCredentials().verify(basicCredentials.getPassword())) {
      authenticationSucceededMeter.mark();
      return device;
    }

    authenticationFailedMeter.mark();
    return Optional.absent();
  }
}
