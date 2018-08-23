package com.openchat.secureim.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;

public class ClientContacts {

  @JsonProperty
  private List<ClientContact> contacts;

  public ClientContacts(List<ClientContact> contacts) {
    if (contacts != null) this.contacts = contacts;
    else                  this.contacts = new LinkedList<>();
  }

  public ClientContacts() {
    this.contacts = new LinkedList<>();
  }

  public List<ClientContact> getContacts() {
    return contacts;
  }
}
