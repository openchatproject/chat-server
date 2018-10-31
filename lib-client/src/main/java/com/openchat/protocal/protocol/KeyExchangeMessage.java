package com.openchat.protocal.protocol;


import com.google.protobuf.ByteString;

import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.InvalidMessageException;
import com.openchat.protocal.InvalidVersionException;
import com.openchat.protocal.LegacyMessageException;
import com.openchat.protocal.ecc.Curve;
import com.openchat.protocal.ecc.ECPublicKey;
import com.openchat.protocal.util.ByteUtil;

import java.io.IOException;

import static com.openchat.protocal.protocol.OpenchatProtos.KeyExchangeMessage.Builder;

public class KeyExchangeMessage {

  public static final int INITIATE_FLAG              = 0x01;
  public static final int RESPONSE_FLAG              = 0X02;
  public static final int SIMULTAENOUS_INITIATE_FLAG = 0x04;

  private final int         version;
  private final int         supportedVersion;
  private final int         sequence;
  private final int         flags;

  private final ECPublicKey baseKey;
  private final byte[]      baseKeySignature;
  private final ECPublicKey ratchetKey;
  private final IdentityKey identityKey;
  private final byte[]      serialized;

  public KeyExchangeMessage(int messageVersion, int sequence, int flags,
                            ECPublicKey baseKey, byte[] baseKeySignature,
                            ECPublicKey ratchetKey,
                            IdentityKey identityKey)
  {
    this.supportedVersion = CiphertextMessage.CURRENT_VERSION;
    this.version          = messageVersion;
    this.sequence         = sequence;
    this.flags            = flags;
    this.baseKey          = baseKey;
    this.baseKeySignature = baseKeySignature;
    this.ratchetKey       = ratchetKey;
    this.identityKey      = identityKey;

    byte[]  version = {ByteUtil.intsToByteHighAndLow(this.version, this.supportedVersion)};
    Builder builder = OpenchatProtos.KeyExchangeMessage
                                   .newBuilder()
                                   .setId((sequence << 5) | flags)
                                   .setBaseKey(ByteString.copyFrom(baseKey.serialize()))
                                   .setRatchetKey(ByteString.copyFrom(ratchetKey.serialize()))
                                   .setIdentityKey(ByteString.copyFrom(identityKey.serialize()));

    if (messageVersion >= 3) {
      builder.setBaseKeySignature(ByteString.copyFrom(baseKeySignature));
    }

    this.serialized = ByteUtil.combine(version, builder.build().toByteArray());
  }

  public KeyExchangeMessage(byte[] serialized)
      throws InvalidMessageException, InvalidVersionException, LegacyMessageException
  {
    try {
      byte[][] parts        = ByteUtil.split(serialized, 1, serialized.length - 1);
      this.version          = ByteUtil.highBitsToInt(parts[0][0]);
      this.supportedVersion = ByteUtil.lowBitsToInt(parts[0][0]);

      if (this.version < CiphertextMessage.CURRENT_VERSION) {
        throw new LegacyMessageException("Unsupported legacy version: " + this.version);
      }

      if (this.version > CiphertextMessage.CURRENT_VERSION) {
        throw new InvalidVersionException("Unknown version: " + this.version);
      }

      OpenchatProtos.KeyExchangeMessage message = OpenchatProtos.KeyExchangeMessage.parseFrom(parts[1]);

      if (!message.hasId()           || !message.hasBaseKey()     ||
          !message.hasRatchetKey()   || !message.hasIdentityKey() ||
          !message.hasBaseKeySignature())
      {
        throw new InvalidMessageException("Some required fields missing!");
      }

      this.sequence         = message.getId() >> 5;
      this.flags            = message.getId() & 0x1f;
      this.serialized       = serialized;
      this.baseKey          = Curve.decodePoint(message.getBaseKey().toByteArray(), 0);
      this.baseKeySignature = message.getBaseKeySignature().toByteArray();
      this.ratchetKey       = Curve.decodePoint(message.getRatchetKey().toByteArray(), 0);
      this.identityKey      = new IdentityKey(message.getIdentityKey().toByteArray(), 0);
    } catch (InvalidKeyException | IOException e) {
      throw new InvalidMessageException(e);
    }
  }

  public int getVersion() {
    return version;
  }

  public ECPublicKey getBaseKey() {
    return baseKey;
  }

  public byte[] getBaseKeySignature() {
    return baseKeySignature;
  }

  public ECPublicKey getRatchetKey() {
    return ratchetKey;
  }

  public IdentityKey getIdentityKey() {
    return identityKey;
  }

  public boolean hasIdentityKey() {
    return true;
  }

  public int getMaxVersion() {
    return supportedVersion;
  }

  public boolean isResponse() {
    return ((flags & RESPONSE_FLAG) != 0);
  }

  public boolean isInitiate() {
    return (flags & INITIATE_FLAG) != 0;
  }

  public boolean isResponseForSimultaneousInitiate() {
    return (flags & SIMULTAENOUS_INITIATE_FLAG) != 0;
  }

  public int getFlags() {
    return flags;
  }

  public int getSequence() {
    return sequence;
  }

  public byte[] serialize() {
    return serialized;
  }
}
