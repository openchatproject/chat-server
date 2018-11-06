package com.openchat.imservice.api.messages;

import com.openchat.protocal.util.guava.Optional;

import java.util.LinkedList;
import java.util.List;

public class OpenchatServiceMessage {

  private final long                                 timestamp;
  private final Optional<List<OpenchatServiceAttachment>> attachments;
  private final Optional<String>                     body;
  private final Optional<OpenchatServiceGroup>            group;
  private final boolean                              secure;
  private final boolean                              endSession;

  
  public OpenchatServiceMessage(long timestamp, String body) {
    this(timestamp, (List<OpenchatServiceAttachment>)null, body);
  }

  public OpenchatServiceMessage(final long timestamp, final OpenchatServiceAttachment attachment, final String body) {
    this(timestamp, new LinkedList<OpenchatServiceAttachment>() {{add(attachment);}}, body);
  }

  
  public OpenchatServiceMessage(long timestamp, List<OpenchatServiceAttachment> attachments, String body) {
    this(timestamp, null, attachments, body);
  }

  
  public OpenchatServiceMessage(long timestamp, OpenchatServiceGroup group, List<OpenchatServiceAttachment> attachments, String body) {
    this(timestamp, group, attachments, body, true, false);
  }

  
  public OpenchatServiceMessage(long timestamp, OpenchatServiceGroup group, List<OpenchatServiceAttachment> attachments, String body, boolean secure, boolean endSession) {
    this.timestamp   = timestamp;
    this.body        = Optional.fromNullable(body);
    this.group       = Optional.fromNullable(group);
    this.secure      = secure;
    this.endSession  = endSession;

    if (attachments != null && !attachments.isEmpty()) {
      this.attachments = Optional.of(attachments);
    } else {
      this.attachments = Optional.absent();
    }
  }

  
  public long getTimestamp() {
    return timestamp;
  }

  
  public Optional<List<OpenchatServiceAttachment>> getAttachments() {
    return attachments;
  }

  
  public Optional<String> getBody() {
    return body;
  }

  
  public Optional<OpenchatServiceGroup> getGroupInfo() {
    return group;
  }

  public boolean isSecure() {
    return secure;
  }

  public boolean isEndSession() {
    return endSession;
  }

  public boolean isGroupUpdate() {
    return group.isPresent() && group.get().getType() != OpenchatServiceGroup.Type.DELIVER;
  }
}
