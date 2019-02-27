package com.openchat.secureim.transport;

import com.openchat.imservice.push.UnregisteredUserException;

import java.util.List;

public class EncapsulatedExceptions extends Throwable {

  private final List<UntrustedIdentityException> untrustedIdentityExceptions;
  private final List<UnregisteredUserException>  unregisteredUserExceptions;

  public EncapsulatedExceptions(List<UntrustedIdentityException> untrustedIdentities,
                                List<UnregisteredUserException> unregisteredUsers)
  {
    this.untrustedIdentityExceptions = untrustedIdentities;
    this.unregisteredUserExceptions  = unregisteredUsers;
  }

  public List<UntrustedIdentityException> getUntrustedIdentityExceptions() {
    return untrustedIdentityExceptions;
  }

  public List<UnregisteredUserException> getUnregisteredUserExceptions() {
    return unregisteredUserExceptions;
  }
}
