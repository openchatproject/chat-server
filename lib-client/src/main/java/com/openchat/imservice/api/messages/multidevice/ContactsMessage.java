package com.openchat.imservice.api.messages.multidevice;

import com.openchat.imservice.api.messages.OpenchatServiceAttachment;

public class ContactsMessage {

  private final OpenchatServiceAttachment contacts;
  private final boolean                 complete;

  public ContactsMessage(OpenchatServiceAttachment contacts, boolean complete) {
    this.contacts = contacts;
    this.complete = complete;
  }

  public OpenchatServiceAttachment getContactsStream() {
    return contacts;
  }

  public boolean isComplete() {
    return complete;
  }
}
