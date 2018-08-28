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
import com.openchat.secureim.storage.Device;
import com.openchat.secureim.storage.AccountsManager;
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
import java.util.List;

@Path("/v1/keys")
public class KeysController {

  private final Logger logger = LoggerFactory.getLogger(KeysController.class);

  private final RateLimiters           rateLimiters;
  private final Keys                   keys;
  private final AccountsManager        accountsManager;
  private final FederatedClientManager federatedClientManager;

  public KeysController(RateLimiters rateLimiters, Keys keys, AccountsManager accountsManager,
                        FederatedClientManager federatedClientManager)
  {
    this.rateLimiters           = rateLimiters;
    this.keys                   = keys;
    this.accountsManager        = accountsManager;
    this.federatedClientManager = federatedClientManager;
  }

  @Timed
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  public void setKeys(@Auth Device device, @Valid PreKeyList preKeys)  {
    keys.store(device.getNumber(), device.getDeviceId(), preKeys.getLastResortKey(), preKeys.getKeys());
  }

  private List<PreKey> getKeys(Device device, String number, String relay) throws RateLimitExceededException
  {
    rateLimiters.getPreKeysLimiter().validate(device.getNumber() + "__" + number);

    try {
      UnstructuredPreKeyList keyList;

      if (relay == null) {
        keyList = keys.get(number, accountsManager.getAllByNumber(number));
      } else {
        keyList = federatedClientManager.getClient(relay).getKeys(number);
      }

      if (keyList == null || keyList.getKeys().isEmpty()) throw new WebApplicationException(Response.status(404).build());
      else                                                return keyList.getKeys();
    } catch (NoSuchPeerException e) {
      logger.info("No peer: " + relay);
      throw new WebApplicationException(Response.status(404).build());
    }
  }

  @Timed
  @GET
  @Path("/{number}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response get(@Auth Device device,
                      @PathParam("number")     String number,
                      @QueryParam("multikeys") Optional<String> multikey,
                      @QueryParam("relay")     String relay)
      throws RateLimitExceededException
  {
    if (!multikey.isPresent())
      return Response.ok(getKeys(device, number, relay).get(0)).type(MediaType.APPLICATION_JSON).build();
    else
      return Response.ok(getKeys(device, number, relay)).type(MediaType.APPLICATION_JSON).build();
  }
}
