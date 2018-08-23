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
    if (memcachedClient != null) {
      memcachedClient.set(MEMCACHE_PREFIX + number, 0, code);
    }

    pendingAccounts.insert(number, code);
  }

  public Optional<String> getCodeForNumber(String number) {
    String code = null;

    if (memcachedClient != null) {
      code = (String)memcachedClient.get(MEMCACHE_PREFIX + number);
    }

    if (code == null) {
      code = pendingAccounts.getCodeForNumber(number);

      if (code != null && memcachedClient != null) {
        memcachedClient.set(MEMCACHE_PREFIX + number, 0, code);
      }
    }

    if (code != null) return Optional.of(code);
    else              return Optional.absent();
  }
}
