package com.openchat.secureim.push;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Meter;
import com.openchat.secureim.entities.CryptoEncodingException;
import com.openchat.secureim.entities.EncryptedOutgoingMessage;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GCMSender {

  private final Meter success = Metrics.newMeter(GCMSender.class, "sent", "success", TimeUnit.MINUTES);
  private final Meter failure = Metrics.newMeter(GCMSender.class, "sent", "failure", TimeUnit.MINUTES);

  private final Sender sender;

  public GCMSender(String apiKey) {
    this.sender = new Sender(apiKey);
  }

  public String sendMessage(String gcmRegistrationId, EncryptedOutgoingMessage outgoingMessage)
      throws NotPushRegisteredException, TransientPushFailureException
  {
    try {
      Message gcmMessage = new Message.Builder().addData("type", "message")
                                                .addData("message", outgoingMessage.serialize())
                                                .build();

      Result  result = sender.send(gcmMessage, gcmRegistrationId, 5);

      if (result.getMessageId() != null) {
        success.mark();
        return result.getCanonicalRegistrationId();
      } else {
        failure.mark();
        if (result.getErrorCodeName().equals(Constants.ERROR_NOT_REGISTERED)) {
          throw new NotPushRegisteredException("Device no longer registered with GCM.");
        } else {
          throw new TransientPushFailureException("GCM Failed: " + result.getErrorCodeName());
        }
      }
    } catch (IOException e) {
      throw new TransientPushFailureException(e);
    } catch (CryptoEncodingException e) {
      throw new NotPushRegisteredException(e);
    }
  }
}
