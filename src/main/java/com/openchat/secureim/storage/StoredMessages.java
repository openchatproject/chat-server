package com.openchat.secureim.storage;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import java.util.List;

public interface StoredMessages {

  @SqlUpdate("INSERT INTO messages (account_id, device_id, encrypted_message) VALUES (:account_id, :device_id, :encrypted_message)")
  void insert(@Bind("account_id") long accountId, @Bind("device_id") long deviceId, @Bind("encrypted_message") String encryptedOutgoingMessage);

  @SqlBatch("INSERT INTO messages (account_id, device_id, encrypted_message) VALUES (:account_id, :device_id, :encrypted_message)")
  void insert(@Bind("account_id") long accountId, @Bind("device_id") long deviceId, @Bind("encrypted_message") List<String> encryptedOutgoingMessages);

  @SqlQuery("DELETE FROM messages WHERE account_id = :account_id AND device_id = :device_id RETURNING encrypted_message")
  List<String> getMessagesForDevice(@Bind("account_id") long accountId, @Bind("device_id") long deviceId);
}
