package com.openchat.secureim.storage;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.openchat.secureim.auth.AuthenticationCredentials;
import com.openchat.secureim.entities.DeviceKey;
import com.openchat.secureim.entities.PreKeyV2;
import com.openchat.secureim.util.Util;

import java.io.Serializable;

public class Device implements Serializable {

  public static final long MASTER_ID = 1;

  @JsonProperty
  private long    id;

  @JsonProperty
  private String  authToken;

  @JsonProperty
  private String  salt;

  @JsonProperty
  private String  signalingKey;

  @JsonProperty
  private String  gcmId;

  @JsonProperty
  private String  apnId;

  @JsonProperty
  private boolean fetchesMessages;

  @JsonProperty
  private int registrationId;

  @JsonProperty
  private DeviceKey deviceKey;

  public Device() {}

  public Device(long id, String authToken, String salt,
                String signalingKey, String gcmId, String apnId,
                boolean fetchesMessages, int registrationId,
                DeviceKey deviceKey)
  {
    this.id              = id;
    this.authToken       = authToken;
    this.salt            = salt;
    this.signalingKey    = signalingKey;
    this.gcmId           = gcmId;
    this.apnId           = apnId;
    this.fetchesMessages = fetchesMessages;
    this.registrationId  = registrationId;
    this.deviceKey       = deviceKey;
  }

  public String getApnId() {
    return apnId;
  }

  public void setApnId(String apnId) {
    this.apnId = apnId;
  }

  public String getGcmId() {
    return gcmId;
  }

  public void setGcmId(String gcmId) {
    this.gcmId = gcmId;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setAuthenticationCredentials(AuthenticationCredentials credentials) {
    this.authToken = credentials.getHashedAuthenticationToken();
    this.salt      = credentials.getSalt();
  }

  public AuthenticationCredentials getAuthenticationCredentials() {
    return new AuthenticationCredentials(authToken, salt);
  }

  public String getSignalingKey() {
    return signalingKey;
  }

  public void setSignalingKey(String signalingKey) {
    this.signalingKey = signalingKey;
  }

  public boolean isActive() {
    return fetchesMessages || !Util.isEmpty(getApnId()) || !Util.isEmpty(getGcmId());
  }

  public boolean getFetchesMessages() {
    return fetchesMessages;
  }

  public void setFetchesMessages(boolean fetchesMessages) {
    this.fetchesMessages = fetchesMessages;
  }

  public boolean isMaster() {
    return getId() == MASTER_ID;
  }

  public int getRegistrationId() {
    return registrationId;
  }

  public void setRegistrationId(int registrationId) {
    this.registrationId = registrationId;
  }

  public DeviceKey getDeviceKey() {
    return deviceKey;
  }

  public void setDeviceKey(DeviceKey deviceKey) {
    this.deviceKey = deviceKey;
  }
}
