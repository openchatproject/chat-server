package com.openchat.imservice.api.messages;

import java.io.InputStream;

public class OpenchatServiceAttachmentStream extends OpenchatServiceAttachment {

  private final InputStream      inputStream;
  private final long             length;
  private final ProgressListener listener;

  public OpenchatServiceAttachmentStream(InputStream inputStream, String contentType, long length, ProgressListener listener) {
    super(contentType);
    this.inputStream = inputStream;
    this.length      = length;
    this.listener    = listener;
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

  public ProgressListener getListener() {
    return listener;
  }
}
