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
  private final String            userAgent;

  
  public OpenchatServiceAccountManager(String url, TrustStore trustStore,
                                  String user, String password,
                                  String userAgent)
  {
    this.pushServiceSocket = new PushServiceSocket(url, trustStore, new StaticCredentialsProvider(user, password, null), userAgent);
    this.user              = user;
    this.userAgent         = userAgent;
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

  
  public void verifyAccountWithCode(String verificationCode, String openchatingKey, int axolotlRegistrationId, boolean voice)
      throws IOException
  {
    this.pushServiceSocket.verifyAccountCode(verificationCode, openchatingKey,
                                             axolotlRegistrationId, voice);
  }

  
  public void verifyAccountWithToken(String verificationToken, String openchatingKey, int axolotlRegistrationId, boolean voice)
      throws IOException
  {
    this.pushServiceSocket.verifyAccountToken(verificationToken, openchatingKey, axolotlRegistrationId, voice);
  }

  
  public void setAccountAttributes(String openchatingKey, int axolotlRegistrationId, boolean voice)
      throws IOException
  {
    this.pushServiceSocket.setAccountAttributes(openchatingKey, axolotlRegistrationId, voice);
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

  public String getAccountVerificationToken() throws IOException {
    return this.pushServiceSocket.getAccountVerificationToken();
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
