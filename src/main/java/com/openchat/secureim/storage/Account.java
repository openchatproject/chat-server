package com.openchat.secureim.storage;


import com.openchat.secureim.auth.AuthenticationCredentials;
import com.openchat.secureim.util.Util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Account implements Serializable {
  private String  number;
  private boolean supportsSms;
  private Map<Long, Device> devices = new HashMap<>();

  private Account(String number, boolean supportsSms) {
    this.number      = number;
    this.supportsSms = supportsSms;
  }

  public Account(String number, boolean supportsSms, Device onlyDevice) {
    this(number, supportsSms);
    this.devices.put(onlyDevice.getDeviceId(), onlyDevice);
  }

  public Account(String number, boolean supportsSms, List<Device> devices) {
    this(number, supportsSms);
    for (Device device : devices)
      this.devices.put(device.getDeviceId(), device);
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

  public boolean isActive() {
    Device masterDevice = devices.get((long) 1);
    return masterDevice != null && masterDevice.isActive();
  }

  public Collection<Device> getDevices() {
    return devices.values();
  }

  public Device getDevice(long destinationDeviceId) {
    return devices.get(destinationDeviceId);
  }

  public boolean hasAllDeviceIds(Set<Long> deviceIds) {
    if (devices.size() != deviceIds.size())
      return false;
    for (long deviceId : devices.keySet()) {
      if (!deviceIds.contains(deviceId))
        return false;
    }
    return true;
  }
}
