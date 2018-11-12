package com.openchat.imservice.api.messages.multidevice;

import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.messages.OpenchatServiceAttachment;
import com.openchat.imservice.api.messages.OpenchatServiceGroup;

public class OpenchatServiceSyncMessage {

  private final Optional<SentTranscriptMessage> sent;
  private final Optional<OpenchatServiceAttachment>  contacts;
  private final Optional<OpenchatServiceAttachment>  groups;
  private final Optional<RequestMessage>        request;
  private final Optional<ReadMessage>           read;

  private OpenchatServiceSyncMessage(Optional<SentTranscriptMessage> sent,
                                Optional<OpenchatServiceAttachment>  contacts,
                                Optional<OpenchatServiceAttachment>  groups,
                                Optional<RequestMessage>        request,
                                Optional<ReadMessage>           read)
  {
    this.sent     = sent;
    this.contacts = contacts;
    this.groups   = groups;
    this.request  = request;
    this.read     = read;
  }

  public static OpenchatServiceSyncMessage forSentTranscript(SentTranscriptMessage sent) {
    return new OpenchatServiceSyncMessage(Optional.of(sent),
                                     Optional.<OpenchatServiceAttachment>absent(),
                                     Optional.<OpenchatServiceAttachment>absent(),
                                     Optional.<RequestMessage>absent(),
                                     Optional.<ReadMessage>absent());
  }

  public static OpenchatServiceSyncMessage forContacts(OpenchatServiceAttachment contacts) {
    return new OpenchatServiceSyncMessage(Optional.<SentTranscriptMessage>absent(),
                                     Optional.of(contacts),
                                     Optional.<OpenchatServiceAttachment>absent(),
                                     Optional.<RequestMessage>absent(),
                                     Optional.<ReadMessage>absent());
  }

  public static OpenchatServiceSyncMessage forGroups(OpenchatServiceAttachment groups) {
    return new OpenchatServiceSyncMessage(Optional.<SentTranscriptMessage>absent(),
                                     Optional.<OpenchatServiceAttachment>absent(),
                                     Optional.of(groups),
                                     Optional.<RequestMessage>absent(),
                                     Optional.<ReadMessage>absent());
  }

  public static OpenchatServiceSyncMessage forRequest(RequestMessage request) {
    return new OpenchatServiceSyncMessage(Optional.<SentTranscriptMessage>absent(),
                                     Optional.<OpenchatServiceAttachment>absent(),
                                     Optional.<OpenchatServiceAttachment>absent(),
                                     Optional.of(request),
                                     Optional.<ReadMessage>absent());
  }

  public static OpenchatServiceSyncMessage forRead(ReadMessage read) {
    return new OpenchatServiceSyncMessage(Optional.<SentTranscriptMessage>absent(),
                                     Optional.<OpenchatServiceAttachment>absent(),
                                     Optional.<OpenchatServiceAttachment>absent(),
                                     Optional.<RequestMessage>absent(),
                                     Optional.of(read));
  }

  public static OpenchatServiceSyncMessage empty() {
    return new OpenchatServiceSyncMessage(Optional.<SentTranscriptMessage>absent(),
                                     Optional.<OpenchatServiceAttachment>absent(),
                                     Optional.<OpenchatServiceAttachment>absent(),
                                     Optional.<RequestMessage>absent(),
                                     Optional.<ReadMessage>absent());
  }

  public Optional<SentTranscriptMessage> getSent() {
    return sent;
  }

  public Optional<OpenchatServiceAttachment> getGroups() {
    return groups;
  }

  public Optional<OpenchatServiceAttachment> getContacts() {
    return contacts;
  }

  public Optional<RequestMessage> getRequest() {
    return request;
  }

  public Optional<ReadMessage> getRead() {
    return read;
  }

}
