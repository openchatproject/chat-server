package com.openchat.secureim.limits;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Meter;
import net.spy.memcached.MemcachedClient;

import com.openchat.secureim.controllers.RateLimitExceededException;

import java.util.concurrent.TimeUnit;

public class RateLimiter {

  private final Meter           meter;
  private final MemcachedClient memcachedClient;
  private final String          name;
  private final int             bucketSize;
  private final double          leakRatePerMillis;

  public RateLimiter(MemcachedClient memcachedClient, String name,
                     int bucketSize, double leakRatePerMinute)
  {
    this.meter             = Metrics.newMeter(RateLimiter.class, name, "exceeded", TimeUnit.MINUTES);
    this.memcachedClient   = memcachedClient;
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
    memcachedClient.set(getBucketName(key),
                        (int)Math.ceil((bucketSize / leakRatePerMillis) / 1000), bucket);
  }

  private LeakyBucket getBucket(String key) {
    LeakyBucket bucket = (LeakyBucket)memcachedClient.get(getBucketName(key));

    if (bucket == null) {
      return new LeakyBucket(bucketSize, leakRatePerMillis);
    } else {
      return bucket;
    }
  }

  private String getBucketName(String key) {
    return LeakyBucket.class.getSimpleName() + name + key;
  }
}
