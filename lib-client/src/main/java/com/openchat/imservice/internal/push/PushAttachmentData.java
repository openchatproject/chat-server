package com.openchat.imservice.internal.push;

import com.openchat.imservice.api.messages.OpenchatServiceAttachment.ProgressListener;

import java.io.InputStream;

public class PushAttachmentData {

  private final String           contentType;
  private final InputStream      data;
  private final long             dataSize;
  private final byte[]           key;
  private final ProgressListener listener;

  public PushAttachmentData(String contentType, InputStream data, long dataSize,
                            ProgressListener listener, byte[] key)
  {
    this.contentType = contentType;
    this.data        = data;
    this.dataSize    = dataSize;
    this.key         = key;
    this.listener    = listener;
  }

  public String getContentType() {
    return contentType;
  }

  public InputStream getData() {
    return data;
  }

  public long getDataSize() {
    return dataSize;
  }

  public byte[] getKey() {
    return key;
  }

  public ProgressListener getListener() {
    return listener;
  }
}
