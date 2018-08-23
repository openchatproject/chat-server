package com.openchat.secureim.controllers;

import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.entities.PreKey;
import com.openchat.secureim.entities.PreKeyList;
import com.openchat.secureim.federation.FederatedClientManager;
import com.openchat.secureim.federation.NoSuchPeerException;
import com.openchat.secureim.limits.RateLimiters;
import com.openchat.secureim.storage.Account;
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

  private final Logger logger = LoggerFactory.getLogger(AccountController.class);

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
    keys.store(account.getNumber(), preKeys.getLastResortKey(), preKeys.getKeys());
  }

  @Timed
  @GET
  @Path("/{number}")
  @Produces(MediaType.APPLICATION_JSON)
  public PreKey get(@Auth                Account account,
                    @PathParam("number") String number,
                    @QueryParam("relay") String relay)
      throws RateLimitExceededException
  {
    rateLimiters.getPreKeysLimiter().validate(account.getNumber() + "__" + number);

    try {
      PreKey key;

      if (relay == null) key = keys.get(number);
      else               key = federatedClientManager.getClient(relay).getKey(number);

      if (key == null) throw new WebApplicationException(Response.status(404).build());
      else             return key;
    } catch (NoSuchPeerException e) {
      logger.info("No peer: " + relay);
      throw new WebApplicationException(Response.status(404).build());
    }
  }

}
