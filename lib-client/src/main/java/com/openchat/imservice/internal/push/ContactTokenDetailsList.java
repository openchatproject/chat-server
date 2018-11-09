package com.openchat.imservice.internal.push;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.openchat.imservice.api.push.ContactTokenDetails;

import java.util.List;

public class ContactTokenDetailsList {

  @JsonProperty
  private List<ContactTokenDetails> contacts;

  public ContactTokenDetailsList() {}

  public List<ContactTokenDetails> getContacts() {
    return contacts;
  }
}
