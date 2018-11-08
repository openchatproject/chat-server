package com.openchat.imservice.api.messages;

import com.google.protobuf.ByteString;

import com.openchat.protocal.InvalidVersionException;
import com.openchat.protocal.logging.Log;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.push.OpenchatServiceAddress;
import com.openchat.imservice.internal.push.OpenchatServiceProtos.Envelope;
import com.openchat.imservice.internal.util.Base64;
import com.openchat.imservice.internal.util.Hex;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class OpenchatServiceEnvelope {

  private static final String TAG = OpenchatServiceEnvelope.class.getSimpleName();

  private static final int SUPPORTED_VERSION =  1;
  private static final int CIPHER_KEY_SIZE   = 32;
  private static final int MAC_KEY_SIZE      = 20;
  private static final int MAC_SIZE          = 10;

  private static final int VERSION_OFFSET    =  0;
  private static final int VERSION_LENGTH    =  1;
  private static final int IV_OFFSET         = VERSION_OFFSET + VERSION_LENGTH;
  private static final int IV_LENGTH         = 16;
  private static final int CIPHERTEXT_OFFSET = IV_OFFSET + IV_LENGTH;

  private final Envelope envelope;

  
  public OpenchatServiceEnvelope(String message, String openchatingKey)
      throws IOException, InvalidVersionException
  {
    this(Base64.decode(message), openchatingKey);
  }

  
  public OpenchatServiceEnvelope(byte[] ciphertext, String openchatingKey)
      throws InvalidVersionException, IOException
  {
    if (ciphertext.length < VERSION_LENGTH || ciphertext[VERSION_OFFSET] != SUPPORTED_VERSION)
      throw new InvalidVersionException("Unsupported version!");

    SecretKeySpec cipherKey  = getCipherKey(openchatingKey);
    SecretKeySpec macKey     = getMacKey(openchatingKey);

    verifyMac(ciphertext, macKey);

    this.envelope = Envelope.parseFrom(getPlaintext(ciphertext, cipherKey));
  }

  public OpenchatServiceEnvelope(int type, String source, int sourceDevice,
                            String relay, long timestamp,
                            byte[] legacyMessage, byte[] content)
  {
    Envelope.Builder builder = Envelope.newBuilder()
                                       .setType(Envelope.Type.valueOf(type))
                                       .setSource(source)
                                       .setSourceDevice(sourceDevice)
                                       .setRelay(relay)
                                       .setTimestamp(timestamp);

    if (legacyMessage != null) builder.setLegacyMessage(ByteString.copyFrom(legacyMessage));
    if (content != null)       builder.setContent(ByteString.copyFrom(content));

    this.envelope = builder.build();
  }

  
  public String getSource() {
    return envelope.getSource();
  }

  
  public int getSourceDevice() {
    return envelope.getSourceDevice();
  }

  
  public OpenchatServiceAddress getSourceAddress() {
    return new OpenchatServiceAddress(envelope.getSource(),
                                 envelope.hasRelay() ? Optional.fromNullable(envelope.getRelay()) :
                                                     Optional.<String>absent());
  }

  
  public int getType() {
    return envelope.getType().getNumber();
  }

  
  public String getRelay() {
    return envelope.getRelay();
  }

  
  public long getTimestamp() {
    return envelope.getTimestamp();
  }

  
  public boolean hasLegacyMessage() {
    return envelope.hasLegacyMessage();
  }

  
  public byte[] getLegacyMessage() {
    return envelope.getLegacyMessage().toByteArray();
  }

  
  public boolean hasContent() {
    return envelope.hasContent();
  }

  
  public byte[] getContent() {
    return envelope.getContent().toByteArray();
  }

  
  public boolean isOpenchatMessage() {
    return envelope.getType().getNumber() == Envelope.Type.CIPHERTEXT_VALUE;
  }

  
  public boolean isPreKeyOpenchatMessage() {
    return envelope.getType().getNumber() == Envelope.Type.PREKEY_BUNDLE_VALUE;
  }

  
  public boolean isReceipt() {
    return envelope.getType().getNumber() == Envelope.Type.RECEIPT_VALUE;
  }

  private byte[] getPlaintext(byte[] ciphertext, SecretKeySpec cipherKey) throws IOException {
    try {
      byte[] ivBytes = new byte[IV_LENGTH];
      System.arraycopy(ciphertext, IV_OFFSET, ivBytes, 0, ivBytes.length);
      IvParameterSpec iv = new IvParameterSpec(ivBytes);

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(Cipher.DECRYPT_MODE, cipherKey, iv);

      return cipher.doFinal(ciphertext, CIPHERTEXT_OFFSET,
                            ciphertext.length - VERSION_LENGTH - IV_LENGTH - MAC_SIZE);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException e) {
      throw new AssertionError(e);
    } catch (BadPaddingException e) {
      Log.w(TAG, e);
      throw new IOException("Bad padding?");
    }
  }

  private void verifyMac(byte[] ciphertext, SecretKeySpec macKey) throws IOException {
    try {
      Mac mac = Mac.getInstance("HmacSHA256");
      mac.init(macKey);

      if (ciphertext.length < MAC_SIZE + 1)
        throw new IOException("Invalid MAC!");

      mac.update(ciphertext, 0, ciphertext.length - MAC_SIZE);

      byte[] ourMacFull  = mac.doFinal();
      byte[] ourMacBytes = new byte[MAC_SIZE];
      System.arraycopy(ourMacFull, 0, ourMacBytes, 0, ourMacBytes.length);

      byte[] theirMacBytes = new byte[MAC_SIZE];
      System.arraycopy(ciphertext, ciphertext.length-MAC_SIZE, theirMacBytes, 0, theirMacBytes.length);

      Log.w(TAG, "Our MAC: " + Hex.toString(ourMacBytes));
      Log.w(TAG, "Thr MAC: " + Hex.toString(theirMacBytes));

      if (!Arrays.equals(ourMacBytes, theirMacBytes)) {
        throw new IOException("Invalid MAC compare!");
      }
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      throw new AssertionError(e);
    }
  }

  private SecretKeySpec getCipherKey(String openchatingKey) throws IOException {
    byte[] openchatingKeyBytes = Base64.decode(openchatingKey);
    byte[] cipherKey         = new byte[CIPHER_KEY_SIZE];
    System.arraycopy(openchatingKeyBytes, 0, cipherKey, 0, cipherKey.length);

    return new SecretKeySpec(cipherKey, "AES");
  }

  private SecretKeySpec getMacKey(String openchatingKey) throws IOException {
    byte[] openchatingKeyBytes = Base64.decode(openchatingKey);
    byte[] macKey            = new byte[MAC_KEY_SIZE];
    System.arraycopy(openchatingKeyBytes, CIPHER_KEY_SIZE, macKey, 0, macKey.length);

    return new SecretKeySpec(macKey, "HmacSHA256");
  }

}
