package com.openchat.secureim.jobs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.openchat.secureim.dependencies.InjectableType;
import com.openchat.jobqueue.JobParameters;
import com.openchat.jobqueue.requirements.NetworkRequirement;
import com.openchat.imservice.api.openchatServiceMessageSender;
import com.openchat.imservice.api.crypto.UntrustedIdentityException;
import com.openchat.imservice.api.messages.openchatServiceDataMessage;
import com.openchat.imservice.api.messages.openchatServiceGroup;
import com.openchat.imservice.api.messages.openchatServiceGroup.Type;
import com.openchat.imservice.api.push.openchatServiceAddress;
import com.openchat.imservice.api.push.exceptions.PushNetworkException;

import java.io.IOException;

import javax.inject.Inject;

public class RequestGroupInfoJob extends ContextJob implements InjectableType {

  private static final String TAG = RequestGroupInfoJob.class.getSimpleName();

  private static final long serialVersionUID = 0L;

  @Inject transient openchatServiceMessageSender messageSender;

  private final String source;
  private final byte[] groupId;

  public RequestGroupInfoJob(@NonNull Context context, @NonNull String source, @NonNull byte[] groupId) {
    super(context, JobParameters.newBuilder()
                                .withRequirement(new NetworkRequirement(context))
                                .withPersistence()
                                .withRetryCount(50)
                                .create());

    this.source  = source;
    this.groupId = groupId;
  }

  @Override
  public void onAdded() {}

  @Override
  public void onRun() throws IOException, UntrustedIdentityException {
    openchatServiceGroup       group   = openchatServiceGroup.newBuilder(Type.REQUEST_INFO)
                                                         .withId(groupId)
                                                         .build();

    openchatServiceDataMessage message = openchatServiceDataMessage.newBuilder()
                                                               .asGroupMessage(group)
                                                               .withTimestamp(System.currentTimeMillis())
                                                               .build();

    messageSender.sendMessage(new openchatServiceAddress(source), message);
  }

  @Override
  public boolean onShouldRetry(Exception e) {
    return e instanceof PushNetworkException;
  }

  @Override
  public void onCanceled() {

  }
}
