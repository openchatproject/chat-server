package com.openchat.imservice.api.messages.calls;

import com.openchat.protocal.util.guava.Optional;

import java.util.LinkedList;
import java.util.List;

public class OpenchatServiceCallMessage {

  private final Optional<OfferMessage>           offerMessage;
  private final Optional<AnswerMessage>          answerMessage;
  private final Optional<HangupMessage>          hangupMessage;
  private final Optional<BusyMessage>            busyMessage;
  private final Optional<List<IceUpdateMessage>> iceUpdateMessages;

  private OpenchatServiceCallMessage(Optional<OfferMessage> offerMessage,
                                   Optional<AnswerMessage> answerMessage,
                                   Optional<List<IceUpdateMessage>> iceUpdateMessages,
                                   Optional<HangupMessage> hangupMessage,
                                   Optional<BusyMessage> busyMessage)
  {
    this.offerMessage      = offerMessage;
    this.answerMessage     = answerMessage;
    this.iceUpdateMessages = iceUpdateMessages;
    this.hangupMessage     = hangupMessage;
    this.busyMessage       = busyMessage;
  }

  public static OpenchatServiceCallMessage forOffer(OfferMessage offerMessage) {
    return new OpenchatServiceCallMessage(Optional.of(offerMessage),
                                        Optional.<AnswerMessage>absent(),
                                        Optional.<List<IceUpdateMessage>>absent(),
                                        Optional.<HangupMessage>absent(),
                                        Optional.<BusyMessage>absent());
  }

  public static OpenchatServiceCallMessage forAnswer(AnswerMessage answerMessage) {
    return new OpenchatServiceCallMessage(Optional.<OfferMessage>absent(),
                                        Optional.of(answerMessage),
                                        Optional.<List<IceUpdateMessage>>absent(),
                                        Optional.<HangupMessage>absent(),
                                        Optional.<BusyMessage>absent());
  }

  public static OpenchatServiceCallMessage forIceUpdates(List<IceUpdateMessage> iceUpdateMessages) {
    return new OpenchatServiceCallMessage(Optional.<OfferMessage>absent(),
                                        Optional.<AnswerMessage>absent(),
                                        Optional.of(iceUpdateMessages),
                                        Optional.<HangupMessage>absent(),
                                        Optional.<BusyMessage>absent());
  }

  public static OpenchatServiceCallMessage forIceUpdate(final IceUpdateMessage iceUpdateMessage) {
    List<IceUpdateMessage> iceUpdateMessages = new LinkedList<>();
    iceUpdateMessages.add(iceUpdateMessage);

    return new OpenchatServiceCallMessage(Optional.<OfferMessage>absent(),
                                        Optional.<AnswerMessage>absent(),
                                        Optional.of(iceUpdateMessages),
                                        Optional.<HangupMessage>absent(),
                                        Optional.<BusyMessage>absent());
  }

  public static OpenchatServiceCallMessage forHangup(HangupMessage hangupMessage) {
    return new OpenchatServiceCallMessage(Optional.<OfferMessage>absent(),
                                        Optional.<AnswerMessage>absent(),
                                        Optional.<List<IceUpdateMessage>>absent(),
                                        Optional.of(hangupMessage),
                                        Optional.<BusyMessage>absent());
  }

  public static OpenchatServiceCallMessage forBusy(BusyMessage busyMessage) {
    return new OpenchatServiceCallMessage(Optional.<OfferMessage>absent(),
                                        Optional.<AnswerMessage>absent(),
                                        Optional.<List<IceUpdateMessage>>absent(),
                                        Optional.<HangupMessage>absent(),
                                        Optional.of(busyMessage));
  }

  public static OpenchatServiceCallMessage empty() {
    return new OpenchatServiceCallMessage(Optional.<OfferMessage>absent(),
                                        Optional.<AnswerMessage>absent(),
                                        Optional.<List<IceUpdateMessage>>absent(),
                                        Optional.<HangupMessage>absent(),
                                        Optional.<BusyMessage>absent());
  }

  public Optional<List<IceUpdateMessage>> getIceUpdateMessages() {
    return iceUpdateMessages;
  }

  public Optional<AnswerMessage> getAnswerMessage() {
    return answerMessage;
  }

  public Optional<OfferMessage> getOfferMessage() {
    return offerMessage;
  }

  public Optional<HangupMessage> getHangupMessage() {
    return hangupMessage;
  }

  public Optional<BusyMessage> getBusyMessage() {
    return busyMessage;
  }
}
