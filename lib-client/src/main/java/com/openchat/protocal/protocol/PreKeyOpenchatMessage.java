package com.openchat.protocal.protocol;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.InvalidMessageException;
import com.openchat.protocal.InvalidVersionException;
import com.openchat.protocal.LegacyMessageException;
import com.openchat.protocal.ecc.Curve;
import com.openchat.protocal.ecc.ECPublicKey;
import com.openchat.protocal.util.ByteUtil;
import com.openchat.protocal.util.guava.Optional;


public class PreKeyOpenchatMessage implements CiphertextMessage {

  private final int               version;
  private final int               registrationId;
  private final Optional<Integer> preKeyId;
  private final int               signedPreKeyId;
  private final ECPublicKey       baseKey;
  private final IdentityKey       identityKey;
  private final OpenchatMessage     message;
  private final byte[]            serialized;

  public PreKeyOpenchatMessage(byte[] serialized)
      throws InvalidMessageException, InvalidVersionException
  {
    try {
      this.version = ByteUtil.highBitsToInt(serialized[0]);

      if (this.version > CiphertextMessage.CURRENT_VERSION) {
        throw new InvalidVersionException("Unknown version: " + this.version);
      }

      if (this.version < CiphertextMessage.CURRENT_VERSION) {
        throw new LegacyMessageException("Legacy version: " + this.version);
      }

      OpenchatProtos.PreKeyOpenchatMessage preKeyOpenchatMessage
          = OpenchatProtos.PreKeyOpenchatMessage.parseFrom(ByteString.copyFrom(serialized, 1,
                                                                           serialized.length-1));

      if (!preKeyOpenchatMessage.hasSignedPreKeyId()  ||
          !preKeyOpenchatMessage.hasBaseKey()         ||
          !preKeyOpenchatMessage.hasIdentityKey()     ||
          !preKeyOpenchatMessage.hasMessage())
      {
        throw new InvalidMessageException("Incomplete message.");
      }

      this.serialized     = serialized;
      this.registrationId = preKeyOpenchatMessage.getRegistrationId();
      this.preKeyId       = preKeyOpenchatMessage.hasPreKeyId() ? Optional.of(preKeyOpenchatMessage.getPreKeyId()) : Optional.<Integer>absent();
      this.signedPreKeyId = preKeyOpenchatMessage.hasSignedPreKeyId() ? preKeyOpenchatMessage.getSignedPreKeyId() : -1;
      this.baseKey        = Curve.decodePoint(preKeyOpenchatMessage.getBaseKey().toByteArray(), 0);
      this.identityKey    = new IdentityKey(Curve.decodePoint(preKeyOpenchatMessage.getIdentityKey().toByteArray(), 0));
      this.message        = new OpenchatMessage(preKeyOpenchatMessage.getMessage().toByteArray());
    } catch (InvalidProtocolBufferException | InvalidKeyException | LegacyMessageException e) {
      throw new InvalidMessageException(e);
    }
  }

  public PreKeyOpenchatMessage(int messageVersion, int registrationId, Optional<Integer> preKeyId,
                             int signedPreKeyId, ECPublicKey baseKey, IdentityKey identityKey,
                             OpenchatMessage message)
  {
    this.version        = messageVersion;
    this.registrationId = registrationId;
    this.preKeyId       = preKeyId;
    this.signedPreKeyId = signedPreKeyId;
    this.baseKey        = baseKey;
    this.identityKey    = identityKey;
    this.message        = message;

    OpenchatProtos.PreKeyOpenchatMessage.Builder builder =
        OpenchatProtos.PreKeyOpenchatMessage.newBuilder()
                                        .setSignedPreKeyId(signedPreKeyId)
                                        .setBaseKey(ByteString.copyFrom(baseKey.serialize()))
                                        .setIdentityKey(ByteString.copyFrom(identityKey.serialize()))
                                        .setMessage(ByteString.copyFrom(message.serialize()))
                                        .setRegistrationId(registrationId);

    if (preKeyId.isPresent()) {
      builder.setPreKeyId(preKeyId.get());
    }

    byte[] versionBytes = {ByteUtil.intsToByteHighAndLow(this.version, CURRENT_VERSION)};
    byte[] messageBytes = builder.build().toByteArray();

    this.serialized = ByteUtil.combine(versionBytes, messageBytes);
  }

  public int getMessageVersion() {
    return version;
  }

  public IdentityKey getIdentityKey() {
    return identityKey;
  }

  public int getRegistrationId() {
    return registrationId;
  }

  public Optional<Integer> getPreKeyId() {
    return preKeyId;
  }

  public int getSignedPreKeyId() {
    return signedPreKeyId;
  }

  public ECPublicKey getBaseKey() {
    return baseKey;
  }

  public OpenchatMessage getOpenchatMessage() {
    return message;
  }

  @Override
  public byte[] serialize() {
    return serialized;
  }

  @Override
  public int getType() {
    return CiphertextMessage.PREKEY_TYPE;
  }

}
