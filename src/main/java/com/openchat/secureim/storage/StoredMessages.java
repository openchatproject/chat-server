package com.openchat.secureim.storage;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;

import com.openchat.secureim.util.Constants;

import java.util.LinkedList;
import java.util.List;

import static com.codahale.metrics.MetricRegistry.name;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class StoredMessages {

  private final MetricRegistry metricRegistry     = SharedMetricRegistries.getOrCreate(Constants.METRICS_NAME);
  private final Histogram      queueSizeHistogram = metricRegistry.histogram(name(getClass(), "queue_size"));

  private static final String QUEUE_PREFIX = "msgs";

  private final JedisPool jedisPool;

  public StoredMessages(JedisPool jedisPool) {
    this.jedisPool = jedisPool;
  }

  public void insert(long accountId, long deviceId, String message) {
    Jedis jedis = null;

    try {
      jedis = jedisPool.getResource();

      long queueSize = jedis.lpush(getKey(accountId, deviceId), message);
      queueSizeHistogram.update(queueSize);

      if (queueSize > 1000) {
        jedis.ltrim(getKey(accountId, deviceId), 0, 999);
      }
    } finally {
      if (jedis != null)
        jedisPool.returnResource(jedis);
    }
  }

  public List<String> getMessagesForDevice(long accountId, long deviceId) {
    List<String> messages = new LinkedList<>();
    Jedis        jedis    = null;

    try {
      jedis = jedisPool.getResource();
      String message;

      while ((message = jedis.rpop(QUEUE_PREFIX + accountId + ":" + deviceId)) != null) {
        messages.add(message);
      }

      return messages;
    } finally {
      if (jedis != null)
        jedisPool.returnResource(jedis);
    }
  }

  private String getKey(long accountId, long deviceId) {
    return QUEUE_PREFIX + ":" + accountId + ":" + deviceId;
  }

}
