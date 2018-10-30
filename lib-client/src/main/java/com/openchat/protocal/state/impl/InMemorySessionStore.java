package com.openchat.protocal.state.impl;

import com.openchat.protocal.OpenchatAddress;
import com.openchat.protocal.state.SessionRecord;
import com.openchat.protocal.state.SessionStore;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InMemorySessionStore implements SessionStore {

  private Map<OpenchatAddress, byte[]> sessions = new HashMap<>();

  public InMemorySessionStore() {}

  @Override
  public synchronized SessionRecord loadSession(OpenchatAddress remoteAddress) {
    try {
      if (containsSession(remoteAddress)) {
        return new SessionRecord(sessions.get(remoteAddress));
      } else {
        return new SessionRecord();
      }
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }

  @Override
  public synchronized List<Integer> getSubDeviceSessions(String name) {
    List<Integer> deviceIds = new LinkedList<>();

    for (OpenchatAddress key : sessions.keySet()) {
      if (key.getName().equals(name) &&
          key.getDeviceId() != 1)
      {
        deviceIds.add(key.getDeviceId());
      }
    }

    return deviceIds;
  }

  @Override
  public synchronized void storeSession(OpenchatAddress address, SessionRecord record) {
    sessions.put(address, record.serialize());
  }

  @Override
  public synchronized boolean containsSession(OpenchatAddress address) {
    return sessions.containsKey(address);
  }

  @Override
  public synchronized void deleteSession(OpenchatAddress address) {
    sessions.remove(address);
  }

  @Override
  public synchronized void deleteAllSessions(String name) {
    for (OpenchatAddress key : sessions.keySet()) {
      if (key.getName().equals(name)) {
        sessions.remove(key);
      }
    }
  }
}
