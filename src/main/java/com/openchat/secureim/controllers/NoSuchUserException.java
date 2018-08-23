package com.openchat.secureim.controllers;

import com.openchat.secureim.federation.NoSuchPeerException;

import java.util.LinkedList;
import java.util.List;

public class NoSuchUserException extends Exception {

  private List<String> missing;

  public NoSuchUserException(String user) {
    super(user);
    missing = new LinkedList<String>();
    missing.add(user);
  }

  public NoSuchUserException(List<String> missing) {
    this.missing = missing;
  }

  public NoSuchUserException(Exception e) {
    super(e);
  }

  public List<String> getMissing() {
    return missing;
  }
}
