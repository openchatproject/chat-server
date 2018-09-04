package com.openchat.secureim.controllers;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.entities.PreKeyResponseV1;
import com.openchat.secureim.entities.PreKeyStateV1;
import com.openchat.secureim.entities.PreKeyV1;
import com.openchat.secureim.federation.FederatedClientManager;
import com.openchat.secureim.federation.NoSuchPeerException;
import com.openchat.secureim.limits.RateLimiters;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.AccountsManager;
import com.openchat.secureim.storage.Device;
import com.openchat.secureim.storage.KeyRecord;
import com.openchat.secureim.storage.Keys;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

import io.dropwizard.auth.Auth;

@Path("/v1/keys")
public class KeysControllerV1 extends KeysController {

  private final Logger logger = LoggerFactory.getLogger(KeysControllerV1.class);

  public KeysControllerV1(RateLimiters rateLimiters, Keys keys, AccountsManager accounts,
                          FederatedClientManager federatedClientManager)
  {
    super(rateLimiters, keys, accounts, federatedClientManager);
  }

  @Timed
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  public void setKeys(@Auth Account account, @Valid PreKeyStateV1 preKeys)  {
    Device device      = account.getAuthenticatedDevice().get();
    String identityKey = preKeys.getLastResortKey().getIdentityKey();

    if (!identityKey.equals(account.getIdentityKey())) {
      account.setIdentityKey(identityKey);
      accounts.update(account);
    }

    keys.store(account.getNumber(), device.getId(), preKeys.getKeys(), preKeys.getLastResortKey());
  }

  @Timed
  @GET
  @Path("/{number}/{device_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Optional<PreKeyResponseV1> getDeviceKey(@Auth                   Account account,
                                                 @PathParam("number")    String number,
                                                 @PathParam("device_id") String deviceId,
                                                 @QueryParam("relay")    Optional<String> relay)
      throws RateLimitExceededException
  {
    try {
      if (account.isRateLimited()) {
        rateLimiters.getPreKeysLimiter().validate(account.getNumber() +  "__" + number + "." + deviceId);
      }

      if (relay.isPresent()) {
        return federatedClientManager.getClient(relay.get()).getKeysV1(number, deviceId);
      }

      TargetKeys targetKeys = getLocalKeys(number, deviceId);

      if (!targetKeys.getKeys().isPresent()) {
        return Optional.absent();
      }

      List<PreKeyV1> preKeys     = new LinkedList<>();
      Account        destination = targetKeys.getDestination();

      for (KeyRecord record : targetKeys.getKeys().get()) {
        Optional<Device> device = destination.getDevice(record.getDeviceId());
        if (device.isPresent() && device.get().isActive()) {
          preKeys.add(new PreKeyV1(record.getDeviceId(), record.getKeyId(),
                                   record.getPublicKey(), destination.getIdentityKey(),
                                   device.get().getRegistrationId()));
        }
      }

      if (preKeys.isEmpty()) return Optional.absent();
      else                   return Optional.of(new PreKeyResponseV1(preKeys));
    } catch (NoSuchPeerException | NoSuchUserException e) {
      throw new WebApplicationException(Response.status(404).build());
    }
  }

  @Timed
  @GET
  @Path("/{number}")
  @Produces(MediaType.APPLICATION_JSON)
  public Optional<PreKeyV1> get(@Auth                Account account,
                                @PathParam("number") String number,
                                @QueryParam("relay") Optional<String> relay)
      throws RateLimitExceededException
  {
    Optional<PreKeyResponseV1> results = getDeviceKey(account, number, String.valueOf(Device.MASTER_ID), relay);

    if (results.isPresent()) return Optional.of(results.get().getKeys().get(0));
    else                     return Optional.absent();
  }

}
