package com.openchat.imservice.api;

import com.google.protobuf.ByteString;

import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.IdentityKeyPair;
import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.ecc.ECPublicKey;
import com.openchat.protocal.state.PreKeyRecord;
import com.openchat.protocal.state.SignedPreKeyRecord;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.crypto.ProfileCipher;
import com.openchat.imservice.api.crypto.ProfileCipherInputStream;
import com.openchat.imservice.api.crypto.ProfileCipherOutputStream;
import com.openchat.imservice.api.messages.calls.TurnServerInfo;
import com.openchat.imservice.api.messages.multidevice.DeviceInfo;
import com.openchat.imservice.api.push.ContactTokenDetails;
import com.openchat.imservice.api.push.SignedPreKeyEntity;
import com.openchat.imservice.api.util.CredentialsProvider;
import com.openchat.imservice.api.util.StreamDetails;
import com.openchat.imservice.internal.configuration.OpenchatServiceConfiguration;
import com.openchat.imservice.internal.crypto.PaddingInputStream;
import com.openchat.imservice.internal.crypto.ProvisioningCipher;
import com.openchat.imservice.internal.push.ProfileAvatarData;
import com.openchat.imservice.internal.push.PushServiceSocket;
import com.openchat.imservice.internal.configuration.OpenchatServiceUrl;
import com.openchat.imservice.internal.push.http.ProfileCipherOutputStreamFactory;
import com.openchat.imservice.internal.util.Base64;
import com.openchat.imservice.internal.util.StaticCredentialsProvider;
import com.openchat.imservice.internal.util.Util;

import java.io.IOException;
import java.io.InputStream;
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

  
  public OpenchatServiceAccountManager(OpenchatServiceConfiguration configuration,
                                     String user, String password,
                                     String userAgent)
  {
    this(configuration, new StaticCredentialsProvider(user, password, null), userAgent);
  }

  public OpenchatServiceAccountManager(OpenchatServiceConfiguration configuration,
                                     CredentialsProvider credentialsProvider,
                                     String userAgent)
  {
    this.pushServiceSocket = new PushServiceSocket(configuration, credentialsProvider, userAgent);
    this.user              = credentialsProvider.getUser();
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

  
  public void verifyAccountWithCode(String verificationCode, String openchatingKey, int openchatProtocolRegistrationId, boolean fetchesMessages)
      throws IOException
  {
    this.pushServiceSocket.verifyAccountCode(verificationCode, openchatingKey,
                                             openchatProtocolRegistrationId,
                                             fetchesMessages);
  }

  
  public void setAccountAttributes(String openchatingKey, int openchatProtocolRegistrationId, boolean fetchesMessages)
      throws IOException
  {
    this.pushServiceSocket.setAccountAttributes(openchatingKey, openchatProtocolRegistrationId, fetchesMessages);
  }

  
  public void setPreKeys(IdentityKey identityKey, SignedPreKeyRecord signedPreKey, List<PreKeyRecord> oneTimePreKeys)
      throws IOException
  {
    this.pushServiceSocket.registerPreKeys(identityKey, signedPreKey, oneTimePreKeys);
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
                        Optional<byte[]> profileKey,
                        String code)
      throws InvalidKeyException, IOException
  {
    ProvisioningCipher       cipher  = new ProvisioningCipher(deviceKey);
    ProvisionMessage.Builder message = ProvisionMessage.newBuilder()
                                                       .setIdentityKeyPublic(ByteString.copyFrom(identityKeyPair.getPublicKey().serialize()))
                                                       .setIdentityKeyPrivate(ByteString.copyFrom(identityKeyPair.getPrivateKey().serialize()))
                                                       .setNumber(user)
                                                       .setProvisioningCode(code);

    if (profileKey.isPresent()) {
      message.setProfileKey(ByteString.copyFrom(profileKey.get()));
    }

    byte[] ciphertext = cipher.encrypt(message.build());
    this.pushServiceSocket.sendProvisioningMessage(deviceIdentifier, ciphertext);
  }

  public List<DeviceInfo> getDevices() throws IOException {
    return this.pushServiceSocket.getDevices();
  }

  public void removeDevice(long deviceId) throws IOException {
    this.pushServiceSocket.removeDevice(deviceId);
  }

  public TurnServerInfo getTurnServerInfo() throws IOException {
    return this.pushServiceSocket.getTurnServerInfo();
  }

  public void setProfileName(byte[] key, String name)
      throws IOException
  {
    if (name == null) name = "";

    String ciphertextName = Base64.encodeBytesWithoutPadding(new ProfileCipher(key).encryptName(name.getBytes("UTF-8"), ProfileCipher.NAME_PADDED_LENGTH));

    this.pushServiceSocket.setProfileName(ciphertextName);
  }

  public void setProfileAvatar(byte[] key, StreamDetails avatar)
      throws IOException
  {
    ProfileAvatarData profileAvatarData = null;

    if (avatar != null) {
      profileAvatarData = new ProfileAvatarData(avatar.getStream(),
                                                ProfileCipherOutputStream.getCiphertextLength(avatar.getLength()),
                                                avatar.getContentType(),
                                                new ProfileCipherOutputStreamFactory(key));
    }

    this.pushServiceSocket.setProfileAvatar(profileAvatarData);
  }

  public void setSoTimeoutMillis(long soTimeoutMillis) {
    this.pushServiceSocket.setSoTimeoutMillis(soTimeoutMillis);
  }

  public void cancelInFlightRequests() {
    this.pushServiceSocket.cancelInFlightRequests();
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
