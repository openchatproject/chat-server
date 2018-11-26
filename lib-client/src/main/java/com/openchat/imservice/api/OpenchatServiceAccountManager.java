package com.openchat.imservice.api;

import com.google.protobuf.ByteString;

import org.spongycastle.crypto.InvalidCipherTextException;
import com.openchat.curve25519.Curve25519;
import com.openchat.curve25519.Curve25519KeyPair;
import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.IdentityKeyPair;
import com.openchat.protocal.InvalidKeyException;
import com.openchat.protocal.ecc.ECPublicKey;
import com.openchat.protocal.logging.Log;
import com.openchat.protocal.state.PreKeyRecord;
import com.openchat.protocal.state.SignedPreKeyRecord;
import com.openchat.protocal.util.Pair;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.crypto.ProfileCipher;
import com.openchat.imservice.api.crypto.ProfileCipherOutputStream;
import com.openchat.imservice.api.messages.calls.TurnServerInfo;
import com.openchat.imservice.api.messages.multidevice.DeviceInfo;
import com.openchat.imservice.api.push.ContactTokenDetails;
import com.openchat.imservice.api.push.SignedPreKeyEntity;
import com.openchat.imservice.api.util.CredentialsProvider;
import com.openchat.imservice.api.util.StreamDetails;
import com.openchat.imservice.internal.configuration.OpenchatServiceConfiguration;
import com.openchat.imservice.internal.contacts.crypto.ContactDiscoveryCipher;
import com.openchat.imservice.internal.contacts.crypto.Quote;
import com.openchat.imservice.internal.contacts.crypto.RemoteAttestation;
import com.openchat.imservice.internal.contacts.crypto.RemoteAttestationKeys;
import com.openchat.imservice.internal.contacts.crypto.UnauthenticatedQuoteException;
import com.openchat.imservice.internal.contacts.crypto.UnauthenticatedResponseException;
import com.openchat.imservice.internal.contacts.entities.DiscoveryRequest;
import com.openchat.imservice.internal.contacts.entities.DiscoveryResponse;
import com.openchat.imservice.internal.contacts.entities.RemoteAttestationRequest;
import com.openchat.imservice.internal.contacts.entities.RemoteAttestationResponse;
import com.openchat.imservice.internal.crypto.ProvisioningCipher;
import com.openchat.imservice.internal.push.ProfileAvatarData;
import com.openchat.imservice.internal.push.PushServiceSocket;
import com.openchat.imservice.internal.push.http.ProfileCipherOutputStreamFactory;
import com.openchat.imservice.internal.util.Base64;
import com.openchat.imservice.internal.util.StaticCredentialsProvider;
import com.openchat.imservice.internal.util.Util;

import java.io.IOException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.openchat.imservice.internal.push.ProvisioningProtos.ProvisionMessage;

public class OpenchatServiceAccountManager {

  private static final String TAG = OpenchatServiceAccountManager.class.getSimpleName();

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

  public void setPin(Optional<String> pin) throws IOException {
    if (pin.isPresent()) {
      this.pushServiceSocket.setPin(pin.get());
    } else {
      this.pushServiceSocket.removePin();
    }
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

  
  public void verifyAccountWithCode(String verificationCode, String openchatingKey, int openchatProtocolRegistrationId, boolean fetchesMessages, String pin)
      throws IOException
  {
    this.pushServiceSocket.verifyAccountCode(verificationCode, openchatingKey,
                                             openchatProtocolRegistrationId,
                                             fetchesMessages, pin);
  }

  
  public void setAccountAttributes(String openchatingKey, int openchatProtocolRegistrationId, boolean fetchesMessages, String pin)
      throws IOException
  {
    this.pushServiceSocket.setAccountAttributes(openchatingKey, openchatProtocolRegistrationId, fetchesMessages, pin);
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

  public List<String> getRegisteredUsers(KeyStore iasKeyStore, Set<String> e164numbers, String mrenclave)
      throws IOException, Quote.InvalidQuoteFormatException, UnauthenticatedQuoteException, SignatureException, UnauthenticatedResponseException
  {
    try {
      String            authorization = this.pushServiceSocket.getContactDiscoveryAuthorization();
      Curve25519        curve         = Curve25519.getInstance(Curve25519.BEST);
      Curve25519KeyPair keyPair       = curve.generateKeyPair();

      ContactDiscoveryCipher                        cipher              = new ContactDiscoveryCipher();
      RemoteAttestationRequest                      attestationRequest  = new RemoteAttestationRequest(keyPair.getPublicKey());
      Pair<RemoteAttestationResponse, List<String>> attestationResponse = this.pushServiceSocket.getContactDiscoveryRemoteAttestation(authorization, attestationRequest, mrenclave);

      RemoteAttestationKeys keys      = new RemoteAttestationKeys(keyPair, attestationResponse.first().getServerEphemeralPublic(), attestationResponse.first().getServerStaticPublic());
      Quote                 quote     = new Quote(attestationResponse.first().getQuote());
      byte[]                requestId = cipher.getRequestId(keys, attestationResponse.first());

      cipher.verifyServerQuote(quote, attestationResponse.first().getServerStaticPublic(), mrenclave);
      cipher.verifyIasSignature(iasKeyStore, attestationResponse.first().getCertificates(), attestationResponse.first().getSignatureBody(), attestationResponse.first().getSignature(), quote);

      RemoteAttestation remoteAttestation = new RemoteAttestation(requestId, keys);
      List<String>      addressBook       = new LinkedList<>();

      for (String e164number : e164numbers) {
        addressBook.add(e164number.substring(1));
      }

      DiscoveryRequest  request  = cipher.createDiscoveryRequest(addressBook, remoteAttestation);
      DiscoveryResponse response = this.pushServiceSocket.getContactDiscoveryRegisteredUsers(authorization, request, attestationResponse.second(), mrenclave);
      byte[]            data     = cipher.getDiscoveryResponseData(response, remoteAttestation);

      Iterator<String> addressBookIterator = addressBook.iterator();
      List<String>     results             = new LinkedList<>();

      for (byte aData : data) {
        String candidate = addressBookIterator.next();

        if (aData != 0) results.add('+' + candidate);
      }

      return results;
    } catch (InvalidCipherTextException e) {
      throw new UnauthenticatedResponseException(e);
    }
  }

  public void reportContactDiscoveryServiceMatch() {
    try {
      this.pushServiceSocket.reportContactDiscoveryServiceMatch();
    } catch (IOException e) {
      Log.w(TAG, "Request to indicate a contact discovery result match failed. Ignoring.", e);
    }
  }

  public void reportContactDiscoveryServiceMismatch() {
    try {
      this.pushServiceSocket.reportContactDiscoveryServiceMismatch();
    } catch (IOException e) {
      Log.w(TAG, "Request to indicate a contact discovery result mismatch failed. Ignoring.", e);
    }
  }

  public void reportContactDiscoveryServiceAttestationError() {
    try {
      this.pushServiceSocket.reportContactDiscoveryServiceAttestationError();
    } catch (IOException e) {
      Log.w(TAG, "Request to indicate a contact discovery attestation error failed. Ignoring.", e);
    }
  }

  public void reportContactDiscoveryServiceUnexpectedError() {
    try {
      this.pushServiceSocket.reportContactDiscoveryServiceUnexpectedError();
    } catch (IOException e) {
      Log.w(TAG, "Request to indicate a contact discovery unexpected error failed. Ignoring.", e);
    }
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
