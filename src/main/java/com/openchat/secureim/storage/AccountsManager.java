package com.openchat.secureim.storage;


import com.google.common.base.Optional;
import net.spy.memcached.MemcachedClient;
import com.openchat.secureim.controllers.MissingDevicesException;
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
    return accounts.getCount();
  }

  public List<Account> getAll(int offset, int length) {
    return accounts.getAll(offset, length);
  }

  public Iterator<Account> getAll() {
    return accounts.getAll();
  }

  public void create(Account account) {
    accounts.create(account);

    if (memcachedClient != null) {
      memcachedClient.set(getKey(account.getNumber()), 0, account);
    }

    updateDirectory(account);
  }

  public void update(Account account) {
    if (memcachedClient != null) {
      memcachedClient.set(getKey(account.getNumber()), 0, account);
    }

    accounts.update(account);
    updateDirectory(account);
  }

  public Optional<Account> get(String number) {
    Account account = null;

    if (memcachedClient != null) {
      account = (Account)memcachedClient.get(getKey(number));
    }

    if (account == null) {
      account = accounts.get(number);

      if (account != null && memcachedClient != null) {
        memcachedClient.set(getKey(number), 0, account);
      }
    }

    if (account != null) return Optional.of(account);
    else                 return Optional.absent();
  }

  private void updateDirectory(Account account) {
    if (account.isActive()) {
      byte[]        token         = Util.getContactToken(account.getNumber());
      ClientContact clientContact = new ClientContact(token, null, account.getSupportsSms());
      directory.add(clientContact);
    } else {
      directory.remove(account.getNumber());
    }
  }

  private String getKey(String number) {
    return Account.class.getSimpleName() + Account.MEMCACHE_VERION + number;
  }

}
