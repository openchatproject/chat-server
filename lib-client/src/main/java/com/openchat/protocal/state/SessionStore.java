package com.openchat.protocal.state;

import java.util.List;


public interface SessionStore {

  
  public SessionRecord loadSession(long recipientId, int deviceId);

  
  public List<Integer> getSubDeviceSessions(long recipientId);

  
  public void storeSession(long recipientId, int deviceId, SessionRecord record);

  
  public boolean containsSession(long recipientId, int deviceId);

  
  public void deleteSession(long recipientId, int deviceId);

  
  public void deleteAllSessions(long recipientId);

}
