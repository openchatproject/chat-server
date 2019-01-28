package com.openchat.secureim.push;

import android.content.Context;

import com.openchat.secureim.R;
import com.openchat.imservice.api.push.TrustStore;

import java.io.InputStream;

public class openchatServiceTrustStore implements TrustStore {

  private final Context context;

  public openchatServiceTrustStore(Context context) {
    this.context = context.getApplicationContext();
  }

  @Override
  public InputStream getKeyStoreInputStream() {
    return context.getResources().openRawResource(R.raw.whisper);
  }

  @Override
  public String getKeyStorePassword() {
    return "openchat";
  }
}
