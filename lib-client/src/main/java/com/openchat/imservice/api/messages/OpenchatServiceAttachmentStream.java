package com.openchat.imservice.api.messages;

import com.openchat.protocal.util.guava.Optional;

import java.io.InputStream;

public class OpenchatServiceAttachmentStream extends OpenchatServiceAttachment {

  private final InputStream      inputStream;
  private final long             length;
  private final Optional<String> fileName;
  private final ProgressListener listener;
  private final Optional<byte[]> preview;
  private final boolean          voiceNote;
  private final int              width;
  private final int              height;

  public OpenchatServiceAttachmentStream(InputStream inputStream, String contentType, long length, Optional<String> fileName, boolean voiceNote, ProgressListener listener) {
    this(inputStream, contentType, length, fileName, voiceNote, Optional.<byte[]>absent(), 0, 0, listener);
  }

  public OpenchatServiceAttachmentStream(InputStream inputStream, String contentType, long length, Optional<String> fileName, boolean voiceNote, Optional<byte[]> preview, int width, int height, ProgressListener listener) {
    super(contentType);
    this.inputStream = inputStream;
    this.length      = length;
    this.fileName    = fileName;
    this.listener    = listener;
    this.voiceNote   = voiceNote;
    this.preview     = preview;
    this.width       = width;
    this.height      = height;
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

  public Optional<String> getFileName() {
    return fileName;
  }

  public ProgressListener getListener() {
    return listener;
  }

  public Optional<byte[]> getPreview() {
    return preview;
  }

  public boolean getVoiceNote() {
    return voiceNote;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }
}
