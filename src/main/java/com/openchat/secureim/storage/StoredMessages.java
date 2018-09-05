package com.openchat.secureim.storage;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.entities.PendingMessage;
import com.openchat.secureim.util.Constants;
import com.openchat.secureim.util.SystemMapper;
import com.openchat.secureim.websocket.WebsocketAddress;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static com.codahale.metrics.MetricRegistry.name;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class StoredMessages {

  private static final Logger logger = LoggerFactory.getLogger(StoredMessages.class);

  private final MetricRegistry metricRegistry     = SharedMetricRegistries.getOrCreate(Constants.METRICS_NAME);
  private final Histogram      queueSizeHistogram = metricRegistry.histogram(name(getClass(), "queue_size"));


  private static final ObjectMapper mapper = SystemMapper.getMapper();
  private static final String QUEUE_PREFIX = "msgs";

  private final JedisPool jedisPool;

  public StoredMessages(JedisPool jedisPool) {
    this.jedisPool = jedisPool;
  }

  public void clear(WebsocketAddress address) {
    Jedis jedis = null;

    try {
      jedis = jedisPool.getResource();
      jedis.del(getKey(address));
    } finally {
      if (jedis != null)
        jedisPool.returnResource(jedis);
    }
  }

  public void insert(WebsocketAddress address, PendingMessage message) {
    Jedis jedis = null;

    try {
      jedis = jedisPool.getResource();

      String serializedMessage = mapper.writeValueAsString(message);
      long   queueSize         = jedis.lpush(getKey(address), serializedMessage);
      queueSizeHistogram.update(queueSize);

      if (queueSize > 1000) {
        jedis.ltrim(getKey(address), 0, 999);
      }

    } catch (JsonProcessingException e) {
      logger.warn("StoredMessages", "Unable to store correctly", e);
    } finally {
      if (jedis != null)
        jedisPool.returnResource(jedis);
    }
  }

  public List<PendingMessage> getMessagesForDevice(WebsocketAddress address) {
    List<PendingMessage> messages = new LinkedList<>();
    Jedis                jedis    = null;

    try {
      jedis = jedisPool.getResource();
      String message;

      while ((message = jedis.rpop(getKey(address))) != null) {
        try {
          messages.add(mapper.readValue(message, PendingMessage.class));
        } catch (IOException e) {
          logger.warn("StoredMessages", "Not a valid PendingMessage", e);
        }
      }

      return messages;
    } finally {
      if (jedis != null)
        jedisPool.returnResource(jedis);
    }
  }

  private String getKey(WebsocketAddress address) {
    return QUEUE_PREFIX + ":" + address.serialize();
  }

}
