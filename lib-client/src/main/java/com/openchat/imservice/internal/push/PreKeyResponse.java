package com.openchat.imservice.internal.push;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.InvalidKeyException;
import com.openchat.imservice.internal.util.Base64;
import com.openchat.imservice.internal.util.JsonUtil;

import java.io.IOException;
import java.util.List;

public class PreKeyResponse {

  @JsonProperty
  @JsonSerialize(using = JsonUtil.IdentityKeySerializer.class)
  @JsonDeserialize(using = JsonUtil.IdentityKeyDeserializer.class)
  private IdentityKey identityKey;

  @JsonProperty
  private List<PreKeyResponseItem> devices;

  public IdentityKey getIdentityKey() {
    return identityKey;
  }

  public List<PreKeyResponseItem> getDevices() {
    return devices;
  }

}
