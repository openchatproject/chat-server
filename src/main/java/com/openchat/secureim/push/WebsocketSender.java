package com.openchat.secureim.push;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.openchat.secureim.entities.CryptoEncodingException;
import com.openchat.secureim.entities.EncryptedOutgoingMessage;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.Device;
import com.openchat.secureim.storage.PubSubManager;
import com.openchat.secureim.storage.PubSubMessage;
import com.openchat.secureim.storage.StoredMessages;
import com.openchat.secureim.util.Constants;
import com.openchat.secureim.websocket.WebsocketAddress;

import java.util.List;

import static com.codahale.metrics.MetricRegistry.name;

public class WebsocketSender {

  private final MetricRegistry metricRegistry = SharedMetricRegistries.getOrCreate(Constants.METRICS_NAME);
  private final Meter          onlineMeter    = metricRegistry.meter(name(getClass(), "online"));
  private final Meter          offlineMeter   = metricRegistry.meter(name(getClass(), "offline"));

  private final StoredMessages storedMessages;
  private final PubSubManager  pubSubManager;

  public WebsocketSender(StoredMessages storedMessages, PubSubManager pubSubManager) {
    this.storedMessages = storedMessages;
    this.pubSubManager  = pubSubManager;
  }

  public void sendMessage(Account account, Device device, EncryptedOutgoingMessage outgoingMessage)
      throws CryptoEncodingException
  {
    sendMessage(account, device, outgoingMessage.serialize());
  }

  private void sendMessage(Account account, Device device, String serializedMessage) {
    WebsocketAddress address       = new WebsocketAddress(account.getId(), device.getId());
    PubSubMessage    pubSubMessage = new PubSubMessage(PubSubMessage.TYPE_DELIVER, serializedMessage);

    if (pubSubManager.publish(address, pubSubMessage)) {
      onlineMeter.mark();
    } else {
      offlineMeter.mark();
      storedMessages.insert(account.getId(), device.getId(), serializedMessage);
      pubSubManager.publish(address, new PubSubMessage(PubSubMessage.TYPE_QUERY_DB, null));
    }
  }
}
