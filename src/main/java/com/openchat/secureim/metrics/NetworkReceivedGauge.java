package com.openchat.secureim.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openchat.secureim.util.Pair;

import java.io.IOException;

public class NetworkReceivedGauge extends NetworkGauge {

  private final Logger logger = LoggerFactory.getLogger(NetworkSentGauge.class);

  private long lastTimestamp;
  private long lastReceived;

  @Override
  public Long value() {
    try {
      long             timestamp       = System.currentTimeMillis();
      Pair<Long, Long> sentAndReceived = getSentReceived();
      long             result          = 0;

      if (lastTimestamp != 0) {
        result       = sentAndReceived.second() - lastReceived;
        lastReceived = sentAndReceived.second();
      }

      lastTimestamp = timestamp;
      return result;
    } catch (IOException e) {
      logger.warn("NetworkReceivedGauge", e);
      return -1L;
    }
  }

}
