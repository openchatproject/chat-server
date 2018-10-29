package com.openchat.protocal.logging;

public class OpenchatLoggerProvider {

  private static OpenchatLogger provider;

  public static OpenchatLogger getProvider() {
    return provider;
  }

  public static void setProvider(OpenchatLogger provider) {
    OpenchatLoggerProvider.provider = provider;
  }
}
