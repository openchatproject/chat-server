package com.openchat.secureim.limits;

import java.io.Serializable;

public class LeakyBucket implements Serializable {

  private final int    bucketSize;
  private final double leakRatePerMillis;

  private int spaceRemaining;
  private long lastUpdateTimeMillis;

  public LeakyBucket(int bucketSize, double leakRatePerMillis) {
    this.bucketSize           = bucketSize;
    this.leakRatePerMillis    = leakRatePerMillis;
    this.spaceRemaining       = bucketSize;
    this.lastUpdateTimeMillis = System.currentTimeMillis();
  }

  public boolean add(int amount) {
    this.spaceRemaining = getUpdatedSpaceRemaining();

    if (this.spaceRemaining >= amount) {
      this.spaceRemaining -= amount;
      return true;
    } else {
      return false;
    }
  }

  private int getUpdatedSpaceRemaining() {
    long elapsedTime = System.currentTimeMillis() - this.lastUpdateTimeMillis;

    return Math.min(this.bucketSize,
                    (int)Math.floor(this.spaceRemaining + (elapsedTime * this.leakRatePerMillis)));
  }
}
