package com.openchat.protocal.state;

import com.openchat.protocal.OpenchatProtocolAddress;

import java.util.List;


public interface SessionStore {

  
  public SessionRecord loadSession(OpenchatProtocolAddress address);

  
  public List<Integer> getSubDeviceSessions(String name);

  
  public void storeSession(OpenchatProtocolAddress address, SessionRecord record);

  
  public boolean containsSession(OpenchatProtocolAddress address);

  
  public void deleteSession(OpenchatProtocolAddress address);

  
  public void deleteAllSessions(String name);

}
