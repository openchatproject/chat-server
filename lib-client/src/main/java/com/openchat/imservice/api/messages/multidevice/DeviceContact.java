package com.openchat.imservice.api.messages.multidevice;

import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.messages.OpenchatServiceAttachmentStream;

public class DeviceContact {

  private final String                                  number;
  private final Optional<String>                        name;
  private final Optional<OpenchatServiceAttachmentStream> avatar;
  private final Optional<String>                        color;
  private final Optional<VerifiedMessage>               verified;
  private final Optional<byte[]>                        profileKey;
  private final boolean                                 blocked;
  private final Optional<Integer>                       expirationTimer;

  public DeviceContact(String number, Optional<String> name,
                       Optional<OpenchatServiceAttachmentStream> avatar,
                       Optional<String> color,
                       Optional<VerifiedMessage> verified,
                       Optional<byte[]> profileKey,
                       boolean blocked,
                       Optional<Integer> expirationTimer)
  {
    this.number          = number;
    this.name            = name;
    this.avatar          = avatar;
    this.color           = color;
    this.verified        = verified;
    this.profileKey      = profileKey;
    this.blocked         = blocked;
    this.expirationTimer = expirationTimer;
  }

  public Optional<OpenchatServiceAttachmentStream> getAvatar() {
    return avatar;
  }

  public Optional<String> getName() {
    return name;
  }

  public String getNumber() {
    return number;
  }

  public Optional<String> getColor() {
    return color;
  }

  public Optional<VerifiedMessage> getVerified() {
    return verified;
  }

  public Optional<byte[]> getProfileKey() {
    return profileKey;
  }

  public boolean isBlocked() {
    return blocked;
  }

  public Optional<Integer> getExpirationTimer() {
    return expirationTimer;
  }
}
