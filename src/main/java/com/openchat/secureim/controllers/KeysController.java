package com.openchat.secureim.controllers;

import com.google.common.base.Optional;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.entities.PreKeyCount;
import com.openchat.secureim.federation.FederatedClientManager;
import com.openchat.secureim.limits.RateLimiters;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.AccountsManager;
import com.openchat.secureim.storage.Device;
import com.openchat.secureim.storage.KeyRecord;
import com.openchat.secureim.storage.Keys;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import io.dropwizard.auth.Auth;

public class KeysController {

  private static final Logger logger = LoggerFactory.getLogger(KeysController.class);

  protected final RateLimiters           rateLimiters;
  protected final Keys                   keys;
  protected final AccountsManager        accounts;
  protected final FederatedClientManager federatedClientManager;

  public KeysController(RateLimiters rateLimiters, Keys keys, AccountsManager accounts,
                        FederatedClientManager federatedClientManager)
  {
    this.rateLimiters           = rateLimiters;
    this.keys                   = keys;
    this.accounts               = accounts;
    this.federatedClientManager = federatedClientManager;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public PreKeyCount getStatus(@Auth Account account) {
    int count = keys.getCount(account.getNumber(), account.getAuthenticatedDevice().get().getId());

    if (count > 0) {
      count = count - 1;
    }

    return new PreKeyCount(count);
  }

  protected TargetKeys getLocalKeys(String number, String deviceIdSelector)
      throws NoSuchUserException
  {
    Optional<Account> destination = accounts.get(number);

    if (!destination.isPresent() || !destination.get().isActive()) {
      throw new NoSuchUserException("Target account is inactive");
    }

    try {
      if (deviceIdSelector.equals("*")) {
        Optional<List<KeyRecord>> preKeys = keys.get(number);
        return new TargetKeys(destination.get(), preKeys);
      }

      long             deviceId     = Long.parseLong(deviceIdSelector);
      Optional<Device> targetDevice = destination.get().getDevice(deviceId);

      if (!targetDevice.isPresent() || !targetDevice.get().isActive()) {
        throw new NoSuchUserException("Target device is inactive.");
      }

      for (int i=0;i<20;i++) {
        try {
          Optional<List<KeyRecord>> preKeys = keys.get(number, deviceId);
          return new TargetKeys(destination.get(), preKeys);
        } catch (UnableToExecuteStatementException e) {
          logger.info(e.getMessage());
        }
      }

      throw new WebApplicationException(Response.status(500).build());
    } catch (NumberFormatException e) {
      throw new WebApplicationException(Response.status(422).build());
    }
  }


  public static class TargetKeys {
    private final Account         destination;
    private final Optional<List<KeyRecord>> keys;

    public TargetKeys(Account destination, Optional<List<KeyRecord>> keys) {
      this.destination = destination;
      this.keys        = keys;
    }

    public Optional<List<KeyRecord>> getKeys() {
      return keys;
    }

    public Account getDestination() {
      return destination;
    }
  }

}
