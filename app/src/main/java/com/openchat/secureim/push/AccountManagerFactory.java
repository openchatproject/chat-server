package com.openchat.secureim.push;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.security.ProviderInstaller;

import com.openchat.secureim.BuildConfig;
import com.openchat.secureim.util.TextSecurePreferences;
import com.openchat.imservice.api.openchatServiceAccountManager;

public class AccountManagerFactory {

  private static final String TAG = AccountManagerFactory.class.getName();

  public static openchatServiceAccountManager createManager(Context context) {
    return new openchatServiceAccountManager(new openchatServiceNetworkAccess(context).getConfiguration(context),
                                           TextSecurePreferences.getLocalNumber(context),
                                           TextSecurePreferences.getPushServerPassword(context),
                                           BuildConfig.USER_AGENT);
  }

  public static openchatServiceAccountManager createManager(final Context context, String number, String password) {
    if (new openchatServiceNetworkAccess(context).isCensored(number)) {
      new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... params) {
          try {
            ProviderInstaller.installIfNeeded(context);
          } catch (Throwable t) {
            Log.w(TAG, t);
          }
          return null;
        }
      }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    return new openchatServiceAccountManager(new openchatServiceNetworkAccess(context).getConfiguration(number),
                                           number, password, BuildConfig.USER_AGENT);
  }

}
