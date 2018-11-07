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

  public static Builder newUpdateBuilder() {
    return new Builder(Type.UPDATE);
  }

  public static Builder newBuilder(Type type) {
    return new Builder(type);
  }

  public static class Builder {

    private Type                 type;
    private byte[]               id;
    private String               name;
    private List<String>         members;
    private OpenchatServiceAttachment avatar;

    private Builder(Type type) {
      this.type = type;
    }

    public Builder withId(byte[] id) {
      this.id = id;
      return this;
    }

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public Builder withMembers(List<String> members) {
      this.members = members;
      return this;
    }

    public Builder withAvatar(OpenchatServiceAttachment avatar) {
      this.avatar = avatar;
      return this;
    }

    public OpenchatServiceGroup build() {
      if (id == null) throw new IllegalArgumentException("No group ID specified!");

      if (type == Type.UPDATE && name == null && members == null && avatar == null) {
        throw new IllegalArgumentException("Group update with no updates!");
      }

      return new OpenchatServiceGroup(type, id, name, members, avatar);
    }

  }

}
