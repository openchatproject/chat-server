package com.openchat.secureim.storage;


import com.openchat.secureim.auth.AuthenticationCredentials;

import java.io.Serializable;

public class Account implements Serializable {

  public static final int MEMCACHE_VERION = 1;

  private long    id;
  private String  number;
  private long    deviceId;
  private String  hashedAuthenticationToken;
  private String  salt;
  private String  signalingKey;
  private String  gcmRegistrationId;
  private String  apnRegistrationId;
  private boolean supportsSms;
  private boolean fetchesMessages;

  public Account() {}

  public Account(long id, String number, long deviceId, String hashedAuthenticationToken, String salt,
                 String signalingKey, String gcmRegistrationId, String apnRegistrationId,
                 boolean supportsSms, boolean fetchesMessages)
  {
    this.id                        = id;
    this.number                    = number;
    this.deviceId                  = deviceId;
    this.hashedAuthenticationToken = hashedAuthenticationToken;
    this.salt                      = salt;
    this.signalingKey              = signalingKey;
    this.gcmRegistrationId         = gcmRegistrationId;
    this.apnRegistrationId         = apnRegistrationId;
    this.supportsSms               = supportsSms;
    this.fetchesMessages           = fetchesMessages;
  }

  public String getApnRegistrationId() {
    return apnRegistrationId;
  }

  public void setApnRegistrationId(String apnRegistrationId) {
    this.apnRegistrationId = apnRegistrationId;
  }

  public String getGcmRegistrationId() {
    return gcmRegistrationId;
  }

  public void setGcmRegistrationId(String gcmRegistrationId) {
    this.gcmRegistrationId = gcmRegistrationId;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getNumber() {
    return number;
  }

  public long getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(long deviceId) {
    this.deviceId = deviceId;
  }

  public void setAuthenticationCredentials(AuthenticationCredentials credentials) {
    this.hashedAuthenticationToken = credentials.getHashedAuthenticationToken();
    this.salt                      = credentials.getSalt();
  }

  public AuthenticationCredentials getAuthenticationCredentials() {
    return new AuthenticationCredentials(hashedAuthenticationToken, salt);
  }

  public String getSignalingKey() {
    return signalingKey;
  }

  public void setSignalingKey(String signalingKey) {
    this.signalingKey = signalingKey;
  }

  public boolean getSupportsSms() {
    return supportsSms;
  }

  public void setSupportsSms(boolean supportsSms) {
    this.supportsSms = supportsSms;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setFetchesMessages(boolean fetchesMessages) {
    this.fetchesMessages = fetchesMessages;
  }

  public boolean getFetchesMessages() {
    return fetchesMessages;
  }
}
