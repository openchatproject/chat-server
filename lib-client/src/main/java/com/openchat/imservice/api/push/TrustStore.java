package com.openchat.imservice.api.push;

import java.io.InputStream;

public interface TrustStore {
  public InputStream getKeyStoreInputStream();
  public String getKeyStorePassword();
}

