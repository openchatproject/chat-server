package com.openchat.secureim.controllers;

import com.codahale.metrics.annotation.Timed;
import com.openchat.secureim.entities.ProvisioningMessage;
import com.openchat.secureim.limits.RateLimiters;
import com.openchat.secureim.push.PushSender;
import com.openchat.secureim.push.WebsocketSender;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.util.Base64;
import com.openchat.secureim.websocket.InvalidWebsocketAddressException;
import com.openchat.secureim.websocket.ProvisioningAddress;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

import io.dropwizard.auth.Auth;

@Path("/v1/provisioning")
public class ProvisioningController {

  private final RateLimiters    rateLimiters;
  private final WebsocketSender websocketSender;

  public ProvisioningController(RateLimiters rateLimiters, PushSender pushSender) {
    this.rateLimiters    = rateLimiters;
    this.websocketSender = pushSender.getWebSocketSender();
  }

  @Timed
  @Path("/{destination}")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public void sendProvisioningMessage(@Auth                     Account source,
                                      @PathParam("destination") String destinationName,
                                      @Valid                    ProvisioningMessage message)
      throws RateLimitExceededException, InvalidWebsocketAddressException, IOException
  {
    rateLimiters.getMessagesLimiter().validate(source.getNumber());

    if (!websocketSender.sendProvisioningMessage(new ProvisioningAddress(destinationName),
                                                 Base64.decode(message.getBody())))
    {
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    }
  }
}
