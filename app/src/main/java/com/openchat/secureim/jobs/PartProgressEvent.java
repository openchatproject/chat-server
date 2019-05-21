package com.openchat.secureim.jobs;

import com.openchat.secureim.database.PartDatabase.PartId;

public class PartProgressEvent {
  public PartId partId;
  public long   total;
  public long   progress;

  public PartProgressEvent(PartId partId, long total, long progress) {
    this.partId   = partId;
    this.total    = total;
    this.progress = progress;
  }
}
