package com.openchat.protocal.groups.state;

import com.openchat.protocal.groups.SenderKeyName;

public interface SenderKeyStore {

  
  public void storeSenderKey(SenderKeyName senderKeyName, SenderKeyRecord record);

  

  public SenderKeyRecord loadSenderKey(SenderKeyName senderKeyName);
}
