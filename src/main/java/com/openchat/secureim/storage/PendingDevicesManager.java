package com.openchat.secureim.storage;

import com.google.common.base.Optional;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class PendingDevicesManager {

  private static final String CACHE_PREFIX = "pending_devices::";

  private final PendingDevices  pendingDevices;
  private final JedisPool       cacheClient;

  public PendingDevicesManager(PendingDevices pendingDevices,
                               JedisPool      cacheClient)
  {
    this.pendingDevices = pendingDevices;
    this.cacheClient    = cacheClient;
  }

  public void store(String number, String code) {
    memcacheSet(number, code);
    pendingDevices.insert(number, code);
  }

  public void remove(String number) {
    memcacheDelete(number);
    pendingDevices.remove(number);
  }

  public Optional<String> getCodeForNumber(String number) {
    Optional<String> code = memcacheGet(number);

    if (!code.isPresent()) {
      code = Optional.fromNullable(pendingDevices.getCodeForNumber(number));

      if (code.isPresent()) {
        memcacheSet(number, code.get());
      }
    }

    return code;
  }

  private void memcacheSet(String number, String code) {
    try (Jedis jedis = cacheClient.getResource()) {
      jedis.set(CACHE_PREFIX + number, code);
    }
  }

  private Optional<String> memcacheGet(String number) {
    try (Jedis jedis = cacheClient.getResource()) {
      return Optional.fromNullable(jedis.get(CACHE_PREFIX + number));
    }
  }

  private void memcacheDelete(String number) {
    try (Jedis jedis = cacheClient.getResource()) {
      jedis.del(CACHE_PREFIX + number);
    }
  }

}
