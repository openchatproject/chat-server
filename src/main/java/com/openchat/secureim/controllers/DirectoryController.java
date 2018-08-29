package com.openchat.secureim.controllers;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.entities.ClientContact;
import com.openchat.secureim.entities.ClientContactTokens;
import com.openchat.secureim.entities.ClientContacts;
import com.openchat.secureim.limits.RateLimiters;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.DirectoryManager;
import com.openchat.secureim.util.Base64;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Path("/v1/directory")
public class DirectoryController {

  private final Logger logger = LoggerFactory.getLogger(DirectoryController.class);

  private final RateLimiters     rateLimiters;
  private final DirectoryManager directory;

  public DirectoryController(RateLimiters rateLimiters, DirectoryManager directory) {
    this.directory    = directory;
    this.rateLimiters = rateLimiters;
  }

  @Timed()
  @GET
  @Path("/{token}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getTokenPresence(@Auth Account account, @PathParam("token") String token)
      throws RateLimitExceededException
  {
    rateLimiters.getContactsLimiter().validate(account.getNumber());

    try {
      Optional<ClientContact> contact = directory.get(Base64.decodeWithoutPadding(token));

      if (contact.isPresent()) return Response.ok().entity(contact.get()).build();
      else                     return Response.status(404).build();

    } catch (IOException e) {
      logger.info("Bad token", e);
      return Response.status(404).build();
    }
  }

  @Timed()
  @PUT
  @Path("/tokens")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public ClientContacts getContactIntersection(@Auth Account account, @Valid ClientContactTokens contacts)
      throws RateLimitExceededException
  {
    rateLimiters.getContactsLimiter().validate(account.getNumber(), contacts.getContacts().size());

    try {
      List<byte[]> tokens = new LinkedList<>();

      for (String encodedContact : contacts.getContacts()) {
        tokens.add(Base64.decodeWithoutPadding(encodedContact));
      }

      List<ClientContact> intersection = directory.get(tokens);
      return new ClientContacts(intersection);
    } catch (IOException e) {
      logger.info("Bad token", e);
      throw new WebApplicationException(Response.status(400).build());
    }
  }
}
