package com.openchat.imservice.api.messages;

public abstract class OpenchatServiceAttachment {

  private final String contentType;

  protected OpenchatServiceAttachment(String contentType) {
    this.contentType = contentType;
  }

  public String getContentType() {
    return contentType;
  }

  public abstract boolean isStream();
  public abstract boolean isPointer();

  public OpenchatServiceAttachmentStream asStream() {
    return (OpenchatServiceAttachmentStream)this;
  }

  public OpenchatServiceAttachmentPointer asPointer() {
    return (OpenchatServiceAttachmentPointer)this;
  }
}
