package com.openchat.imservice.internal.push;

import com.openchat.imservice.api.messages.OpenchatServiceAttachment.ProgressListener;
import com.openchat.imservice.internal.push.http.OutputStreamFactory;

import java.io.InputStream;

public class PushAttachmentData {

  private final String              contentType;
  private final InputStream         data;
  private final long                dataSize;
  private final OutputStreamFactory outputStreamFactory;
  private final ProgressListener    listener;

  public PushAttachmentData(String contentType, InputStream data, long dataSize,
                            OutputStreamFactory outputStreamFactory, ProgressListener listener)
  {
    this.contentType         = contentType;
    this.data                = data;
    this.dataSize            = dataSize;
    this.outputStreamFactory = outputStreamFactory;
    this.listener            = listener;
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

  public OutputStreamFactory getOutputStreamFactory() {
    return outputStreamFactory;
  }

  public ProgressListener getListener() {
    return listener;
  }
}
