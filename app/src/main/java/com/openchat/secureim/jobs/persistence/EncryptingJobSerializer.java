package com.openchat.secureim.jobs.persistence;

import com.openchat.secureim.crypto.MasterCipher;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.util.ParcelUtil;
import com.openchat.jobqueue.EncryptionKeys;
import com.openchat.jobqueue.Job;
import com.openchat.jobqueue.persistence.JavaJobSerializer;
import com.openchat.jobqueue.persistence.JobSerializer;
import com.openchat.libim.InvalidMessageException;

import java.io.IOException;

public class EncryptingJobSerializer implements JobSerializer {

  private final JavaJobSerializer delegate;

  public EncryptingJobSerializer() {
    this.delegate = new JavaJobSerializer();
  }

  @Override
  public String serialize(Job job) throws IOException {
    String plaintext = delegate.serialize(job);

    if (job.getEncryptionKeys() != null) {
      MasterSecret masterSecret = ParcelUtil.deserialize(job.getEncryptionKeys().getEncoded(),
                                                         MasterSecret.CREATOR);
      MasterCipher masterCipher = new MasterCipher(masterSecret);

      return masterCipher.encryptBody(plaintext);
    } else {
      return plaintext;
    }
  }

  @Override
  public Job deserialize(EncryptionKeys keys, boolean encrypted, String serialized) throws IOException {
    try {
      String plaintext;

      if (encrypted) {
        MasterSecret masterSecret = ParcelUtil.deserialize(keys.getEncoded(), MasterSecret.CREATOR);
        MasterCipher masterCipher = new MasterCipher(masterSecret);
        plaintext = masterCipher.decryptBody(serialized);
      } else {
        plaintext = serialized;
      }

      return delegate.deserialize(keys, encrypted, plaintext);
    } catch (InvalidMessageException e) {
      throw new IOException(e);
    }
  }
}
