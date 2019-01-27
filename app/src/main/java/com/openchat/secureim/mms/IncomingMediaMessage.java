package com.openchat.secureim.mms;

import com.openchat.secureim.attachments.Attachment;
import com.openchat.secureim.attachments.PointerAttachment;
import com.openchat.secureim.crypto.MasterSecretUnion;
import com.openchat.secureim.database.Address;
import com.openchat.secureim.util.GroupUtil;
import com.openchat.libim.util.guava.Optional;
import com.openchat.imservice.api.messages.openchatServiceAttachment;
import com.openchat.imservice.api.messages.openchatServiceGroup;

import java.util.LinkedList;
import java.util.List;

public class IncomingMediaMessage {

  private final Address from;
  private final Address groupId;
  private final String  body;
  private final boolean push;
  private final long    sentTimeMillis;
  private final int     subscriptionId;
  private final long    expiresIn;
  private final boolean expirationUpdate;

  private final List<Attachment> attachments = new LinkedList<>();

  public IncomingMediaMessage(Address from,
                              Optional<Address> groupId,
                              String body,
                              long sentTimeMillis,
                              List<Attachment> attachments,
                              int subscriptionId,
                              long expiresIn,
                              boolean expirationUpdate)
  {
    this.from             = from;
    this.groupId          = groupId.orNull();
    this.sentTimeMillis   = sentTimeMillis;
    this.body             = body;
    this.push             = false;
    this.subscriptionId   = subscriptionId;
    this.expiresIn        = expiresIn;
    this.expirationUpdate = expirationUpdate;

    this.attachments.addAll(attachments);
  }

  public IncomingMediaMessage(MasterSecretUnion masterSecret,
                              Address from,
                              long sentTimeMillis,
                              int subscriptionId,
                              long expiresIn,
                              boolean expirationUpdate,
                              Optional<String> relay,
                              Optional<String> body,
                              Optional<openchatServiceGroup> group,
                              Optional<List<openchatServiceAttachment>> attachments)
  {
    this.push             = true;
    this.from             = from;
    this.sentTimeMillis   = sentTimeMillis;
    this.body             = body.orNull();
    this.subscriptionId   = subscriptionId;
    this.expiresIn        = expiresIn;
    this.expirationUpdate = expirationUpdate;

    if (group.isPresent()) this.groupId = Address.fromSerialized(GroupUtil.getEncodedId(group.get().getGroupId(), false));
    else                   this.groupId = null;

    this.attachments.addAll(PointerAttachment.forPointers(masterSecret, attachments));
  }

  public int getSubscriptionId() {
    return subscriptionId;
  }

  public String getBody() {
    return body;
  }

  public List<Attachment> getAttachments() {
    return attachments;
  }

  public Address getFrom() {
    return from;
  }

  public Address getGroupId() {
    return groupId;
  }

  public boolean isPushMessage() {
    return push;
  }

  public boolean isExpirationUpdate() {
    return expirationUpdate;
  }

  public long getSentTimeMillis() {
    return sentTimeMillis;
  }

  public long getExpiresIn() {
    return expiresIn;
  }

  public boolean isGroupMessage() {
    return groupId != null;
  }
}
