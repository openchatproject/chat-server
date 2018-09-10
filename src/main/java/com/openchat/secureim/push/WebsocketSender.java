package com.openchat.secureim.push;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.storage.Account;
import com.openchat.secureim.storage.Device;
import com.openchat.secureim.storage.PubSubManager;
import com.openchat.secureim.storage.StoredMessages;
import com.openchat.secureim.util.Constants;
import com.openchat.secureim.websocket.ProvisioningAddress;
import com.openchat.secureim.websocket.WebsocketAddress;

import java.io.UnsupportedEncodingException;

import static com.codahale.metrics.MetricRegistry.name;
import static com.openchat.secureim.entities.MessageProtos.OutgoingMessageSignal;
import static com.openchat.secureim.storage.PubSubProtos.PubSubMessage;

public class WebsocketSender {

  private static final Logger logger = LoggerFactory.getLogger(WebsocketSender.class);

  private final MetricRegistry metricRegistry = SharedMetricRegistries.getOrCreate(Constants.METRICS_NAME);

  private final Meter websocketOnlineMeter  = metricRegistry.meter(name(getClass(), "ws_online"  ));
  private final Meter websocketOfflineMeter = metricRegistry.meter(name(getClass(), "ws_offline" ));

  private final Meter apnOnlineMeter        = metricRegistry.meter(name(getClass(), "apn_online" ));
  private final Meter apnOfflineMeter       = metricRegistry.meter(name(getClass(), "apn_offline"));

  private final Meter provisioningOnlineMeter  = metricRegistry.meter(name(getClass(), "provisioning_online" ));
  private final Meter provisioningOfflineMeter = metricRegistry.meter(name(getClass(), "provisioning_offline"));

  private final StoredMessages storedMessages;
  private final PubSubManager  pubSubManager;

  public WebsocketSender(StoredMessages storedMessages, PubSubManager pubSubManager) {
    this.storedMessages = storedMessages;
    this.pubSubManager  = pubSubManager;
  }

  public boolean sendMessage(Account account, Device device, OutgoingMessageSignal message, boolean apn) {
    WebsocketAddress address       = new WebsocketAddress(account.getNumber(), device.getId());
    PubSubMessage    pubSubMessage = PubSubMessage.newBuilder()
                                                  .setType(PubSubMessage.Type.DELIVER)
                                                  .setContent(message.toByteString())
                                                  .build();

    if (pubSubManager.publish(address, pubSubMessage)) {
      if (apn) apnOnlineMeter.mark();
      else     websocketOnlineMeter.mark();

      return true;
    } else {
      if (apn) apnOfflineMeter.mark();
      else     websocketOfflineMeter.mark();

      storedMessages.insert(address, message);
      pubSubManager.publish(address, PubSubMessage.newBuilder()
                                                  .setType(PubSubMessage.Type.QUERY_DB)
                                                  .build());

      return false;
    }
  }

  public boolean sendProvisioningMessage(ProvisioningAddress address, String body) {
    try {
      PubSubMessage    pubSubMessage = PubSubMessage.newBuilder()
                                                    .setType(PubSubMessage.Type.DELIVER)
                                                    .setContent(ByteString.copyFrom(body, "UTF-8"))
                                                    .build();

      if (pubSubManager.publish(address, pubSubMessage)) {
        provisioningOnlineMeter.mark();
        return true;
      } else {
        provisioningOfflineMeter.mark();
        return false;
      }
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError(e);
    }
  }
}
