package com.openchat.imservice.api.messages;

import com.openchat.protocal.util.guava.Optional;

import java.util.List;

public class OpenchatServiceGroup {

  public enum Type {
    UNKNOWN,
    UPDATE,
    DELIVER,
    QUIT
  }

  private final byte[]                         groupId;
  private final Type                           type;
  private final Optional<String>               name;
  private final Optional<List<String>>         members;
  private final Optional<OpenchatServiceAttachment> avatar;

  
  public OpenchatServiceGroup(byte[] groupId) {
    this(Type.DELIVER, groupId, null, null, null);
  }

  
  public OpenchatServiceGroup(Type type, byte[] groupId, String name,
                         List<String> members,
                         OpenchatServiceAttachment avatar)
  {
    this.type    = type;
    this.groupId = groupId;
    this.name    = Optional.fromNullable(name);
    this.members = Optional.fromNullable(members);
    this.avatar  = Optional.fromNullable(avatar);
  }

  public byte[] getGroupId() {
    return groupId;
  }

  public Type getType() {
    return type;
  }

  public Optional<String> getName() {
    return name;
  }

  public Optional<List<String>> getMembers() {
    return members;
  }

  public Optional<OpenchatServiceAttachment> getAvatar() {
    return avatar;
  }

}
