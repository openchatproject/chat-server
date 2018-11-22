package com.openchat.imservice.api.messages;

import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.push.OpenchatServiceAddress;

import java.util.LinkedList;
import java.util.List;

public class OpenchatServiceDataMessage {

  private final long                                    timestamp;
  private final Optional<List<OpenchatServiceAttachment>> attachments;
  private final Optional<String>                        body;
  private final Optional<OpenchatServiceGroup>            group;
  private final Optional<byte[]>                        profileKey;
  private final boolean                                 endSession;
  private final boolean                                 expirationUpdate;
  private final int                                     expiresInSeconds;
  private final boolean                                 profileKeyUpdate;
  private final Optional<Quote>                         quote;

  
  public OpenchatServiceDataMessage(long timestamp, String body) {
    this(timestamp, body, 0);
  }

  
  public OpenchatServiceDataMessage(long timestamp, String body, int expiresInSeconds) {
    this(timestamp, (List<OpenchatServiceAttachment>)null, body, expiresInSeconds);
  }

  public OpenchatServiceDataMessage(final long timestamp, final OpenchatServiceAttachment attachment, final String body) {
    this(timestamp, new LinkedList<OpenchatServiceAttachment>() {{add(attachment);}}, body);
  }

  
  public OpenchatServiceDataMessage(long timestamp, List<OpenchatServiceAttachment> attachments, String body) {
    this(timestamp, attachments, body, 0);
  }

  
  public OpenchatServiceDataMessage(long timestamp, List<OpenchatServiceAttachment> attachments, String body, int expiresInSeconds) {
    this(timestamp, null, attachments, body, expiresInSeconds);
  }

  
  public OpenchatServiceDataMessage(long timestamp, OpenchatServiceGroup group, List<OpenchatServiceAttachment> attachments, String body) {
    this(timestamp, group, attachments, body, 0);
  }

  
  public OpenchatServiceDataMessage(long timestamp, OpenchatServiceGroup group, List<OpenchatServiceAttachment> attachments, String body, int expiresInSeconds) {
    this(timestamp, group, attachments, body, false, expiresInSeconds, false, null, false, null);
  }

  
  public OpenchatServiceDataMessage(long timestamp, OpenchatServiceGroup group,
                                  List<OpenchatServiceAttachment> attachments,
                                  String body, boolean endSession, int expiresInSeconds,
                                  boolean expirationUpdate, byte[] profileKey, boolean profileKeyUpdate,
                                  Quote quote)
  {
    this.timestamp        = timestamp;
    this.body             = Optional.fromNullable(body);
    this.group            = Optional.fromNullable(group);
    this.endSession       = endSession;
    this.expiresInSeconds = expiresInSeconds;
    this.expirationUpdate = expirationUpdate;
    this.profileKey       = Optional.fromNullable(profileKey);
    this.profileKeyUpdate = profileKeyUpdate;
    this.quote            = Optional.fromNullable(quote);

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

  public boolean isEndSession() {
    return endSession;
  }

  public boolean isExpirationUpdate() {
    return expirationUpdate;
  }

  public boolean isProfileKeyUpdate() {
    return profileKeyUpdate;
  }

  public boolean isGroupUpdate() {
    return group.isPresent() && group.get().getType() != OpenchatServiceGroup.Type.DELIVER;
  }

  public int getExpiresInSeconds() {
    return expiresInSeconds;
  }

  public Optional<byte[]> getProfileKey() {
    return profileKey;
  }

  public Optional<Quote> getQuote() {
    return quote;
  }

  public static class Builder {

    private List<OpenchatServiceAttachment> attachments = new LinkedList<>();
    private long               timestamp;
    private OpenchatServiceGroup group;
    private String             body;
    private boolean            endSession;
    private int                expiresInSeconds;
    private boolean            expirationUpdate;
    private byte[]             profileKey;
    private boolean            profileKeyUpdate;
    private Quote              quote;

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
      return asEndSessionMessage(true);
    }

    public Builder asEndSessionMessage(boolean endSession) {
      this.endSession = endSession;
      return this;
    }

    public Builder asExpirationUpdate() {
      return asExpirationUpdate(true);
    }

    public Builder asExpirationUpdate(boolean expirationUpdate) {
      this.expirationUpdate = expirationUpdate;
      return this;
    }

    public Builder withExpiration(int expiresInSeconds) {
      this.expiresInSeconds = expiresInSeconds;
      return this;
    }

    public Builder withProfileKey(byte[] profileKey) {
      this.profileKey = profileKey;
      return this;
    }

    public Builder asProfileKeyUpdate(boolean profileKeyUpdate) {
      this.profileKeyUpdate = profileKeyUpdate;
      return this;
    }

    public Builder withQuote(Quote quote) {
      this.quote = quote;
      return this;
    }

    public OpenchatServiceDataMessage build() {
      if (timestamp == 0) timestamp = System.currentTimeMillis();
      return new OpenchatServiceDataMessage(timestamp, group, attachments, body, endSession,
                                          expiresInSeconds, expirationUpdate, profileKey,
                                          profileKeyUpdate, quote);
    }
  }

  public static class Quote {
    private final long                          id;
    private final OpenchatServiceAddress          author;
    private final String                        text;
    private final List<OpenchatServiceAttachment> attachments;

    public Quote(long id, OpenchatServiceAddress author, String text, List<OpenchatServiceAttachment> attachments) {
      this.id          = id;
      this.author      = author;
      this.text        = text;
      this.attachments = attachments;
    }

    public long getId() {
      return id;
    }

    public OpenchatServiceAddress getAuthor() {
      return author;
    }

    public String getText() {
      return text;
    }

    public List<OpenchatServiceAttachment> getAttachments() {
      return attachments;
    }

  }
}
