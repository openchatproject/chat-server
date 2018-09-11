package com.openchat.secureim.limits;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LeakyBucket {

  @JsonProperty
  private final int    bucketSize;

  @JsonProperty
  private final double leakRatePerMillis;

  @JsonProperty
  private int spaceRemaining;

  @JsonProperty
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
