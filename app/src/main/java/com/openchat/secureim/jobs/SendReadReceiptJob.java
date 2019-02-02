package com.openchat.secureim.jobs;


import android.content.Context;
import android.util.Log;

import com.openchat.secureim.database.Address;
import com.openchat.secureim.dependencies.InjectableType;
import com.openchat.secureim.util.TextSecurePreferences;
import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.requirements.NetworkRequirement;
import com.openchat.imservice.api.openchatServiceMessageSender;
import com.openchat.imservice.api.crypto.UntrustedIdentityException;
import com.openchat.imservice.api.messages.openchatServiceReceiptMessage;
import com.openchat.imservice.api.push.openchatServiceAddress;
import com.openchat.imservice.api.push.exceptions.PushNetworkException;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

public class SendReadReceiptJob extends ContextJob implements InjectableType {

  private static final long serialVersionUID = 1L;

  private static final String TAG = SendReadReceiptJob.class.getSimpleName();

  @Inject transient openchatServiceMessageSender messageSender;

  private final String     address;
  private final List<Long> messageIds;
  private final long       timestamp;

  public SendReadReceiptJob(Context context, Address address, List<Long> messageIds) {
    super(context, JobParameters.newBuilder()
                                .withRequirement(new NetworkRequirement(context))
                                .withPersistence()
                                .create());

    this.address    = address.serialize();
    this.messageIds = messageIds;
    this.timestamp  = System.currentTimeMillis();
  }

  @Override
  public void onAdded() {}

  @Override
  public void onRun() throws IOException, UntrustedIdentityException {
    if (!TextSecurePreferences.isReadReceiptsEnabled(context)) return;

    openchatServiceAddress        remoteAddress  = new openchatServiceAddress(address);
    openchatServiceReceiptMessage receiptMessage = new openchatServiceReceiptMessage(openchatServiceReceiptMessage.Type.READ, messageIds, timestamp);

    messageSender.sendReceipt(remoteAddress, receiptMessage);
  }

  @Override
  public boolean onShouldRetry(Exception e) {
    if (e instanceof PushNetworkException) return true;
    return false;
  }

  @Override
  public void onCanceled() {
    Log.w(TAG, "Failed to send read receipts to: " + address);
  }
}
