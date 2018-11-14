package com.openchat.imservice.api.messages.calls;

import com.openchat.protocal.util.guava.Optional;

public class OpenchatServiceCallMessage {

  private final Optional<OfferMessage>     offerMessage;
  private final Optional<AnswerMessage>    answerMessage;
  private final Optional<HangupMessage>    hangupMessage;
  private final Optional<BusyMessage>      busyMessage;
  private final Optional<IceUpdateMessage> iceUpdateMessage;

  private OpenchatServiceCallMessage(Optional<OfferMessage> offerMessage,
                                   Optional<AnswerMessage> answerMessage,
                                   Optional<IceUpdateMessage> iceUpdateMessage,
                                   Optional<HangupMessage> hangupMessage,
                                   Optional<BusyMessage> busyMessage)
  {
    this.offerMessage     = offerMessage;
    this.answerMessage    = answerMessage;
    this.iceUpdateMessage = iceUpdateMessage;
    this.hangupMessage    = hangupMessage;
    this.busyMessage      = busyMessage;
  }

  public static OpenchatServiceCallMessage forOffer(OfferMessage offerMessage) {
    return new OpenchatServiceCallMessage(Optional.of(offerMessage),
                                        Optional.<AnswerMessage>absent(),
                                        Optional.<IceUpdateMessage>absent(),
                                        Optional.<HangupMessage>absent(),
                                        Optional.<BusyMessage>absent());
  }

  public static OpenchatServiceCallMessage forAnswer(AnswerMessage answerMessage) {
    return new OpenchatServiceCallMessage(Optional.<OfferMessage>absent(),
                                        Optional.of(answerMessage),
                                        Optional.<IceUpdateMessage>absent(),
                                        Optional.<HangupMessage>absent(),
                                        Optional.<BusyMessage>absent());
  }

  public static OpenchatServiceCallMessage forIceUpdate(IceUpdateMessage iceUpdateMessage) {
    return new OpenchatServiceCallMessage(Optional.<OfferMessage>absent(),
                                        Optional.<AnswerMessage>absent(),
                                        Optional.of(iceUpdateMessage),
                                        Optional.<HangupMessage>absent(),
                                        Optional.<BusyMessage>absent());
  }

  public static OpenchatServiceCallMessage forHangup(HangupMessage hangupMessage) {
    return new OpenchatServiceCallMessage(Optional.<OfferMessage>absent(),
                                        Optional.<AnswerMessage>absent(),
                                        Optional.<IceUpdateMessage>absent(),
                                        Optional.of(hangupMessage),
                                        Optional.<BusyMessage>absent());
  }

  public static OpenchatServiceCallMessage forBusy(BusyMessage busyMessage) {
    return new OpenchatServiceCallMessage(Optional.<OfferMessage>absent(),
                                        Optional.<AnswerMessage>absent(),
                                        Optional.<IceUpdateMessage>absent(),
                                        Optional.<HangupMessage>absent(),
                                        Optional.of(busyMessage));
  }

  public static OpenchatServiceCallMessage empty() {
    return new OpenchatServiceCallMessage(Optional.<OfferMessage>absent(),
                                        Optional.<AnswerMessage>absent(),
                                        Optional.<IceUpdateMessage>absent(),
                                        Optional.<HangupMessage>absent(),
                                        Optional.<BusyMessage>absent());
  }

  public Optional<IceUpdateMessage> getIceUpdateMessage() {
    return iceUpdateMessage;
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
