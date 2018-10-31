package com.openchat.protocal.logging;

public class OpenchatProtocolLoggerProvider {

  private static OpenchatProtocolLogger provider;

  public static OpenchatProtocolLogger getProvider() {
    return provider;
  }

  public static void setProvider(OpenchatProtocolLogger provider) {
    OpenchatProtocolLoggerProvider.provider = provider;
  }
}
