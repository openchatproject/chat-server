package com.openchat.imservice.api.util;

import com.openchat.imservice.api.util.SleepTimer;

public class UptimeSleepTimer implements SleepTimer {
  @Override
  public void sleep(long millis) throws InterruptedException {
    Thread.sleep(millis);
  }
}
