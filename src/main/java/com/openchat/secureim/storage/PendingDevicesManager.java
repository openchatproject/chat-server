package com.openchat.secureim.storage;

import com.google.common.base.Optional;
import net.spy.memcached.MemcachedClient;

public class PendingDevicesManager {

  private static final String MEMCACHE_PREFIX = "pending_devices";

  private final PendingDevices  pendingDevices;
  private final MemcachedClient memcachedClient;

  public PendingDevicesManager(PendingDevices pendingDevices,
                               MemcachedClient memcachedClient)
  {
    this.pendingDevices  = pendingDevices;
    this.memcachedClient = memcachedClient;
  }

  public void store(String number, String code) {
    if (memcachedClient != null) {
      memcachedClient.set(MEMCACHE_PREFIX + number, 0, code);
    }

    pendingDevices.insert(number, code);
  }

  public void remove(String number) {
    if (memcachedClient != null) {
      memcachedClient.delete(MEMCACHE_PREFIX + number);
    }

    pendingDevices.remove(number);
  }

  public Optional<String> getCodeForNumber(String number) {
    String code = null;

    if (memcachedClient != null) {
      code = (String)memcachedClient.get(MEMCACHE_PREFIX + number);
    }

    if (code == null) {
      code = pendingDevices.getCodeForNumber(number);

      if (code != null && memcachedClient != null) {
        memcachedClient.set(MEMCACHE_PREFIX + number, 0, code);
      }
    }

    if (code != null) return Optional.of(code);
    else              return Optional.absent();
  }
}
