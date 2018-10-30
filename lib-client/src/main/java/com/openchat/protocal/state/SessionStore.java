package com.openchat.protocal.state;

import com.openchat.protocal.OpenchatAddress;

import java.util.List;


public interface SessionStore {

  
  public SessionRecord loadSession(OpenchatAddress address);

  
  public List<Integer> getSubDeviceSessions(String name);

  
  public void storeSession(OpenchatAddress address, SessionRecord record);

  
  public boolean containsSession(OpenchatAddress address);

  
  public void deleteSession(OpenchatAddress address);

  
  public void deleteAllSessions(String name);

}
