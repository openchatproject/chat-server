package com.openchat.messaging.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openchat.messaging.server.internal.GcmRequestEntity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Message {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private final String              collapseKey;
  private final Long                ttl;
  private final Boolean             delayWhileIdle;
  private final Map<String, String> data;
  private final List<String>        registrationIds;

  private Message(String collapseKey, Long ttl, Boolean delayWhileIdle,
                  Map<String, String> data, List<String> registrationIds)
  {
    this.collapseKey     = collapseKey;
    this.ttl             = ttl;
    this.delayWhileIdle  = delayWhileIdle;
    this.data            = data;
    this.registrationIds = registrationIds;
  }

  public String serialize() throws JsonProcessingException {
    GcmRequestEntity requestEntity = new GcmRequestEntity(collapseKey, ttl, delayWhileIdle,
                                                          data, registrationIds);

    return objectMapper.writeValueAsString(requestEntity);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static class Builder {

    private String              collapseKey     = null;
    private Long                ttl             = null;
    private Boolean             delayWhileIdle  = null;
    private Map<String, String> data            = null;
    private List<String>        registrationIds = new LinkedList<>();

    private Builder() {}

    public Builder withCollapseKey(String collapseKey) {
      this.collapseKey = collapseKey;
      return this;
    }

    public Builder withTtl(long seconds) {
      this.ttl = seconds;
      return this;
    }

    public Builder withDelayWhileIdle(boolean delayWhileIdle) {
      this.delayWhileIdle = delayWhileIdle;
      return this;
    }

    public Builder withDataPart(String key, String value) {
      if (data == null) {
        data = new HashMap<>();
      }
      data.put(key, value);
      return this;
    }

    public Builder withDestination(String registrationId) {
      this.registrationIds.clear();
      this.registrationIds.add(registrationId);
      return this;
    }

    public Message build() {
      if (registrationIds.isEmpty()) {
        throw new IllegalArgumentException("You must specify a destination!");
      }

      return new Message(collapseKey, ttl, delayWhileIdle, data, registrationIds);
    }
  }


}
