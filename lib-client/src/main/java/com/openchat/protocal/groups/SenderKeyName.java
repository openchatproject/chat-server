package com.openchat.protocal.groups;

import com.openchat.protocal.OpenchatAddress;


public class SenderKeyName {

  private final String         groupId;
  private final OpenchatAddress sender;

  public SenderKeyName(String groupId, OpenchatAddress sender) {
    this.groupId  = groupId;
    this.sender   = sender;
  }

  public String getGroupId() {
    return groupId;
  }

  public OpenchatAddress getSender() {
    return sender;
  }

  public String serialize() {
    return groupId + "::" + sender.getName() + "::" + String.valueOf(sender.getDeviceId());
  }

  @Override
  public boolean equals(Object other) {
    if (other == null)                     return false;
    if (!(other instanceof SenderKeyName)) return false;

    SenderKeyName that = (SenderKeyName)other;

    return
        this.groupId.equals(that.groupId) &&
        this.sender.equals(that.sender);
  }

  @Override
  public int hashCode() {
    return this.groupId.hashCode() ^ this.sender.hashCode();
  }

}
