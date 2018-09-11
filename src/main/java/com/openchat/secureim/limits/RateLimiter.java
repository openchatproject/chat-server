package com.openchat.secureim.limits;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.controllers.RateLimitExceededException;
import com.openchat.secureim.util.Constants;
import com.openchat.secureim.util.SystemMapper;

import java.io.IOException;

import static com.codahale.metrics.MetricRegistry.name;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RateLimiter {

  private final Logger       logger = LoggerFactory.getLogger(RateLimiter.class);
  private final ObjectMapper mapper = SystemMapper.getMapper();

  private final Meter     meter;
  private final JedisPool cacheClient;
  private final String    name;
  private final int       bucketSize;
  private final double    leakRatePerMillis;

  public RateLimiter(JedisPool cacheClient, String name,
                     int bucketSize, double leakRatePerMinute)
  {
    MetricRegistry metricRegistry = SharedMetricRegistries.getOrCreate(Constants.METRICS_NAME);

    this.meter             = metricRegistry.meter(name(getClass(), name, "exceeded"));
    this.cacheClient       = cacheClient;
    this.name              = name;
    this.bucketSize        = bucketSize;
    this.leakRatePerMillis = leakRatePerMinute / (60.0 * 1000.0);
  }

  public void validate(String key, int amount) throws RateLimitExceededException {
    LeakyBucket bucket = getBucket(key);

    if (bucket.add(amount)) {
      setBucket(key, bucket);
    } else {
      meter.mark();
      throw new RateLimitExceededException(key + " , " + amount);
    }
  }

  public void validate(String key) throws RateLimitExceededException {
    validate(key, 1);
  }

  private void setBucket(String key, LeakyBucket bucket) {
    try (Jedis jedis = cacheClient.getResource()) {
      String serialized = bucket.serialize(mapper);
      jedis.setex(getBucketName(key), (int) Math.ceil((bucketSize / leakRatePerMillis) / 1000), serialized);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e);
    }
  }

  private LeakyBucket getBucket(String key) {
    try (Jedis jedis = cacheClient.getResource()) {
      String serialized = jedis.get(getBucketName(key));

      if (serialized != null) {
        return LeakyBucket.fromSerialized(mapper, serialized);
      }
    } catch (IOException e) {
      logger.warn("Deserialization error", e);
    }

    return new LeakyBucket(bucketSize, leakRatePerMillis);
  }

  private String getBucketName(String key) {
    return "leaky_bucket::" + name + "::" + key;
  }
}
