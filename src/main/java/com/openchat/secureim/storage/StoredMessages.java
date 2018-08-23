package com.openchat.secureim.storage;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import com.openchat.secureim.entities.EncryptedOutgoingMessage;

import java.util.List;

public interface StoredMessages {

  @SqlUpdate("INSERT INTO stored_messages (destination_id, encrypted_message) VALUES :destination_id, :encrypted_message")
  void insert(@Bind("destination_id") long destinationAccountId, @Bind("encrypted_message") EncryptedOutgoingMessage encryptedOutgoingMessage);

  @SqlQuery("SELECT encrypted_message FROM stored_messages WHERE destination_id = :account_id")
  List<EncryptedOutgoingMessage> getMessagesForAccountId(@Bind("account_id") long accountId);
}
