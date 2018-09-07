package com.openchat.secureim.push;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.entities.PendingMessage;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.Device;
import com.openchat.secureim.storage.PubSubManager;
import com.openchat.secureim.storage.PubSubMessage;
import com.openchat.secureim.storage.StoredMessages;
import com.openchat.secureim.util.Constants;
import com.openchat.secureim.util.SystemMapper;
import com.openchat.secureim.websocket.WebsocketAddress;

import static com.codahale.metrics.MetricRegistry.name;

public class WebsocketSender {

  private static final Logger logger = LoggerFactory.getLogger(WebsocketSender.class);

  private final MetricRegistry metricRegistry = SharedMetricRegistries.getOrCreate(Constants.METRICS_NAME);

  private final Meter websocketOnlineMeter  = metricRegistry.meter(name(getClass(), "ws_online"  ));
  private final Meter websocketOfflineMeter = metricRegistry.meter(name(getClass(), "ws_offline" ));

  private final Meter apnOnlineMeter        = metricRegistry.meter(name(getClass(), "apn_online" ));
  private final Meter apnOfflineMeter       = metricRegistry.meter(name(getClass(), "apn_offline"));

  private static final ObjectMapper mapper = SystemMapper.getMapper();

  private final StoredMessages storedMessages;
  private final PubSubManager  pubSubManager;

  public WebsocketSender(StoredMessages storedMessages, PubSubManager pubSubManager) {
    this.storedMessages = storedMessages;
    this.pubSubManager  = pubSubManager;
  }

  public boolean sendMessage(Account account, Device device, PendingMessage pendingMessage, boolean apn) {
    try {
      String           serialized    = mapper.writeValueAsString(pendingMessage);
      WebsocketAddress address       = new WebsocketAddress(account.getNumber(), device.getId());
      PubSubMessage    pubSubMessage = new PubSubMessage(PubSubMessage.TYPE_DELIVER, serialized);

      if (pubSubManager.publish(address, pubSubMessage)) {
        if (apn) apnOnlineMeter.mark();
        else     websocketOnlineMeter.mark();

        return true;
      } else {
        if (apn) apnOfflineMeter.mark();
        else     websocketOfflineMeter.mark();

        storedMessages.insert(address, pendingMessage);
        pubSubManager.publish(address, new PubSubMessage(PubSubMessage.TYPE_QUERY_DB, null));
        return false;
      }
    } catch (JsonProcessingException e) {
      logger.warn("WebsocketSender", "Unable to serialize json", e);
      return false;
    }
  }
}
