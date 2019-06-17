package com.openchat.secureim.jobs.requirements;

import com.openchat.jobqueue.requirements.RequirementListener;
import com.openchat.jobqueue.requirements.RequirementProvider;

public class MediaNetworkRequirementProvider implements RequirementProvider {

  private RequirementListener listener;

  public void notifyMediaControlEvent() {
    if (listener != null) listener.onRequirementStatusChanged();
  }

  @Override
  public void setListener(RequirementListener listener) {
    this.listener = listener;
  }
}
