package com.openchat.imservice.api.messages.multidevice;

import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.messages.OpenchatServiceAttachmentStream;

import java.util.List;

public class DeviceGroup {

  private final byte[]                               id;
  private final Optional<String>                     name;
  private final List<String>                         members;
  private final Optional<OpenchatServiceAttachmentStream> avatar;

  public DeviceGroup(byte[] id, Optional<String> name, List<String> members, Optional<OpenchatServiceAttachmentStream> avatar) {
    this.id      = id;
    this.name    = name;
    this.members = members;
    this.avatar  = avatar;
  }

  public Optional<OpenchatServiceAttachmentStream> getAvatar() {
    return avatar;
  }

  public Optional<String> getName() {
    return name;
  }

  public byte[] getId() {
    return id;
  }

  public List<String> getMembers() {
    return members;
  }

}
