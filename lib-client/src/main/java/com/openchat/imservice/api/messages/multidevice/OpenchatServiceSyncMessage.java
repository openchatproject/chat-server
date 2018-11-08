package com.openchat.imservice.api.messages.multidevice;

import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.messages.OpenchatServiceAttachment;
import com.openchat.imservice.api.messages.OpenchatServiceGroup;

public class OpenchatServiceSyncMessage {

  private final Optional<SentTranscriptMessage> sent;
  private final Optional<OpenchatServiceAttachment>  contacts;
  private final Optional<OpenchatServiceGroup>       group;

  public OpenchatServiceSyncMessage() {
    this.sent     = Optional.absent();
    this.contacts = Optional.absent();
    this.group    = Optional.absent();
  }

  public OpenchatServiceSyncMessage(SentTranscriptMessage sent) {
    this.sent     = Optional.of(sent);
    this.contacts = Optional.absent();
    this.group    = Optional.absent();
  }

  public OpenchatServiceSyncMessage(OpenchatServiceAttachment contacts) {
    this.contacts = Optional.of(contacts);
    this.sent     = Optional.absent();
    this.group    = Optional.absent();
  }

  public OpenchatServiceSyncMessage(OpenchatServiceGroup group) {
    this.group    = Optional.of(group);
    this.sent     = Optional.absent();
    this.contacts = Optional.absent();
  }

  public Optional<SentTranscriptMessage> getSent() {
    return sent;
  }

  public Optional<OpenchatServiceGroup> getGroup() {
    return group;
  }

  public Optional<OpenchatServiceAttachment> getContacts() {
    return contacts;
  }

}
