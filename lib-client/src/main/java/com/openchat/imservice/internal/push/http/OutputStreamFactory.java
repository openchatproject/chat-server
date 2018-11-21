package com.openchat.imservice.internal.push.http;

import com.openchat.imservice.api.crypto.DigestingOutputStream;

import java.io.IOException;
import java.io.OutputStream;

public interface OutputStreamFactory {

  public DigestingOutputStream createFor(OutputStream wrap) throws IOException;

}
