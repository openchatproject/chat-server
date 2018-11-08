package com.openchat.imservice.api;

import com.google.protobuf.ByteString;

import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.IdentityKeyPair;
import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.ecc.ECPublicKey;
import com.openchat.protocal.state.PreKeyRecord;
import com.openchat.protocal.state.SignedPreKeyRecord;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.messages.multidevice.DeviceInfo;
import com.openchat.imservice.api.push.ContactTokenDetails;
import com.openchat.imservice.api.push.SignedPreKeyEntity;
import com.openchat.imservice.api.push.TrustStore;
import com.openchat.imservice.internal.crypto.ProvisioningCipher;
import com.openchat.imservice.internal.push.PushServiceSocket;
import com.openchat.imservice.internal.util.Base64;
import com.openchat.imservice.internal.util.StaticCredentialsProvider;
import com.openchat.imservice.internal.util.Util;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.openchat.imservice.internal.push.ProvisioningProtos.ProvisionMessage;

public class OpenchatServiceAccountManager {

  private final PushServiceSocket pushServiceSocket;
  private final String            user;

  
  public OpenchatServiceAccountManager(String url, TrustStore trustStore,
                                  String user, String password)
  {
    this.pushServiceSocket = new PushServiceSocket(url, trustStore, new StaticCredentialsProvider(user, password, null));
    this.user              = user;
  }

  
  public void setGcmId(Optional<String> gcmRegistrationId) throws IOException {
    if (gcmRegistrationId.isPresent()) {
      this.pushServiceSocket.registerGcmId(gcmRegistrationId.get());
    } else {
      this.pushServiceSocket.unregisterGcmId();
    }
  }

  
  public void requestSmsVerificationCode() throws IOException {
    this.pushServiceSocket.createAccount(false);
  }

  
  public void requestVoiceVerificationCode() throws IOException {
    this.pushServiceSocket.createAccount(true);
  }

  
  public void verifyAccount(String verificationCode, String openchatingKey,
                            boolean supportsSms, int axolotlRegistrationId)
      throws IOException
  {
    this.pushServiceSocket.verifyAccount(verificationCode, openchatingKey,
                                         supportsSms, axolotlRegistrationId);
  }

  
  public void setPreKeys(IdentityKey identityKey, PreKeyRecord lastResortKey,
                         SignedPreKeyRecord signedPreKey, List<PreKeyRecord> oneTimePreKeys)
      throws IOException
  {
    this.pushServiceSocket.registerPreKeys(identityKey, lastResortKey, signedPreKey, oneTimePreKeys);
  }

  
  public int getPreKeysCount() throws IOException {
    return this.pushServiceSocket.getAvailablePreKeys();
  }

  
  public void setSignedPreKey(SignedPreKeyRecord signedPreKey) throws IOException {
    this.pushServiceSocket.setCurrentSignedPreKey(signedPreKey);
  }

  
  public SignedPreKeyEntity getSignedPreKey() throws IOException {
    return this.pushServiceSocket.getCurrentSignedPreKey();
  }

  
  public Optional<ContactTokenDetails> getContact(String e164number) throws IOException {
    String              contactToken        = createDirectoryServerToken(e164number, true);
    ContactTokenDetails contactTokenDetails = this.pushServiceSocket.getContactTokenDetails(contactToken);

    if (contactTokenDetails != null) {
      contactTokenDetails.setNumber(e164number);
    }

    return Optional.fromNullable(contactTokenDetails);
  }

  
  public List<ContactTokenDetails> getContacts(Set<String> e164numbers)
      throws IOException
  {
    Map<String, String>       contactTokensMap = createDirectoryServerTokenMap(e164numbers);
    List<ContactTokenDetails> activeTokens     = this.pushServiceSocket.retrieveDirectory(contactTokensMap.keySet());

    for (ContactTokenDetails activeToken : activeTokens) {
      activeToken.setNumber(contactTokensMap.get(activeToken.getToken()));
    }

    return activeTokens;
  }

  public String getNewDeviceVerificationCode() throws IOException {
    return this.pushServiceSocket.getNewDeviceVerificationCode();
  }

  public void addDevice(String deviceIdentifier,
                        ECPublicKey deviceKey,
                        IdentityKeyPair identityKeyPair,
                        String code)
      throws InvalidKeyException, IOException
  {
    ProvisioningCipher cipher  = new ProvisioningCipher(deviceKey);
    ProvisionMessage   message = ProvisionMessage.newBuilder()
                                                 .setIdentityKeyPublic(ByteString.copyFrom(identityKeyPair.getPublicKey().serialize()))
                                                 .setIdentityKeyPrivate(ByteString.copyFrom(identityKeyPair.getPrivateKey().serialize()))
                                                 .setNumber(user)
                                                 .setProvisioningCode(code)
                                                 .build();

    byte[] ciphertext = cipher.encrypt(message);
    this.pushServiceSocket.sendProvisioningMessage(deviceIdentifier, ciphertext);
  }

  public List<DeviceInfo> getDevices() throws IOException {
    return this.pushServiceSocket.getDevices();
  }

  public void removeDevice(long deviceId) throws IOException {
    this.pushServiceSocket.removeDevice(deviceId);
  }

  private String createDirectoryServerToken(String e164number, boolean urlSafe) {
    try {
      MessageDigest digest  = MessageDigest.getInstance("SHA1");
      byte[]        token   = Util.trim(digest.digest(e164number.getBytes()), 10);
      String        encoded = Base64.encodeBytesWithoutPadding(token);

      if (urlSafe) return encoded.replace('+', '-').replace('/', '_');
      else         return encoded;
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError(e);
    }
  }

  private Map<String, String> createDirectoryServerTokenMap(Collection<String> e164numbers) {
    Map<String,String> tokenMap = new HashMap<>(e164numbers.size());

    for (String number : e164numbers) {
      tokenMap.put(createDirectoryServerToken(number, false), number);
    }

    return tokenMap;
  }

}
