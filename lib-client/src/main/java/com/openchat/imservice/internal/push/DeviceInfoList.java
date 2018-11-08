package com.openchat.imservice.internal.push;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.openchat.imservice.api.messages.multidevice.DeviceInfo;

import java.util.List;

public class DeviceInfoList {

  @JsonProperty
  private List<DeviceInfo> devices;

  public DeviceInfoList() {}

  public List<DeviceInfo> getDevices() {
    return devices;
  }
}
