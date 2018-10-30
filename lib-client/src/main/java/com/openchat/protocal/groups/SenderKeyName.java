package com.openchat.protocal.groups;


public class SenderKeyName {

  private final String groupId;
  private final long   senderId;
  private final int    deviceId;

  public SenderKeyName(String groupId, long senderId, int deviceId) {
    this.groupId  = groupId;
    this.senderId = senderId;
    this.deviceId = deviceId;
  }

  public String getGroupId() {
    return groupId;
  }

  public long getSenderId() {
    return senderId;
  }

  public int getDeviceId() {
    return deviceId;
  }

  public String serialize() {
    return groupId + "::" + String.valueOf(senderId) + "::" + String.valueOf(deviceId);
  }

  @Override
  public boolean equals(Object other) {
    if (other == null)                     return false;
    if (!(other instanceof SenderKeyName)) return false;

    SenderKeyName that = (SenderKeyName)other;

    return
        this.groupId.equals(that.groupId) &&
        this.senderId == that.senderId &&
        this.deviceId == that.deviceId;
  }

  @Override
  public int hashCode() {
    return this.groupId.hashCode() ^ (int)this.senderId ^ this.deviceId;
  }

}
