package com.openchat.secureim.auth;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.dropwizard.simpleauth.Authenticator;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.AccountsManager;
import com.openchat.secureim.storage.Device;
import com.openchat.secureim.util.Constants;
import com.openchat.secureim.util.Util;

import static com.codahale.metrics.MetricRegistry.name;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.basic.BasicCredentials;

public class AccountAuthenticator implements Authenticator<BasicCredentials, Account> {

  private final MetricRegistry metricRegistry               = SharedMetricRegistries.getOrCreate(Constants.METRICS_NAME);
  private final Meter          authenticationFailedMeter    = metricRegistry.meter(name(getClass(), "authentication", "failed"   ));
  private final Meter          authenticationSucceededMeter = metricRegistry.meter(name(getClass(), "authentication", "succeeded"));

  private final Logger logger = LoggerFactory.getLogger(AccountAuthenticator.class);

  private final AccountsManager accountsManager;

  public AccountAuthenticator(AccountsManager accountsManager) {
    this.accountsManager = accountsManager;
  }

  @Override
  public Optional<Account> authenticate(BasicCredentials basicCredentials)
      throws AuthenticationException
  {
    try {
      AuthorizationHeader authorizationHeader = AuthorizationHeader.fromUserAndPassword(basicCredentials.getUsername(), basicCredentials.getPassword());
      Optional<Account>   account             = accountsManager.get(authorizationHeader.getNumber());

      if (!account.isPresent()) {
        return Optional.absent();
      }

      Optional<Device> device = account.get().getDevice(authorizationHeader.getDeviceId());

      if (!device.isPresent()) {
        return Optional.absent();
      }

      if (device.get().getAuthenticationCredentials().verify(basicCredentials.getPassword())) {
        authenticationSucceededMeter.mark();
        account.get().setAuthenticatedDevice(device.get());
        updateLastSeen(account.get(), device.get());
        return account;
      }

      authenticationFailedMeter.mark();
      return Optional.absent();
    } catch (InvalidAuthorizationHeaderException iahe) {
      return Optional.absent();
    }
  }

  private void updateLastSeen(Account account, Device device) {
    if (device.getLastSeen() != Util.todayInMillis()) {
      device.setLastSeen(Util.todayInMillis());
      accountsManager.update(account);
    }
  }

}
