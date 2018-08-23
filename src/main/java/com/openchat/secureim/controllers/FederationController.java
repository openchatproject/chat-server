package com.openchat.secureim.controllers;

import com.amazonaws.HttpMethod;
import com.google.protobuf.InvalidProtocolBufferException;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.entities.AccountCount;
import com.openchat.secureim.entities.AttachmentUri;
import com.openchat.secureim.entities.ClientContact;
import com.openchat.secureim.entities.ClientContacts;
import com.openchat.secureim.entities.MessageProtos.OutgoingMessageSignal;
import com.openchat.secureim.entities.PreKey;
import com.openchat.secureim.entities.RelayMessage;
import com.openchat.secureim.federation.FederatedPeer;
import com.openchat.secureim.push.PushSender;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.AccountsManager;
import com.openchat.secureim.storage.Keys;
import com.openchat.secureim.util.UrlSigner;
import com.openchat.secureim.util.Util;

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
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

@Path("/v1/federation")
public class FederationController {

  private final Logger logger = LoggerFactory.getLogger(FederationController.class);

  private static final int ACCOUNT_CHUNK_SIZE = 10000;

  private final PushSender      pushSender;
  private final Keys            keys;
  private final AccountsManager accounts;
  private final UrlSigner       urlSigner;

  public FederationController(Keys keys, AccountsManager accounts, PushSender pushSender, UrlSigner urlSigner) {
    this.keys       = keys;
    this.accounts   = accounts;
    this.pushSender = pushSender;
    this.urlSigner  = urlSigner;
  }

  @Timed
  @GET
  @Path("/attachment/{attachmentId}")
  @Produces(MediaType.APPLICATION_JSON)
  public AttachmentUri getSignedAttachmentUri(@Auth                      FederatedPeer peer,
                                              @PathParam("attachmentId") long attachmentId)
  {
    URL url = urlSigner.getPreSignedUrl(attachmentId, HttpMethod.GET);
    return new AttachmentUri(url);
  }

  @Timed
  @GET
  @Path("/key/{number}")
  @Produces(MediaType.APPLICATION_JSON)
  public PreKey getKey(@Auth                FederatedPeer peer,
                       @PathParam("number") String number)
  {
    PreKey preKey = keys.get(number);

    if (preKey == null) {
      throw new WebApplicationException(Response.status(404).build());
    }

    return preKey;
  }

  @Timed
  @PUT
  @Path("/message")
  @Consumes(MediaType.APPLICATION_JSON)
  public void relayMessage(@Auth FederatedPeer peer, @Valid RelayMessage message)
      throws IOException
  {
    try {
      OutgoingMessageSignal signal = OutgoingMessageSignal.parseFrom(message.getOutgoingMessageSignal())
                                                          .toBuilder()
                                                          .setRelay(peer.getName())
                                                          .build();

      pushSender.sendMessage(message.getDestination(), signal);
    } catch (InvalidProtocolBufferException ipe) {
      logger.warn("ProtoBuf", ipe);
      throw new WebApplicationException(Response.status(400).build());
    } catch (NoSuchUserException e) {
      logger.debug("No User", e);
      throw new WebApplicationException(Response.status(404).build());
    }
  }

  @Timed
  @GET
  @Path("/user_count")
  @Produces(MediaType.APPLICATION_JSON)
  public AccountCount getUserCount(@Auth FederatedPeer peer) {
    return new AccountCount((int)accounts.getCount());
  }

  @Timed
  @GET
  @Path("/user_tokens/{offset}")
  @Produces(MediaType.APPLICATION_JSON)
  public ClientContacts getUserTokens(@Auth                FederatedPeer peer,
                                      @PathParam("offset") int offset)
  {
    List<Account>       accountList    = accounts.getAll(offset, ACCOUNT_CHUNK_SIZE);
    List<ClientContact> clientContacts = new LinkedList<>();

    for (Account account : accountList) {
      byte[]        token         = Util.getContactToken(account.getNumber());
      ClientContact clientContact = new ClientContact(token, null, account.getSupportsSms());

      if (Util.isEmpty(account.getApnRegistrationId()) &&
          Util.isEmpty(account.getGcmRegistrationId()))
      {
        clientContact.setInactive(true);
      }

      clientContacts.add(clientContact);
    }

    return new ClientContacts(clientContacts);
  }
}
