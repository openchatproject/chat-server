package com.openchat.secureim.storage;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;

import java.util.HashSet;
import java.util.Set;

public class Account {

  public static final int MEMCACHE_VERION = 5;

  @JsonProperty
  private String number;

  @JsonProperty
  private Set<Device> devices = new HashSet<>();

  @JsonProperty
  private String identityKey;

  @JsonIgnore
  private Device authenticatedDevice;

  public Account() {}

  @VisibleForTesting
  public Account(String number, Set<Device> devices) {
    this.number  = number;
    this.devices = devices;
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

  public void addDevice(Device device) {
    this.devices.remove(device);
    this.devices.add(device);
  }

  public void removeDevice(long deviceId) {
    this.devices.remove(new Device(deviceId, null, null, null, null, null, null, null, false, 0, null, 0, 0, false, "NA"));
  }

  public Set<Device> getDevices() {
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

  public boolean isVoiceSupported() {
    for (Device device : devices) {
      if (device.isActive() && device.isVoiceSupported()) {
        return true;
      }
    }

    return false;
  }

  public boolean isActive() {
    return
        getMasterDevice().isPresent() &&
        getMasterDevice().get().isActive();
  }

  public long getNextDeviceId() {
    long highestDevice = Device.MASTER_ID;

    for (Device device : devices) {
      if (!device.isActive()) {
        return device.getId();
      } else if (device.getId() > highestDevice) {
        highestDevice = device.getId();
      }
    }

    return highestDevice + 1;
  }

  public int getActiveDeviceCount() {
    int count = 0;

    for (Device device : devices) {
      if (device.isActive()) count++;
    }

    return count;
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
