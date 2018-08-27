package com.openchat.secureim.controllers;

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
  public void setKeys(@Auth Account account, @Valid PreKeyList preKeys)  {
    keys.store(account.getNumber(), account.getDeviceId(), preKeys.getLastResortKey(), preKeys.getKeys());
  }

  public List<PreKey> getKeys(Account account, String number, String relay) throws RateLimitExceededException
  {
    rateLimiters.getPreKeysLimiter().validate(account.getNumber() + "__" + number);

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
  public PreKey get(@Auth                Account account,
                    @PathParam("number") String number,
                    @QueryParam("relay") String relay)
      throws RateLimitExceededException
  {
    return getKeys(account, number, relay).get(0);
  }

  @Timed
  @GET
  @Path("/multikeys/{number}")
  @Produces(MediaType.APPLICATION_JSON)
  public List<PreKey> getMultiDevice(@Auth                Account account,
                                     @PathParam("number") String number,
                                     @QueryParam("relay") String relay)
      throws RateLimitExceededException
  {
    return getKeys(account, number, relay);
  }
}
