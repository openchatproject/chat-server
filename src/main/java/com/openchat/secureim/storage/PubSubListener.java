package com.openchat.secureim.storage;

public interface PubSubListener {

  public void onPubSubMessage(PubSubMessage outgoingMessage);

}
