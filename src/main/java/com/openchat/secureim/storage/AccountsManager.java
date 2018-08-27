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

  public List<Account> getAllMasterAccounts(int offset, int length) {
    return accounts.getAllFirstAccounts(offset, length);
  }

  public Iterator<Account> getAllMasterAccounts() {
    return accounts.getAllFirstAccounts();
  }

  public void createAccountOnExistingNumber(Account account) {
    long id = accounts.insert(account);
    account.setId(id);

    if (memcachedClient != null) {
      memcachedClient.set(getKey(account.getNumber(), account.getDeviceId()), 0, account);
    }

    updateDirectory(account);
  }

  public void update(Account account) {
    if (memcachedClient != null) {
      memcachedClient.set(getKey(account.getNumber(), account.getDeviceId()), 0, account);
    }

    accounts.update(account);
    updateDirectory(account);
  }

  public Optional<Account> get(String number, long deviceId) {
    Account account = null;

    if (memcachedClient != null) {
      account = (Account)memcachedClient.get(getKey(number, deviceId));
    }

    if (account == null) {
      account = accounts.get(number, deviceId);

      if (account != null && memcachedClient != null) {
        memcachedClient.set(getKey(number, deviceId), 0, account);
      }
    }

    if (account != null) return Optional.of(account);
    else                 return Optional.absent();
  }

  public List<Account> getAllByNumber(String number) {
    return accounts.getAllByNumber(number);
  }

  private void updateDirectory(Account account) {
    if (account.getDeviceId() != 1)
      return;

    if (account.isActive()) {
      byte[]        token         = Util.getContactToken(account.getNumber());
      ClientContact clientContact = new ClientContact(token, null, account.getSupportsSms());
      directory.add(clientContact);
    } else {
      directory.remove(account.getNumber());
    }
  }

  private String getKey(String number, long accountId) {
    return Account.class.getSimpleName() + Account.MEMCACHE_VERION + number + accountId;
  }
}
