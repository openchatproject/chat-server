package com.openchat.imservice.api.messages;

import java.io.InputStream;

public class OpenchatServiceAttachmentStream extends OpenchatServiceAttachment {

  private final InputStream inputStream;
  private final long        length;

  public OpenchatServiceAttachmentStream(InputStream inputStream, String contentType, long length) {
    super(contentType);
    this.inputStream = inputStream;
    this.length      = length;
  }

  @Override
  public boolean isStream() {
    return true;
  }

  @Override
  public boolean isPointer() {
    return false;
  }

  public InputStream getInputStream() {
    return inputStream;
  }

  public long getLength() {
    return length;
  }
}
