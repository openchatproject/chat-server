package com.openchat.secureim.push;

import android.content.Context;

import com.openchat.secureim.R;
import com.openchat.imservice.api.push.TrustStore;
import com.openchat.imservice.internal.push.PushServiceSocket;

import java.io.InputStream;

public class OpenchatServicePushTrustStore implements TrustStore {

  private final Context context;

  public OpenchatServicePushTrustStore(Context context) {
    this.context = context.getApplicationContext();
  }

  @Override
  public InputStream getKeyStoreInputStream() {
    return context.getResources().openRawResource(R.raw.openchat);
  }

  @Override
  public String getKeyStorePassword() {
    return "openchat";
  }
}
