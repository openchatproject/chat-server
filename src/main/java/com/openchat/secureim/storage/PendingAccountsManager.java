package com.openchat.secureim.storage;

import com.google.common.base.Optional;
import net.spy.memcached.MemcachedClient;

public class PendingAccountsManager {

  private static final String MEMCACHE_PREFIX = "pending_account";

  private final PendingAccounts pendingAccounts;
  private final MemcachedClient memcachedClient;

  public PendingAccountsManager(PendingAccounts pendingAccounts,
                                MemcachedClient memcachedClient)
  {
    this.pendingAccounts = pendingAccounts;
    this.memcachedClient = memcachedClient;
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
    if (memcachedClient != null) {
      memcachedClient.set(MEMCACHE_PREFIX + number, 0, code);
    }
  }

  private Optional<String> memcacheGet(String number) {
    if (memcachedClient != null) {
      return Optional.fromNullable((String)memcachedClient.get(MEMCACHE_PREFIX + number));
    } else {
      return Optional.absent();
    }
  }

  private void memcacheDelete(String number) {
    if (memcachedClient != null) {
      memcachedClient.delete(MEMCACHE_PREFIX + number);
    }
  }
}
