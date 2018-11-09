package com.openchat.imservice.api.messages;

import com.openchat.protocal.util.guava.Optional;

import java.io.InputStream;

public class OpenchatServiceAttachmentStream extends OpenchatServiceAttachment {

  private final InputStream      inputStream;
  private final long             length;
  private final ProgressListener listener;
  private final Optional<byte[]> preview;

  public OpenchatServiceAttachmentStream(InputStream inputStream, String contentType, long length, ProgressListener listener) {
    this(inputStream, contentType, length, Optional.<byte[]>absent(), listener);
  }

  public OpenchatServiceAttachmentStream(InputStream inputStream, String contentType, long length, Optional<byte[]> preview, ProgressListener listener) {
    super(contentType);
    this.inputStream = inputStream;
    this.length      = length;
    this.listener    = listener;
    this.preview     = preview;
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

  public Optional<byte[]> getPreview() {
    return preview;
  }
}
