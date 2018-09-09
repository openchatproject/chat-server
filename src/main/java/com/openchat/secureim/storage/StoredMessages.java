package com.openchat.secureim.storage;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.util.Constants;
import com.openchat.secureim.websocket.WebsocketAddress;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.codahale.metrics.MetricRegistry.name;
import static com.openchat.secureim.entities.MessageProtos.OutgoingMessageSignal;
import static com.openchat.secureim.storage.StoredMessageProtos.StoredMessage;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class StoredMessages {

  private static final Logger logger = LoggerFactory.getLogger(StoredMessages.class);

  private final MetricRegistry metricRegistry     = SharedMetricRegistries.getOrCreate(Constants.METRICS_NAME);
  private final Histogram      queueSizeHistogram = metricRegistry.histogram(name(getClass(), "queue_size"));

  private static final String QUEUE_PREFIX = "msgs";

  private final JedisPool jedisPool;

  public StoredMessages(JedisPool jedisPool) {
    this.jedisPool = jedisPool;
  }

  public void clear(WebsocketAddress address) {
    try (Jedis jedis = jedisPool.getResource()) {
      jedis.del(getKey(address));
    }
  }

  public void insert(WebsocketAddress address, OutgoingMessageSignal message) {
    try (Jedis jedis = jedisPool.getResource()) {
      byte[]        queue         = getKey(address);
      StoredMessage storedMessage = StoredMessage.newBuilder()
                                                 .setType(StoredMessage.Type.MESSAGE)
                                                 .setContent(message.toByteString())
                                                 .build();

      long queueSize = jedis.lpush(queue, storedMessage.toByteArray());
      queueSizeHistogram.update(queueSize);

      jedis.expireAt(queue, (System.currentTimeMillis() / 1000) + TimeUnit.DAYS.toSeconds(30));

      if (queueSize > 1000) {
        jedis.ltrim(getKey(address), 0, 999);
      }
    }
  }

  public List<OutgoingMessageSignal> getMessagesForDevice(WebsocketAddress address) {
    List<OutgoingMessageSignal> messages = new LinkedList<>();

    try (Jedis jedis = jedisPool.getResource()) {
      byte[] message;

      while ((message = jedis.rpop(getKey(address))) != null) {
        try {
          StoredMessage storedMessage = StoredMessage.parseFrom(message);

          if (storedMessage.getType().getNumber() == StoredMessage.Type.MESSAGE_VALUE) {
            messages.add(OutgoingMessageSignal.parseFrom(storedMessage.getContent()));
          } else {
            logger.warn("Unkown stored message type: " + storedMessage.getType().getNumber());
          }

        } catch (InvalidProtocolBufferException e) {
          logger.warn("Error parsing protobuf", e);
        }
      }

      return messages;
    }
  }

  private byte[] getKey(WebsocketAddress address) {
    return (QUEUE_PREFIX + ":" + address.serialize()).getBytes();
  }

}
