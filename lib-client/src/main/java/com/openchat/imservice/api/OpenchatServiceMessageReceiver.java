package com.openchat.imservice.api;

import com.openchat.protocal.InvalidMessageException;
import com.openchat.imservice.api.crypto.AttachmentCipherInputStream;
import com.openchat.imservice.api.messages.OpenchatServiceAttachment.ProgressListener;
import com.openchat.imservice.api.messages.OpenchatServiceAttachmentPointer;
import com.openchat.imservice.api.messages.OpenchatServiceDataMessage;
import com.openchat.imservice.api.messages.OpenchatServiceEnvelope;
import com.openchat.imservice.api.push.OpenchatServiceAddress;
import com.openchat.imservice.api.push.OpenchatServiceProfile;
import com.openchat.imservice.api.util.CredentialsProvider;
import com.openchat.imservice.internal.push.PushServiceSocket;
import com.openchat.imservice.internal.push.OpenchatServiceEnvelopeEntity;
import com.openchat.imservice.internal.push.OpenchatServiceUrl;
import com.openchat.imservice.internal.util.StaticCredentialsProvider;
import com.openchat.imservice.internal.websocket.WebSocketConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class OpenchatServiceMessageReceiver {

  private final PushServiceSocket   socket;
  private final OpenchatServiceUrl[]  urls;
  private final CredentialsProvider credentialsProvider;
  private final String              userAgent;

  
  public OpenchatServiceMessageReceiver(OpenchatServiceUrl[] urls,
                                      String user, String password,
                                      String openchatingKey, String userAgent)
  {
    this(urls, new StaticCredentialsProvider(user, password, openchatingKey), userAgent);
  }

  
  public OpenchatServiceMessageReceiver(OpenchatServiceUrl[] urls, CredentialsProvider credentials, String userAgent)
  {
    this.urls                 = urls;
    this.credentialsProvider = credentials;
    this.socket              = new PushServiceSocket(urls, credentials, userAgent);
    this.userAgent           = userAgent;
  }

  
  public InputStream retrieveAttachment(OpenchatServiceAttachmentPointer pointer, File destination, int maxSizeBytes)
      throws IOException, InvalidMessageException
  {
    return retrieveAttachment(pointer, destination, maxSizeBytes, null);
  }

  public OpenchatServiceProfile retrieveProfile(OpenchatServiceAddress address)
    throws IOException
  {
    return socket.retrieveProfile(address);
  }

  
  public InputStream retrieveAttachment(OpenchatServiceAttachmentPointer pointer, File destination, int maxSizeBytes, ProgressListener listener)
      throws IOException, InvalidMessageException
  {
    socket.retrieveAttachment(pointer.getRelay().orNull(), pointer.getId(), destination, maxSizeBytes, listener);
    return new AttachmentCipherInputStream(destination, pointer.getKey(), pointer.getDigest());
  }

  
  public OpenchatServiceMessagePipe createMessagePipe() {
    WebSocketConnection webSocket = new WebSocketConnection(urls[0].getUrl(), urls[0].getTrustStore(), credentialsProvider, userAgent);
    return new OpenchatServiceMessagePipe(webSocket, credentialsProvider);
  }

  public List<OpenchatServiceEnvelope> retrieveMessages() throws IOException {
    return retrieveMessages(new NullMessageReceivedCallback());
  }

  public List<OpenchatServiceEnvelope> retrieveMessages(MessageReceivedCallback callback)
      throws IOException
  {
    List<OpenchatServiceEnvelope>       results  = new LinkedList<>();
    List<OpenchatServiceEnvelopeEntity> entities = socket.getMessages();

    for (OpenchatServiceEnvelopeEntity entity : entities) {
      OpenchatServiceEnvelope envelope =  new OpenchatServiceEnvelope(entity.getType(), entity.getSource(),
                                                                  entity.getSourceDevice(), entity.getRelay(),
                                                                  entity.getTimestamp(), entity.getMessage(),
                                                                  entity.getContent());

      callback.onMessage(envelope);
      results.add(envelope);

      socket.acknowledgeMessage(entity.getSource(), entity.getTimestamp());
    }

    return results;
  }

  public interface MessageReceivedCallback {
    public void onMessage(OpenchatServiceEnvelope envelope);
  }

  public static class NullMessageReceivedCallback implements MessageReceivedCallback {
    @Override
    public void onMessage(OpenchatServiceEnvelope envelope) {}
  }

}
