package com.openchat.secureim.controllers;

import com.amazonaws.HttpMethod;
import com.google.common.base.Optional;
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
import com.openchat.secureim.entities.MessageResponse;
import com.openchat.secureim.entities.RelayMessage;
import com.openchat.secureim.entities.UnstructuredPreKeyList;
import com.openchat.secureim.federation.FederatedPeer;
import com.openchat.secureim.push.PushSender;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.Device;
import com.openchat.secureim.storage.AccountsManager;
import com.openchat.secureim.storage.Keys;
import com.openchat.secureim.util.Pair;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
  public UnstructuredPreKeyList getKey(@Auth                FederatedPeer peer,
                                       @PathParam("number") String number)
  {
    Optional<Account> account = accounts.getAccount(number);
    UnstructuredPreKeyList keyList = null;
    if (account.isPresent())
      keyList = keys.get(number, account.get());
    if (!account.isPresent() || keyList.getKeys().isEmpty())
      throw new WebApplicationException(Response.status(404).build());
    return keyList;
  }

  @Timed
  @PUT
  @Path("/message")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public MessageResponse relayMessage(@Auth FederatedPeer peer, @Valid List<RelayMessage> messages)
      throws IOException
  {
    try {
      Map<String, Set<Long>> localDestinations = new HashMap<>();
      for (RelayMessage message : messages) {
        Set<Long> deviceIds = localDestinations.get(message.getDestination());
        if (deviceIds == null) {
          deviceIds = new HashSet<>();
          localDestinations.put(message.getDestination(), deviceIds);
        }
        deviceIds.add(message.getDestinationDeviceId());
      }

      Pair<Map<String, Account>, List<String>> accountsForDevices = accounts.getAccountsForDevices(localDestinations);

      Map<String, Account> localAccounts         = accountsForDevices.first();
      List<String>         numbersMissingDevices = accountsForDevices.second();
      List<String>         success               = new LinkedList<>();
      List<String>         failure               = new LinkedList<>(numbersMissingDevices);

      for (RelayMessage message : messages) {
        Account destinationAccount = localAccounts.get(message.getDestination());
        if (destinationAccount == null)
          continue;
        Device device = destinationAccount.getDevice(message.getDestinationDeviceId());
        OutgoingMessageSignal signal = OutgoingMessageSignal.parseFrom(message.getOutgoingMessageSignal())
                                                            .toBuilder()
                                                            .setRelay(peer.getName())
                                                            .build();
        try {
          pushSender.sendMessage(device, signal);
          success.add(device.getBackwardsCompatibleNumberEncoding());
        } catch (NoSuchUserException e) {
          logger.info("No such user", e);
          failure.add(device.getBackwardsCompatibleNumberEncoding());
        }
      }

      return new MessageResponse(success, failure, numbersMissingDevices);
    } catch (InvalidProtocolBufferException ipe) {
      logger.warn("ProtoBuf", ipe);
      throw new WebApplicationException(Response.status(400).build());
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
    List<Device>         numberList    = accounts.getAllMasterDevices(offset, ACCOUNT_CHUNK_SIZE);
    List<ClientContact> clientContacts = new LinkedList<>();

    for (Device device : numberList) {
      byte[]        token         = Util.getContactToken(device.getNumber());
      ClientContact clientContact = new ClientContact(token, null, device.getSupportsSms());

      if (!device.isActive())
        clientContact.setInactive(true);

      clientContacts.add(clientContact);
    }

    return new ClientContacts(clientContacts);
  }
}
