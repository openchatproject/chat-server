package com.openchat.secureim.controllers;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.entities.PreKey;
import com.openchat.secureim.entities.PreKeyList;
import com.openchat.secureim.entities.UnstructuredPreKeyList;
import com.openchat.secureim.federation.FederatedClientManager;
import com.openchat.secureim.federation.NoSuchPeerException;
import com.openchat.secureim.limits.RateLimiters;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.Device;
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

@Path("/v1/keys")
public class KeysController {

  private final Logger logger = LoggerFactory.getLogger(KeysController.class);

  private final RateLimiters           rateLimiters;
  private final Keys                   keys;
  private final FederatedClientManager federatedClientManager;

  public KeysController(RateLimiters rateLimiters, Keys keys,
                        FederatedClientManager federatedClientManager)
  {
    this.rateLimiters           = rateLimiters;
    this.keys                   = keys;
    this.federatedClientManager = federatedClientManager;
  }

  @Timed
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  public void setKeys(@Auth Account account, @Valid PreKeyList preKeys)  {
    Device device = account.getAuthenticatedDevice().get();
    keys.store(account.getNumber(), device.getId(), preKeys.getKeys(), preKeys.getLastResortKey());
  }

  @Timed
  @GET
  @Path("/{number}/{device_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public UnstructuredPreKeyList getDeviceKey(@Auth                   Account account,
                                             @PathParam("number")    String number,
                                             @PathParam("device_id") String deviceId,
                                             @QueryParam("relay")    Optional<String> relay)
      throws RateLimitExceededException
  {
    try {
      if (account.isRateLimited()) {
        rateLimiters.getPreKeysLimiter().validate(account.getNumber() +  "__" + number + "." + deviceId);
      }

      Optional<UnstructuredPreKeyList> results;

      if (!relay.isPresent()) results = getLocalKeys(number, deviceId);
      else                    results = federatedClientManager.getClient(relay.get()).getKeys(number, deviceId);

      if (results.isPresent()) return results.get();
      else                     throw new WebApplicationException(Response.status(404).build());
    } catch (NoSuchPeerException e) {
      throw new WebApplicationException(Response.status(404).build());
    }
  }

  @Timed
  @GET
  @Path("/{number}")
  @Produces(MediaType.APPLICATION_JSON)
  public PreKey get(@Auth                Account account,
                    @PathParam("number") String number,
                    @QueryParam("relay") Optional<String> relay)
      throws RateLimitExceededException
  {
    UnstructuredPreKeyList results = getDeviceKey(account, number, String.valueOf(Device.MASTER_ID), relay);
    return results.getKeys().get(0);
  }

  private Optional<UnstructuredPreKeyList> getLocalKeys(String number, String deviceId) {
    try {
      if (deviceId.equals("*")) {
        return keys.get(number);
      }

      Optional<PreKey> targetKey = keys.get(number, Long.parseLong(deviceId));

      if (targetKey.isPresent()) return Optional.of(new UnstructuredPreKeyList(targetKey.get()));
      else                       return Optional.absent();
    } catch (NumberFormatException e) {
      throw new WebApplicationException(Response.status(422).build());
    }
  }
}
