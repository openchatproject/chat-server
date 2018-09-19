package com.openchat.secureim.controllers;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.entities.IncomingMessage;
import com.openchat.secureim.entities.IncomingMessageList;
import com.openchat.secureim.entities.MessageProtos.Envelope;
import com.openchat.secureim.entities.MessageResponse;
import com.openchat.secureim.entities.MismatchedDevices;
import com.openchat.secureim.entities.OutgoingMessageEntity;
import com.openchat.secureim.entities.OutgoingMessageEntityList;
import com.openchat.secureim.entities.SendMessageResponse;
import com.openchat.secureim.entities.StaleDevices;
import com.openchat.secureim.federation.FederatedClient;
import com.openchat.secureim.federation.FederatedClientManager;
import com.openchat.secureim.federation.NoSuchPeerException;
import com.openchat.secureim.limits.RateLimiters;
import com.openchat.secureim.push.NotPushRegisteredException;
import com.openchat.secureim.push.PushSender;
import com.openchat.secureim.push.ReceiptSender;
import com.openchat.secureim.push.TransientPushFailureException;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.AccountsManager;
import com.openchat.secureim.storage.Device;
import com.openchat.secureim.storage.MessagesManager;
import com.openchat.secureim.util.Base64;
import com.openchat.secureim.util.Util;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import io.dropwizard.auth.Auth;

@Path("/v1/messages")
public class MessageController {

  private final Logger logger = LoggerFactory.getLogger(MessageController.class);

  private final RateLimiters           rateLimiters;
  private final PushSender             pushSender;
  private final ReceiptSender          receiptSender;
  private final FederatedClientManager federatedClientManager;
  private final AccountsManager        accountsManager;
  private final MessagesManager        messagesManager;

  public MessageController(RateLimiters rateLimiters,
                           PushSender pushSender,
                           ReceiptSender receiptSender,
                           AccountsManager accountsManager,
                           MessagesManager messagesManager,
                           FederatedClientManager federatedClientManager)
  {
    this.rateLimiters           = rateLimiters;
    this.pushSender             = pushSender;
    this.receiptSender          = receiptSender;
    this.accountsManager        = accountsManager;
    this.messagesManager        = messagesManager;
    this.federatedClientManager = federatedClientManager;
  }

  @Timed
  @Path("/{destination}")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public SendMessageResponse sendMessage(@Auth                     Account source,
                                         @PathParam("destination") String destinationName,
                                         @Valid                    IncomingMessageList messages)
      throws IOException, RateLimitExceededException
  {
    rateLimiters.getMessagesLimiter().validate(source.getNumber() + "__" + destinationName);

    try {
      boolean isSyncMessage = source.getNumber().equals(destinationName);

      if (Util.isEmpty(messages.getRelay())) sendLocalMessage(source, destinationName, messages, isSyncMessage);
      else                                   sendRelayMessage(source, destinationName, messages, isSyncMessage);

      return new SendMessageResponse(!isSyncMessage && source.getActiveDeviceCount() > 1);
    } catch (NoSuchUserException e) {
      throw new WebApplicationException(Response.status(404).build());
    } catch (MismatchedDevicesException e) {
      throw new WebApplicationException(Response.status(409)
                                                .type(MediaType.APPLICATION_JSON_TYPE)
                                                .entity(new MismatchedDevices(e.getMissingDevices(),
                                                                              e.getExtraDevices()))
                                                .build());
    } catch (StaleDevicesException e) {
      throw new WebApplicationException(Response.status(410)
                                                .type(MediaType.APPLICATION_JSON)
                                                .entity(new StaleDevices(e.getStaleDevices()))
                                                .build());
    } catch (InvalidDestinationException e) {
      throw new WebApplicationException(Response.status(400).build());
    }
  }

  @Timed
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public MessageResponse sendMessageLegacy(@Auth Account source, @Valid IncomingMessageList messages)
      throws IOException, RateLimitExceededException
  {
    try {
      List<IncomingMessage> incomingMessages = messages.getMessages();
      validateLegacyDestinations(incomingMessages);

      messages.setRelay(incomingMessages.get(0).getRelay());
      sendMessage(source, incomingMessages.get(0).getDestination(), messages);

      return new MessageResponse(new LinkedList<String>(), new LinkedList<String>());
    } catch (ValidationException e) {
      throw new WebApplicationException(Response.status(422).build());
    }
  }

  @Timed
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public OutgoingMessageEntityList getPendingMessages(@Auth Account account) {
    return new OutgoingMessageEntityList(messagesManager.getMessagesForDevice(account.getNumber(),
                                                                              account.getAuthenticatedDevice()
                                                                                     .get().getId()));
  }

  @Timed
  @DELETE
  @Path("/{source}/{timestamp}")
  public void removePendingMessage(@Auth Account account,
                                   @PathParam("source") String source,
                                   @PathParam("timestamp") long timestamp)
      throws IOException
  {
    try {
      Optional<OutgoingMessageEntity> message = messagesManager.delete(account.getNumber(), source, timestamp);

      if (message.isPresent() && message.get().getType() != Envelope.Type.RECEIPT_VALUE) {
        receiptSender.sendReceipt(account,
                                  message.get().getSource(),
                                  message.get().getTimestamp(),
                                  Optional.fromNullable(message.get().getRelay()));
      }
    } catch (NoSuchUserException | NotPushRegisteredException | TransientPushFailureException e) {
      logger.warn("Sending delivery receipt", e);
    }
  }


  private void sendLocalMessage(Account source,
                                String destinationName,
                                IncomingMessageList messages,
                                boolean isSyncMessage)
      throws NoSuchUserException, MismatchedDevicesException, IOException, StaleDevicesException
  {
    Account destination;

    if (!isSyncMessage) destination = getDestinationAccount(destinationName);
    else                destination = source;

    validateCompleteDeviceList(destination, messages.getMessages(), isSyncMessage);
    validateRegistrationIds(destination, messages.getMessages());

    for (IncomingMessage incomingMessage : messages.getMessages()) {
      Optional<Device> destinationDevice = destination.getDevice(incomingMessage.getDestinationDeviceId());

      if (destinationDevice.isPresent()) {
        sendLocalMessage(source, destination, destinationDevice.get(), messages.getTimestamp(), incomingMessage);
      }
    }
  }

  private void sendLocalMessage(Account source,
                                Account destinationAccount,
                                Device destinationDevice,
                                long timestamp,
                                IncomingMessage incomingMessage)
      throws NoSuchUserException, IOException
  {
    try {
      Optional<byte[]> messageBody    = getMessageBody(incomingMessage);
      Optional<byte[]> messageContent = getMessageContent(incomingMessage);
      Envelope.Builder messageBuilder = Envelope.newBuilder();

      messageBuilder.setType(Envelope.Type.valueOf(incomingMessage.getType()))
                    .setSource(source.getNumber())
                    .setTimestamp(timestamp == 0 ? System.currentTimeMillis() : timestamp)
                    .setSourceDevice((int) source.getAuthenticatedDevice().get().getId());

      if (messageBody.isPresent()) {
        messageBuilder.setLegacyMessage(ByteString.copyFrom(messageBody.get()));
      }

      if (messageContent.isPresent()) {
        messageBuilder.setContent(ByteString.copyFrom(messageContent.get()));
      }

      if (source.getRelay().isPresent()) {
        messageBuilder.setRelay(source.getRelay().get());
      }

      pushSender.sendMessage(destinationAccount, destinationDevice, messageBuilder.build());
    } catch (NotPushRegisteredException e) {
      if (destinationDevice.isMaster()) throw new NoSuchUserException(e);
      else                              logger.debug("Not registered", e);
    } catch (TransientPushFailureException e) {
      if (destinationDevice.isMaster()) throw new IOException(e);
      else                              logger.debug("Transient failure", e);
    }
  }

  private void sendRelayMessage(Account source,
                                String destinationName,
                                IncomingMessageList messages,
                                boolean isSyncMessage)
      throws IOException, NoSuchUserException, InvalidDestinationException
  {
    if (isSyncMessage) throw new InvalidDestinationException("Transcript messages can't be relayed!");

    try {
      FederatedClient client = federatedClientManager.getClient(messages.getRelay());
      client.sendMessages(source.getNumber(), source.getAuthenticatedDevice().get().getId(),
                          destinationName, messages);
    } catch (NoSuchPeerException e) {
      throw new NoSuchUserException(e);
    }
  }

  private Account getDestinationAccount(String destination)
      throws NoSuchUserException
  {
    Optional<Account> account = accountsManager.get(destination);

    if (!account.isPresent() || !account.get().isActive()) {
      throw new NoSuchUserException(destination);
    }

    return account.get();
  }

  private void validateRegistrationIds(Account account, List<IncomingMessage> messages)
      throws StaleDevicesException
  {
    List<Long> staleDevices = new LinkedList<>();

    for (IncomingMessage message : messages) {
      Optional<Device> device = account.getDevice(message.getDestinationDeviceId());

      if (device.isPresent() &&
          message.getDestinationRegistrationId() > 0 &&
          message.getDestinationRegistrationId() != device.get().getRegistrationId())
      {
        staleDevices.add(device.get().getId());
      }
    }

    if (!staleDevices.isEmpty()) {
      throw new StaleDevicesException(staleDevices);
    }
  }

  private void validateCompleteDeviceList(Account account,
                                          List<IncomingMessage> messages,
                                          boolean isSyncMessage)
      throws MismatchedDevicesException
  {
    Set<Long> messageDeviceIds = new HashSet<>();
    Set<Long> accountDeviceIds = new HashSet<>();

    List<Long> missingDeviceIds = new LinkedList<>();
    List<Long> extraDeviceIds   = new LinkedList<>();

    for (IncomingMessage message : messages) {
      messageDeviceIds.add(message.getDestinationDeviceId());
    }

    for (Device device : account.getDevices()) {
      if (device.isActive() &&
          !(isSyncMessage && device.getId() == account.getAuthenticatedDevice().get().getId()))
      {
        accountDeviceIds.add(device.getId());

        if (!messageDeviceIds.contains(device.getId())) {
          missingDeviceIds.add(device.getId());
        }
      }
    }

    for (IncomingMessage message : messages) {
      if (!accountDeviceIds.contains(message.getDestinationDeviceId())) {
        extraDeviceIds.add(message.getDestinationDeviceId());
      }
    }

    if (!missingDeviceIds.isEmpty() || !extraDeviceIds.isEmpty()) {
      throw new MismatchedDevicesException(missingDeviceIds, extraDeviceIds);
    }
  }

  private void validateLegacyDestinations(List<IncomingMessage> messages)
      throws ValidationException
  {
    String destination = null;

    for (IncomingMessage message : messages) {
      if ((message.getDestination() == null) ||
          (destination != null && !destination.equals(message.getDestination())))
      {
        throw new ValidationException("Multiple account destinations!");
      }

      destination = message.getDestination();
    }
  }

  private Optional<byte[]> getMessageBody(IncomingMessage message) {
    if (Util.isEmpty(message.getBody())) return Optional.absent();

    try {
      return Optional.of(Base64.decode(message.getBody()));
    } catch (IOException ioe) {
      logger.debug("Bad B64", ioe);
      return Optional.absent();
    }
  }

  private Optional<byte[]> getMessageContent(IncomingMessage message) {
    if (Util.isEmpty(message.getContent())) return Optional.absent();

    try {
      return Optional.of(Base64.decode(message.getContent()));
    } catch (IOException ioe) {
      logger.debug("Bad B64", ioe);
      return Optional.absent();
    }
  }
}
