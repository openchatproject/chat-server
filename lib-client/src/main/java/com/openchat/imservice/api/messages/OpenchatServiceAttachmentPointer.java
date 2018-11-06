package com.openchat.imservice.api.messages;

import com.openchat.protocal.util.guava.Optional;

public class OpenchatServiceAttachmentPointer extends OpenchatServiceAttachment {

  private final long             id;
  private final byte[]           key;
  private final Optional<String> relay;

  public OpenchatServiceAttachmentPointer(long id, String contentType, byte[] key, String relay) {
    super(contentType);
    this.id    = id;
    this.key   = key;
    this.relay = Optional.fromNullable(relay);
  }

  public long getId() {
    return id;
  }

  public byte[] getKey() {
    return key;
  }

  @Override
  public boolean isStream() {
    return false;
  }

  @Override
  public boolean isPointer() {
    return true;
  }

  public Optional<String> getRelay() {
    return relay;
  }
}
