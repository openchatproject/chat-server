package com.openchat.imservice.api.messages.multidevice;

import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.messages.OpenchatServiceAttachment;

import java.util.LinkedList;
import java.util.List;

public class OpenchatServiceSyncMessage {

  private final Optional<SentTranscriptMessage>   sent;
  private final Optional<ContactsMessage>         contacts;
  private final Optional<OpenchatServiceAttachment> groups;
  private final Optional<BlockedListMessage>      blockedList;
  private final Optional<RequestMessage>          request;
  private final Optional<List<ReadMessage>>       reads;
  private final Optional<List<VerifiedMessage>>   verified;

  private OpenchatServiceSyncMessage(Optional<SentTranscriptMessage>   sent,
                                   Optional<ContactsMessage>         contacts,
                                   Optional<OpenchatServiceAttachment> groups,
                                   Optional<BlockedListMessage>      blockedList,
                                   Optional<RequestMessage>          request,
                                   Optional<List<ReadMessage>>       reads,
                                   Optional<List<VerifiedMessage>>   verified)
  {
    this.sent        = sent;
    this.contacts    = contacts;
    this.groups      = groups;
    this.blockedList = blockedList;
    this.request     = request;
    this.reads       = reads;
    this.verified    = verified;
  }

  public static OpenchatServiceSyncMessage forSentTranscript(SentTranscriptMessage sent) {
    return new OpenchatServiceSyncMessage(Optional.of(sent),
                                        Optional.<ContactsMessage>absent(),
                                        Optional.<OpenchatServiceAttachment>absent(),
                                        Optional.<BlockedListMessage>absent(),
                                        Optional.<RequestMessage>absent(),
                                        Optional.<List<ReadMessage>>absent(),
                                        Optional.<List<VerifiedMessage>>absent());
  }

  public static OpenchatServiceSyncMessage forContacts(ContactsMessage contacts) {
    return new OpenchatServiceSyncMessage(Optional.<SentTranscriptMessage>absent(),
                                        Optional.of(contacts),
                                        Optional.<OpenchatServiceAttachment>absent(),
                                        Optional.<BlockedListMessage>absent(),
                                        Optional.<RequestMessage>absent(),
                                        Optional.<List<ReadMessage>>absent(),
                                        Optional.<List<VerifiedMessage>>absent());
  }

  public static OpenchatServiceSyncMessage forGroups(OpenchatServiceAttachment groups) {
    return new OpenchatServiceSyncMessage(Optional.<SentTranscriptMessage>absent(),
                                        Optional.<ContactsMessage>absent(),
                                        Optional.of(groups),
                                        Optional.<BlockedListMessage>absent(),
                                        Optional.<RequestMessage>absent(),
                                        Optional.<List<ReadMessage>>absent(),
                                        Optional.<List<VerifiedMessage>>absent());
  }

  public static OpenchatServiceSyncMessage forRequest(RequestMessage request) {
    return new OpenchatServiceSyncMessage(Optional.<SentTranscriptMessage>absent(),
                                        Optional.<ContactsMessage>absent(),
                                        Optional.<OpenchatServiceAttachment>absent(),
                                        Optional.<BlockedListMessage>absent(),
                                        Optional.of(request),
                                        Optional.<List<ReadMessage>>absent(),
                                        Optional.<List<VerifiedMessage>>absent());
  }

  public static OpenchatServiceSyncMessage forRead(List<ReadMessage> reads) {
    return new OpenchatServiceSyncMessage(Optional.<SentTranscriptMessage>absent(),
                                        Optional.<ContactsMessage>absent(),
                                        Optional.<OpenchatServiceAttachment>absent(),
                                        Optional.<BlockedListMessage>absent(),
                                        Optional.<RequestMessage>absent(),
                                        Optional.of(reads),
                                        Optional.<List<VerifiedMessage>>absent());
  }

  public static OpenchatServiceSyncMessage forRead(ReadMessage read) {
    List<ReadMessage> reads = new LinkedList<>();
    reads.add(read);

    return new OpenchatServiceSyncMessage(Optional.<SentTranscriptMessage>absent(),
                                        Optional.<ContactsMessage>absent(),
                                        Optional.<OpenchatServiceAttachment>absent(),
                                        Optional.<BlockedListMessage>absent(),
                                        Optional.<RequestMessage>absent(),
                                        Optional.of(reads),
                                        Optional.<List<VerifiedMessage>>absent());
  }

  public static OpenchatServiceSyncMessage forVerified(List<VerifiedMessage> verified) {
    return new OpenchatServiceSyncMessage(Optional.<SentTranscriptMessage>absent(),
                                        Optional.<ContactsMessage>absent(),
                                        Optional.<OpenchatServiceAttachment>absent(),
                                        Optional.<BlockedListMessage>absent(),
                                        Optional.<RequestMessage>absent(),
                                        Optional.<List<ReadMessage>>absent(),
                                        Optional.of(verified));
  }

  public static OpenchatServiceSyncMessage forVerified(VerifiedMessage verifiedMessage) {
    List<VerifiedMessage> verifiedMessages = new LinkedList<>();
    verifiedMessages.add(verifiedMessage);

    return new OpenchatServiceSyncMessage(Optional.<SentTranscriptMessage>absent(),
                                        Optional.<ContactsMessage>absent(),
                                        Optional.<OpenchatServiceAttachment>absent(),
                                        Optional.<BlockedListMessage>absent(),
                                        Optional.<RequestMessage>absent(),
                                        Optional.<List<ReadMessage>>absent(),
                                        Optional.of(verifiedMessages));
  }

  public static OpenchatServiceSyncMessage forBlocked(BlockedListMessage blocked) {
    return new OpenchatServiceSyncMessage(Optional.<SentTranscriptMessage>absent(),
                                        Optional.<ContactsMessage>absent(),
                                        Optional.<OpenchatServiceAttachment>absent(),
                                        Optional.of(blocked),
                                        Optional.<RequestMessage>absent(),
                                        Optional.<List<ReadMessage>>absent(),
                                        Optional.<List<VerifiedMessage>>absent());
  }

  public static OpenchatServiceSyncMessage empty() {
    return new OpenchatServiceSyncMessage(Optional.<SentTranscriptMessage>absent(),
                                        Optional.<ContactsMessage>absent(),
                                        Optional.<OpenchatServiceAttachment>absent(),
                                        Optional.<BlockedListMessage>absent(),
                                        Optional.<RequestMessage>absent(),
                                        Optional.<List<ReadMessage>>absent(),
                                        Optional.<List<VerifiedMessage>>absent());
  }

  public Optional<SentTranscriptMessage> getSent() {
    return sent;
  }

  public Optional<OpenchatServiceAttachment> getGroups() {
    return groups;
  }

  public Optional<ContactsMessage> getContacts() {
    return contacts;
  }

  public Optional<RequestMessage> getRequest() {
    return request;
  }

  public Optional<List<ReadMessage>> getRead() {
    return reads;
  }

  public Optional<BlockedListMessage> getBlockedList() {
    return blockedList;
  }

  public Optional<List<VerifiedMessage>> getVerified() {
    return verified;
  }

}
