package com.openchat.secureim.storage;


import com.google.common.base.Optional;
import net.spy.memcached.MemcachedClient;
import com.openchat.secureim.entities.ClientContact;
import com.openchat.secureim.util.Pair;
import com.openchat.secureim.util.Util;
import sun.util.logging.resources.logging_zh_CN;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

  public List<Device> getAllMasterDevices(int offset, int length) {
    return accounts.getAllMasterDevices(offset, length);
  }

  public Iterator<Device> getAllMasterDevices() {
    return accounts.getAllMasterDevices();
  }

  public void provisionDevice(Device device) {
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

  public Optional<Account> getAccount(String number) {
    List<Device> devices = accounts.getAllByNumber(number);
    if (devices.isEmpty())
      return Optional.absent();
    return Optional.of(new Account(number, devices.get(0).getSupportsSms(), devices));
  }

  private Map<String, Account> getAllAccounts(Set<String> numbers) {
    //TODO: ONE QUERY
    Map<String, Account> result = new HashMap<>();
    for (String number : numbers) {
      Optional<Account> account = getAccount(number);
      if (account.isPresent())
        result.put(number, account.get());
    }
    return result;
  }

  public Pair<Map<String, Account>, List<String>> getAccountsForDevices(Map<String, Set<Long>> destinations) {
    List<String> numbersMissingDevices = new LinkedList<>();
    Map<String, Account> localAccounts = getAllAccounts(destinations.keySet());

    for (String number : destinations.keySet()) {
      if (localAccounts.get(number) == null)
        numbersMissingDevices.add(number);
    }

    Iterator<Account> localAccountIterator = localAccounts.values().iterator();
    while (localAccountIterator.hasNext()) {
      Account account = localAccountIterator.next();
      if (!account.hasAllDeviceIds(destinations.get(account.getNumber()))) {
        numbersMissingDevices.add(account.getNumber());
        localAccountIterator.remove();
      }
    }

    return new Pair<>(localAccounts, numbersMissingDevices);
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
