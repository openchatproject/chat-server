package com.openchat.secureim.storage;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface PendingDevices {

  @SqlUpdate("WITH upsert AS (UPDATE pending_devices SET verification_code = :verification_code WHERE number = :number RETURNING *) " +
             "INSERT INTO pending_devices (number, verification_code) SELECT :number, :verification_code WHERE NOT EXISTS (SELECT * FROM upsert)")
  void insert(@Bind("number") String number, @Bind("verification_code") String verificationCode);

  @SqlQuery("SELECT verification_code FROM pending_devices WHERE number = :number")
  String getCodeForNumber(@Bind("number") String number);

  @SqlUpdate("DELETE FROM pending_devices WHERE number = :number")
  void remove(@Bind("number") String number);
}
