package com.openchat.imservice.internal.push;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.http.conn.ssl.StrictHostnameVerifier;
import com.openchat.protocal.IdentityKey;
import com.openchat.protocal.ecc.ECPublicKey;
import com.openchat.protocal.logging.Log;
import com.openchat.protocal.state.PreKeyBundle;
import com.openchat.protocal.state.PreKeyRecord;
import com.openchat.protocal.state.SignedPreKeyRecord;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.crypto.AttachmentCipherOutputStream;
import com.openchat.imservice.api.push.ContactTokenDetails;
import com.openchat.imservice.api.push.OpenchatServiceAddress;
import com.openchat.imservice.api.push.SignedPreKeyEntity;
import com.openchat.imservice.api.push.TrustStore;
import com.openchat.imservice.api.push.exceptions.AuthorizationFailedException;
import com.openchat.imservice.api.push.exceptions.ExpectationFailedException;
import com.openchat.imservice.api.push.exceptions.NonSuccessfulResponseCodeException;
import com.openchat.imservice.api.push.exceptions.NotFoundException;
import com.openchat.imservice.api.push.exceptions.PushNetworkException;
import com.openchat.imservice.api.push.exceptions.RateLimitException;
import com.openchat.imservice.api.push.exceptions.UnregisteredUserException;
import com.openchat.imservice.api.util.CredentialsProvider;
import com.openchat.imservice.internal.push.exceptions.MismatchedDevicesException;
import com.openchat.imservice.internal.push.exceptions.StaleDevicesException;
import com.openchat.imservice.internal.util.Base64;
import com.openchat.imservice.internal.util.BlacklistingTrustManager;
import com.openchat.imservice.internal.util.JsonUtil;
import com.openchat.imservice.internal.util.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class PushServiceSocket {

  private static final String TAG = PushServiceSocket.class.getSimpleName();

  private static final String CREATE_ACCOUNT_SMS_PATH   = "/v1/accounts/sms/code/%s";
  private static final String CREATE_ACCOUNT_VOICE_PATH = "/v1/accounts/voice/code/%s";
  private static final String VERIFY_ACCOUNT_PATH       = "/v1/accounts/code/%s";
  private static final String REGISTER_GCM_PATH         = "/v1/accounts/gcm/";

  private static final String PREKEY_METADATA_PATH      = "/v2/keys/";
  private static final String PREKEY_PATH               = "/v2/keys/%s";
  private static final String PREKEY_DEVICE_PATH        = "/v2/keys/%s/%s";
  private static final String SIGNED_PREKEY_PATH        = "/v2/keys/signed";

  private static final String PROVISIONING_CODE_PATH    = "/v1/devices/provisioning/code";
  private static final String PROVISIONING_MESSAGE_PATH = "/v1/provisioning/%s";

  private static final String DIRECTORY_TOKENS_PATH     = "/v1/directory/tokens";
  private static final String DIRECTORY_VERIFY_PATH     = "/v1/directory/%s";
  private static final String MESSAGE_PATH              = "/v1/messages/%s";
  private static final String RECEIPT_PATH              = "/v1/receipt/%s/%d";
  private static final String ATTACHMENT_PATH           = "/v1/attachments/%s";

  private static final boolean ENFORCE_SSL = true;

  private final String              serviceUrl;
  private final TrustManager[]      trustManagers;
  private final CredentialsProvider credentialsProvider;

  public PushServiceSocket(String serviceUrl, TrustStore trustStore, CredentialsProvider credentialsProvider)
  {
    this.serviceUrl          = serviceUrl;
    this.credentialsProvider = credentialsProvider;
    this.trustManagers       = BlacklistingTrustManager.createFor(trustStore);
  }

  public void createAccount(boolean voice) throws IOException {
    String path = voice ? CREATE_ACCOUNT_VOICE_PATH : CREATE_ACCOUNT_SMS_PATH;
    makeRequest(String.format(path, credentialsProvider.getUser()), "GET", null);
  }

  public void verifyAccount(String verificationCode, String openchatingKey,
                            boolean supportsSms, int registrationId)
      throws IOException
  {
    AccountAttributes openchatingKeyEntity = new AccountAttributes(openchatingKey, supportsSms, registrationId);
    makeRequest(String.format(VERIFY_ACCOUNT_PATH, verificationCode),
                "PUT", JsonUtil.toJson(openchatingKeyEntity));
  }

  public String getNewDeviceVerificationCode() throws IOException {
    String responseText = makeRequest(PROVISIONING_CODE_PATH, "GET", null);
    return JsonUtil.fromJson(responseText, DeviceCode.class).getVerificationCode();
  }

  public void sendProvisioningMessage(String destination, byte[] body) throws IOException {
    makeRequest(String.format(PROVISIONING_MESSAGE_PATH, destination), "PUT",
                JsonUtil.toJson(new ProvisioningMessage(Base64.encodeBytes(body))));
  }

  public void sendReceipt(String destination, long messageId, Optional<String> relay) throws IOException {
    String path = String.format(RECEIPT_PATH, destination, messageId);

    if (relay.isPresent()) {
      path += "?relay=" + relay.get();
    }

    makeRequest(path, "PUT", null);
  }

  public void registerGcmId(String gcmRegistrationId) throws IOException {
    GcmRegistrationId registration = new GcmRegistrationId(gcmRegistrationId, true);
    makeRequest(REGISTER_GCM_PATH, "PUT", JsonUtil.toJson(registration));
  }

  public void unregisterGcmId() throws IOException {
    makeRequest(REGISTER_GCM_PATH, "DELETE", null);
  }

  public SendMessageResponse sendMessage(OutgoingPushMessageList bundle)
      throws IOException
  {
    try {
      String responseText = makeRequest(String.format(MESSAGE_PATH, bundle.getDestination()), "PUT", JsonUtil.toJson(bundle));

      if (responseText == null) return new SendMessageResponse(false);
      else                      return JsonUtil.fromJson(responseText, SendMessageResponse.class);
    } catch (NotFoundException nfe) {
      throw new UnregisteredUserException(bundle.getDestination(), nfe);
    }
  }

  public void registerPreKeys(IdentityKey identityKey,
                              PreKeyRecord lastResortKey,
                              SignedPreKeyRecord signedPreKey,
                              List<PreKeyRecord> records)
      throws IOException
  {
    List<PreKeyEntity> entities = new LinkedList<>();

    for (PreKeyRecord record : records) {
      PreKeyEntity entity = new PreKeyEntity(record.getId(),
                                             record.getKeyPair().getPublicKey());

      entities.add(entity);
    }

    PreKeyEntity lastResortEntity = new PreKeyEntity(lastResortKey.getId(),
                                                     lastResortKey.getKeyPair().getPublicKey());

    SignedPreKeyEntity signedPreKeyEntity = new SignedPreKeyEntity(signedPreKey.getId(),
                                                                   signedPreKey.getKeyPair().getPublicKey(),
                                                                   signedPreKey.getSignature());

    makeRequest(String.format(PREKEY_PATH, ""), "PUT",
                JsonUtil.toJson(new PreKeyState(entities, lastResortEntity,
                                                signedPreKeyEntity, identityKey)));
  }

  public int getAvailablePreKeys() throws IOException {
    String       responseText = makeRequest(PREKEY_METADATA_PATH, "GET", null);
    PreKeyStatus preKeyStatus = JsonUtil.fromJson(responseText, PreKeyStatus.class);

    return preKeyStatus.getCount();
  }

  public List<PreKeyBundle> getPreKeys(OpenchatServiceAddress destination, int deviceIdInteger) throws IOException {
    try {
      String deviceId = String.valueOf(deviceIdInteger);

      if (deviceId.equals("1"))
        deviceId = "*";

      String path = String.format(PREKEY_DEVICE_PATH, destination.getNumber(), deviceId);

      if (destination.getRelay().isPresent()) {
        path = path + "?relay=" + destination.getRelay().get();
      }

      String             responseText = makeRequest(path, "GET", null);
      PreKeyResponse     response     = JsonUtil.fromJson(responseText, PreKeyResponse.class);
      List<PreKeyBundle> bundles      = new LinkedList<>();

      for (PreKeyResponseItem device : response.getDevices()) {
        ECPublicKey preKey                = null;
        ECPublicKey signedPreKey          = null;
        byte[]      signedPreKeySignature = null;
        int         preKeyId              = -1;
        int         signedPreKeyId        = -1;

        if (device.getSignedPreKey() != null) {
          signedPreKey          = device.getSignedPreKey().getPublicKey();
          signedPreKeyId        = device.getSignedPreKey().getKeyId();
          signedPreKeySignature = device.getSignedPreKey().getSignature();
        }

        if (device.getPreKey() != null) {
          preKeyId = device.getPreKey().getKeyId();
          preKey   = device.getPreKey().getPublicKey();
        }

        bundles.add(new PreKeyBundle(device.getRegistrationId(), device.getDeviceId(), preKeyId,
                                     preKey, signedPreKeyId, signedPreKey, signedPreKeySignature,
                                     response.getIdentityKey()));
      }

      return bundles;
    } catch (JsonUtil.JsonParseException e) {
      throw new IOException(e);
    } catch (NotFoundException nfe) {
      throw new UnregisteredUserException(destination.getNumber(), nfe);
    }
  }

  public PreKeyBundle getPreKey(OpenchatServiceAddress destination, int deviceId) throws IOException {
    try {
      String path = String.format(PREKEY_DEVICE_PATH, destination.getNumber(),
                                  String.valueOf(deviceId));

      if (destination.getRelay().isPresent()) {
        path = path + "?relay=" + destination.getRelay().get();
      }

      String         responseText = makeRequest(path, "GET", null);
      PreKeyResponse response     = JsonUtil.fromJson(responseText, PreKeyResponse.class);

      if (response.getDevices() == null || response.getDevices().size() < 1)
        throw new IOException("Empty prekey list");

      PreKeyResponseItem device                = response.getDevices().get(0);
      ECPublicKey        preKey                = null;
      ECPublicKey        signedPreKey          = null;
      byte[]             signedPreKeySignature = null;
      int                preKeyId              = -1;
      int                signedPreKeyId        = -1;

      if (device.getPreKey() != null) {
        preKeyId = device.getPreKey().getKeyId();
        preKey   = device.getPreKey().getPublicKey();
      }

      if (device.getSignedPreKey() != null) {
        signedPreKeyId        = device.getSignedPreKey().getKeyId();
        signedPreKey          = device.getSignedPreKey().getPublicKey();
        signedPreKeySignature = device.getSignedPreKey().getSignature();
      }

      return new PreKeyBundle(device.getRegistrationId(), device.getDeviceId(), preKeyId, preKey,
                              signedPreKeyId, signedPreKey, signedPreKeySignature, response.getIdentityKey());
    } catch (JsonUtil.JsonParseException e) {
      throw new IOException(e);
    } catch (NotFoundException nfe) {
      throw new UnregisteredUserException(destination.getNumber(), nfe);
    }
  }

  public SignedPreKeyEntity getCurrentSignedPreKey() throws IOException {
    try {
      String responseText = makeRequest(SIGNED_PREKEY_PATH, "GET", null);
      return JsonUtil.fromJson(responseText, SignedPreKeyEntity.class);
    } catch (NotFoundException e) {
      Log.w(TAG, e);
      return null;
    }
  }

  public void setCurrentSignedPreKey(SignedPreKeyRecord signedPreKey) throws IOException {
    SignedPreKeyEntity signedPreKeyEntity = new SignedPreKeyEntity(signedPreKey.getId(),
                                                                   signedPreKey.getKeyPair().getPublicKey(),
                                                                   signedPreKey.getSignature());
    makeRequest(SIGNED_PREKEY_PATH, "PUT", JsonUtil.toJson(signedPreKeyEntity));
  }

  public long sendAttachment(PushAttachmentData attachment) throws IOException {
    String               response      = makeRequest(String.format(ATTACHMENT_PATH, ""), "GET", null);
    AttachmentDescriptor attachmentKey = JsonUtil.fromJson(response, AttachmentDescriptor.class);

    if (attachmentKey == null || attachmentKey.getLocation() == null) {
      throw new IOException("Server failed to allocate an attachment key!");
    }

    Log.w(TAG, "Got attachment content location: " + attachmentKey.getLocation());

    uploadAttachment("PUT", attachmentKey.getLocation(), attachment.getData(),
                     attachment.getDataSize(), attachment.getKey());

    return attachmentKey.getId();
  }

  public void retrieveAttachment(String relay, long attachmentId, File destination) throws IOException {
    String path = String.format(ATTACHMENT_PATH, String.valueOf(attachmentId));

    if (!Util.isEmpty(relay)) {
      path = path + "?relay=" + relay;
    }

    String               response   = makeRequest(path, "GET", null);
    AttachmentDescriptor descriptor = JsonUtil.fromJson(response, AttachmentDescriptor.class);

    Log.w(TAG, "Attachment: " + attachmentId + " is at: " + descriptor.getLocation());

    downloadExternalFile(descriptor.getLocation(), destination);
  }

  public List<ContactTokenDetails> retrieveDirectory(Set<String> contactTokens)
      throws NonSuccessfulResponseCodeException, PushNetworkException
  {
    ContactTokenList        contactTokenList = new ContactTokenList(new LinkedList<>(contactTokens));
    String                  response         = makeRequest(DIRECTORY_TOKENS_PATH, "PUT", JsonUtil.toJson(contactTokenList));
    ContactTokenDetailsList activeTokens     = JsonUtil.fromJson(response, ContactTokenDetailsList.class);

    return activeTokens.getContacts();
  }

  public ContactTokenDetails getContactTokenDetails(String contactToken) throws IOException {
    try {
      String response = makeRequest(String.format(DIRECTORY_VERIFY_PATH, contactToken), "GET", null);
      return JsonUtil.fromJson(response, ContactTokenDetails.class);
    } catch (NotFoundException nfe) {
      return null;
    }
  }

  private void downloadExternalFile(String url, File localDestination)
      throws IOException
  {
    URL               downloadUrl = new URL(url);
    HttpURLConnection connection  = (HttpURLConnection) downloadUrl.openConnection();
    connection.setRequestProperty("Content-Type", "application/octet-stream");
    connection.setRequestMethod("GET");
    connection.setDoInput(true);

    try {
      if (connection.getResponseCode() != 200) {
        throw new NonSuccessfulResponseCodeException("Bad response: " + connection.getResponseCode());
      }

      OutputStream output = new FileOutputStream(localDestination);
      InputStream input   = connection.getInputStream();
      byte[] buffer       = new byte[4096];
      int read;

      while ((read = input.read(buffer)) != -1) {
        output.write(buffer, 0, read);
      }

      output.close();
      Log.w(TAG, "Downloaded: " + url + " to: " + localDestination.getAbsolutePath());
    } catch (IOException ioe) {
      throw new PushNetworkException(ioe);
    } finally {
      connection.disconnect();
    }
  }

  private void uploadAttachment(String method, String url, InputStream data, long dataSize, byte[] key)
    throws IOException
  {
    URL                uploadUrl  = new URL(url);
    HttpsURLConnection connection = (HttpsURLConnection) uploadUrl.openConnection();
    connection.setDoOutput(true);

    if (dataSize > 0) {
      connection.setFixedLengthStreamingMode((int) AttachmentCipherOutputStream.getCiphertextLength(dataSize));
    } else {
      connection.setChunkedStreamingMode(0);
    }

    connection.setRequestMethod(method);
    connection.setRequestProperty("Content-Type", "application/octet-stream");
    connection.setRequestProperty("Connection", "close");
    connection.connect();

    try {
      OutputStream                 stream = connection.getOutputStream();
      AttachmentCipherOutputStream out    = new AttachmentCipherOutputStream(key, stream);

      Util.copy(data, out);
      out.flush();

      if (connection.getResponseCode() != 200) {
        throw new IOException("Bad response: " + connection.getResponseCode() + " " + connection.getResponseMessage());
      }
    } finally {
      connection.disconnect();
    }
  }

  private String makeRequest(String urlFragment, String method, String body)
      throws NonSuccessfulResponseCodeException, PushNetworkException
  {
    HttpURLConnection connection = makeBaseRequest(urlFragment, method, body);

    try {
      String response = Util.readFully(connection.getInputStream());
      connection.disconnect();

      return response;
    } catch (IOException ioe) {
      throw new PushNetworkException(ioe);
    }
  }

  private HttpURLConnection makeBaseRequest(String urlFragment, String method, String body)
      throws NonSuccessfulResponseCodeException, PushNetworkException
  {
    HttpURLConnection connection = getConnection(urlFragment, method, body);
    int               responseCode;
    String            responseMessage;
    String            response;

    try {
      responseCode    = connection.getResponseCode();
      responseMessage = connection.getResponseMessage();
    } catch (IOException ioe) {
      throw new PushNetworkException(ioe);
    }

    switch (responseCode) {
      case 413:
        connection.disconnect();
        throw new RateLimitException("Rate limit exceeded: " + responseCode);
      case 401:
      case 403:
        connection.disconnect();
        throw new AuthorizationFailedException("Authorization failed!");
      case 404:
        connection.disconnect();
        throw new NotFoundException("Not found");
      case 409:
        try {
          response = Util.readFully(connection.getErrorStream());
        } catch (IOException e) {
          throw new PushNetworkException(e);
        }
        throw new MismatchedDevicesException(JsonUtil.fromJson(response, MismatchedDevices.class));
      case 410:
        try {
          response = Util.readFully(connection.getErrorStream());
        } catch (IOException e) {
          throw new PushNetworkException(e);
        }
        throw new StaleDevicesException(JsonUtil.fromJson(response, StaleDevices.class));
      case 417:
        throw new ExpectationFailedException();
    }

    if (responseCode != 200 && responseCode != 204) {
        throw new NonSuccessfulResponseCodeException("Bad response: " + responseCode + " " +
                                                     responseMessage);
    }

    return connection;
  }

  private HttpURLConnection getConnection(String urlFragment, String method, String body)
      throws PushNetworkException
  {
    try {
      SSLContext context = SSLContext.getInstance("TLS");
      context.init(null, trustManagers, null);

      URL url = new URL(String.format("%s%s", serviceUrl, urlFragment));
      Log.w(TAG, "Push service URL: " + serviceUrl);
      Log.w(TAG, "Opening URL: " + url);

      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      if (ENFORCE_SSL) {
        ((HttpsURLConnection) connection).setSSLSocketFactory(context.getSocketFactory());
        ((HttpsURLConnection) connection).setHostnameVerifier(new StrictHostnameVerifier());
      }

      connection.setRequestMethod(method);
      connection.setRequestProperty("Content-Type", "application/json");

      if (credentialsProvider.getPassword() != null) {
        connection.setRequestProperty("Authorization", getAuthorizationHeader());
      }

      if (body != null) {
        connection.setDoOutput(true);
      }

      connection.connect();

      if (body != null) {
        Log.w(TAG, method + "  --  " + body);
        OutputStream out = connection.getOutputStream();
        out.write(body.getBytes());
        out.close();
      }

      return connection;
    } catch (IOException e) {
      throw new PushNetworkException(e);
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      throw new AssertionError(e);
    }
  }

  private String getAuthorizationHeader() {
    try {
      return "Basic " + Base64.encodeBytes((credentialsProvider.getUser() + ":" + credentialsProvider.getPassword()).getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError(e);
    }
  }

  private static class GcmRegistrationId {

    @JsonProperty
    private String gcmRegistrationId;

    @JsonProperty
    private boolean webSocketChannel;

    public GcmRegistrationId() {}

    public GcmRegistrationId(String gcmRegistrationId, boolean webSocketChannel) {
      this.gcmRegistrationId = gcmRegistrationId;
      this.webSocketChannel  = webSocketChannel;
    }
  }

  private static class AttachmentDescriptor {
    @JsonProperty
    private long id;

    @JsonProperty
    private String location;

    public long getId() {
      return id;
    }

    public String getLocation() {
      return location;
    }
  }
}
