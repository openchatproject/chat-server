package com.openchat.imservice.api;

import com.openchat.protocal.InvalidMessageException;
import com.openchat.imservice.api.crypto.AttachmentCipherInputStream;
import com.openchat.imservice.api.messages.OpenchatServiceAttachment;
import com.openchat.imservice.api.messages.OpenchatServiceAttachment.ProgressListener;
import com.openchat.imservice.api.messages.OpenchatServiceAttachmentPointer;
import com.openchat.imservice.api.messages.OpenchatServiceDataMessage;
import com.openchat.imservice.api.messages.OpenchatServiceEnvelope;
import com.openchat.imservice.api.push.TrustStore;
import com.openchat.imservice.api.util.CredentialsProvider;
import com.openchat.imservice.internal.push.PushServiceSocket;
import com.openchat.imservice.internal.push.OpenchatServiceEnvelopeEntity;
import com.openchat.imservice.internal.util.StaticCredentialsProvider;
import com.openchat.imservice.internal.websocket.WebSocketConnection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class OpenchatServiceMessageReceiver {

  private final PushServiceSocket   socket;
  private final TrustStore          trustStore;
  private final String              url;
  private final CredentialsProvider credentialsProvider;

  
  public OpenchatServiceMessageReceiver(String url, TrustStore trustStore,
                                   String user, String password, String openchatingKey)
  {
    this(url, trustStore, new StaticCredentialsProvider(user, password, openchatingKey));
  }

  
  public OpenchatServiceMessageReceiver(String url, TrustStore trustStore, CredentialsProvider credentials) {
    this.url                 = url;
    this.trustStore          = trustStore;
    this.credentialsProvider = credentials;
    this.socket              = new PushServiceSocket(url, trustStore, credentials);
  }

  
  public InputStream retrieveAttachment(OpenchatServiceAttachmentPointer pointer, File destination, ProgressListener listener)
      throws IOException, InvalidMessageException
  {
    socket.retrieveAttachment(pointer.getRelay().orNull(), pointer.getId(), destination, listener);
    return new AttachmentCipherInputStream(destination, pointer.getKey());
  }

  
  public OpenchatServiceMessagePipe createMessagePipe() {
    WebSocketConnection webSocket = new WebSocketConnection(url, trustStore, credentialsProvider);
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
