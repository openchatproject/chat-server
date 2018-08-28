package com.openchat.secureim.storage;


import com.google.common.base.Optional;
import net.spy.memcached.MemcachedClient;
import com.openchat.secureim.entities.ClientContact;
import com.openchat.secureim.util.Util;

import java.util.Iterator;
import java.util.List;

public class AccountsManager {

  private final Accounts         accounts;
  private final MemcachedClient  memcachedClient;
  private final DirectoryManager directory;

  public AccountsManager(Accounts accounts,
                         DirectoryManager directory,
                         MemcachedClient memcachedClient)
  {
    this.accounts        = accounts;
    this.directory       = directory;
    this.memcachedClient = memcachedClient;
  }

  public long getCount() {
    return accounts.getNumberCount();
  }

  public List<Device> getAllMasterAccounts(int offset, int length) {
    return accounts.getAllFirstAccounts(offset, length);
  }

  public Iterator<Device> getAllMasterAccounts() {
    return accounts.getAllFirstAccounts();
  }

  public void createAccountOnExistingNumber(Device device) {
    long id = accounts.insert(device);
    device.setId(id);

    if (memcachedClient != null) {
      memcachedClient.set(getKey(device.getNumber(), device.getDeviceId()), 0, device);
    }

    updateDirectory(device);
  }

  public void update(Device device) {
    if (memcachedClient != null) {
      memcachedClient.set(getKey(device.getNumber(), device.getDeviceId()), 0, device);
    }

    accounts.update(device);
    updateDirectory(device);
  }

  public Optional<Device> get(String number, long deviceId) {
    Device device = null;

    if (memcachedClient != null) {
      device = (Device)memcachedClient.get(getKey(number, deviceId));
    }

    if (device == null) {
      device = accounts.get(number, deviceId);

      if (device != null && memcachedClient != null) {
        memcachedClient.set(getKey(number, deviceId), 0, device);
      }
    }

    if (device != null) return Optional.of(device);
    else                 return Optional.absent();
  }

  public List<Device> getAllByNumber(String number) {
    return accounts.getAllByNumber(number);
  }

  private void updateDirectory(Device device) {
    if (device.getDeviceId() != 1)
      return;

    if (device.isActive()) {
      byte[]        token         = Util.getContactToken(device.getNumber());
      ClientContact clientContact = new ClientContact(token, null, device.getSupportsSms());
      directory.add(clientContact);
    } else {
      directory.remove(device.getNumber());
    }
  }

  private String getKey(String number, long accountId) {
    return Device.class.getSimpleName() + Device.MEMCACHE_VERION + number + accountId;
  }
}
