package com.openchat.secureim.storage;


import com.google.common.base.Optional;
import com.openchat.secureim.entities.MessageProtos.OutgoingMessageSignal;
import com.openchat.secureim.entities.OutgoingMessageEntity;
import com.openchat.secureim.util.Pair;

import java.util.List;

public class MessagesManager {

  private final Messages messages;

  public MessagesManager(Messages messages) {
    this.messages = messages;
  }

  public int insert(String destination, long destinationDevice, OutgoingMessageSignal message) {
    return this.messages.store(message, destination, destinationDevice) + 1;
  }

  public List<OutgoingMessageEntity> getMessagesForDevice(String destination, long destinationDevice) {
    return this.messages.load(destination, destinationDevice);
  }

  public void clear(String destination) {
    this.messages.clear(destination);
  }

  public Optional<OutgoingMessageEntity> delete(String destination, long timestamp) {
    return Optional.fromNullable(this.messages.remove(destination, timestamp));
  }

  public void delete(long id) {
    this.messages.remove(id);
  }
}
