package com.openchat.imservice.api.messages;

import com.openchat.protocal.util.guava.Optional;

import java.util.LinkedList;
import java.util.List;

public class OpenchatServiceMessage {

  private final long                                 timestamp;
  private final Optional<List<OpenchatServiceAttachment>> attachments;
  private final Optional<String>                     body;
  private final Optional<OpenchatServiceGroup>            group;
  private final Optional<OpenchatServiceSyncContext>      syncContext;
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
    this(timestamp, group, attachments, body, null, true, false);
  }

  
  public OpenchatServiceMessage(long timestamp, OpenchatServiceGroup group, List<OpenchatServiceAttachment> attachments, String body, OpenchatServiceSyncContext syncContext, boolean secure, boolean endSession) {
    this.timestamp   = timestamp;
    this.body        = Optional.fromNullable(body);
    this.group       = Optional.fromNullable(group);
    this.syncContext = Optional.fromNullable(syncContext);
    this.secure      = secure;
    this.endSession  = endSession;

    if (attachments != null && !attachments.isEmpty()) {
      this.attachments = Optional.of(attachments);
    } else {
      this.attachments = Optional.absent();
    }
  }

  public static Builder newBuilder() {
    return new Builder();
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

  public Optional<OpenchatServiceSyncContext> getSyncContext() {
    return syncContext;
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

  public static class Builder {

    private List<OpenchatServiceAttachment> attachments = new LinkedList<>();
    private long                       timestamp;
    private OpenchatServiceGroup            group;
    private String                     body;
    private boolean                    endSession;

    private Builder() {}

    public Builder withTimestamp(long timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public Builder asGroupMessage(OpenchatServiceGroup group) {
      this.group = group;
      return this;
    }

    public Builder withAttachment(OpenchatServiceAttachment attachment) {
      this.attachments.add(attachment);
      return this;
    }

    public Builder withAttachments(List<OpenchatServiceAttachment> attachments) {
      this.attachments.addAll(attachments);
      return this;
    }

    public Builder withBody(String body) {
      this.body = body;
      return this;
    }

    public Builder asEndSessionMessage() {
      this.endSession = true;
      return this;
    }

    public Builder asEndSessionMessage(boolean endSession) {
      this.endSession = endSession;
      return this;
    }

    public OpenchatServiceMessage build() {
      if (timestamp == 0) timestamp = System.currentTimeMillis();
      return new OpenchatServiceMessage(timestamp, group, attachments, body, null, true, endSession);
    }
  }
}
