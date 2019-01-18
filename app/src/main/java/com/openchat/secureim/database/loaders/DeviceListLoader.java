package com.openchat.secureim.database.loaders;

import android.content.Context;
import android.util.Log;

import com.openchat.secureim.util.AsyncLoader;
import com.openchat.imservice.api.openchatServiceAccountManager;
import com.openchat.imservice.api.messages.multidevice.DeviceInfo;
import com.openchat.imservice.api.push.openchatServiceAddress;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class DeviceListLoader extends AsyncLoader<List<DeviceInfo>> {

  private static final String TAG = DeviceListLoader.class.getSimpleName();

  private final openchatServiceAccountManager accountManager;

  public DeviceListLoader(Context context, openchatServiceAccountManager accountManager) {
    super(context);
    this.accountManager = accountManager;
  }

  @Override
  public List<DeviceInfo> loadInBackground() {
    try {
      List<DeviceInfo>     devices  = accountManager.getDevices();
      Iterator<DeviceInfo> iterator = devices.iterator();

      while (iterator.hasNext()) {
        if ((iterator.next().getId() == openchatServiceAddress.DEFAULT_DEVICE_ID)) {
          iterator.remove();
        }
      }

      Collections.sort(devices, new DeviceInfoComparator());

      return devices;
    } catch (IOException e) {
      Log.w(TAG, e);
      return null;
    }
  }

  private static class DeviceInfoComparator implements Comparator<DeviceInfo> {

    @Override
    public int compare(DeviceInfo lhs, DeviceInfo rhs) {
      if      (lhs.getCreated() < rhs.getCreated())  return -1;
      else if (lhs.getCreated() != rhs.getCreated()) return 1;
      else                                           return 0;
    }
  }
}
