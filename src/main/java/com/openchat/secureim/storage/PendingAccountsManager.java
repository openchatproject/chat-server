package com.openchat.secureim.storage;

import com.google.common.base.Optional;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class PendingAccountsManager {

  private static final String CACHE_PREFIX = "pending_account::";

  private final PendingAccounts pendingAccounts;
  private final JedisPool       cacheClient;

  public PendingAccountsManager(PendingAccounts pendingAccounts, JedisPool cacheClient)
  {
    this.pendingAccounts = pendingAccounts;
    this.cacheClient     = cacheClient;
  }

  public void store(String number, String code) {
    memcacheSet(number, code);
    pendingAccounts.insert(number, code);
  }

  public void remove(String number) {
    memcacheDelete(number);
    pendingAccounts.remove(number);
  }

  public Optional<String> getCodeForNumber(String number) {
    Optional<String> code = memcacheGet(number);

    if (!code.isPresent()) {
      code = Optional.fromNullable(pendingAccounts.getCodeForNumber(number));

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
