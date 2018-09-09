package com.openchat.secureim.storage;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;

import java.util.LinkedList;
import java.util.List;

public class Account {

  public static final int MEMCACHE_VERION = 5;

  @JsonProperty
  private String number;

  @JsonProperty
  private boolean supportsSms;

  @JsonProperty
  private List<Device> devices = new LinkedList<>();

  @JsonProperty
  private String identityKey;

  @JsonIgnore
  private Device authenticatedDevice;

  public Account() {}

  @VisibleForTesting
  public Account(String number, boolean supportsSms, List<Device> devices) {
    this.number      = number;
    this.supportsSms = supportsSms;
    this.devices     = devices;
  }

  public Optional<Device> getAuthenticatedDevice() {
    return Optional.fromNullable(authenticatedDevice);
  }

  public void setAuthenticatedDevice(Device device) {
    this.authenticatedDevice = device;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getNumber() {
    return number;
  }

  public boolean getSupportsSms() {
    return supportsSms;
  }

  public void setSupportsSms(boolean supportsSms) {
    this.supportsSms = supportsSms;
  }

  public void addDevice(Device device) {
    this.devices.add(device);
  }

  public void setDevices(List<Device> devices) {
    this.devices = devices;
  }

  public List<Device> getDevices() {
    return devices;
  }

  public Optional<Device> getMasterDevice() {
    return getDevice(Device.MASTER_ID);
  }

  public Optional<Device> getDevice(long deviceId) {
    for (Device device : devices) {
      if (device.getId() == deviceId) {
        return Optional.of(device);
      }
    }

    return Optional.absent();
  }

  public boolean isActive() {
    return
        getMasterDevice().isPresent() &&
        getMasterDevice().get().isActive();
  }

  public long getNextDeviceId() {
    long highestDevice = Device.MASTER_ID;

    for (Device device : devices) {
      if (device.getId() > highestDevice) {
        highestDevice = device.getId();
      }
    }

    return highestDevice + 1;
  }

  public boolean isRateLimited() {
    return true;
  }

  public Optional<String> getRelay() {
    return Optional.absent();
  }

  public void setIdentityKey(String identityKey) {
    this.identityKey = identityKey;
  }

  public String getIdentityKey() {
    return identityKey;
  }
}
