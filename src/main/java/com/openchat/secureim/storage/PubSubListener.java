package com.openchat.secureim.storage;

import static com.openchat.secureim.storage.PubSubProtos.PubSubMessage;

public interface PubSubListener {

  public void onPubSubMessage(PubSubMessage outgoingMessage);

}
