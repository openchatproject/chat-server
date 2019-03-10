package com.openchat.secureim.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.openchat.secureim.R;
import com.openchat.secureim.database.NotInDirectoryException;
import com.openchat.secureim.database.OpenchatServiceDirectory;
import com.openchat.secureim.push.OpenchatServiceCommunicationFactory;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.imservice.api.OpenchatServiceAccountManager;
import com.openchat.imservice.push.ContactTokenDetails;
import com.openchat.imservice.util.DirectoryUtil;
import com.openchat.imservice.util.InvalidNumberException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class DirectoryHelper {
  private static final String TAG = DirectoryHelper.class.getSimpleName();

  public static void refreshDirectoryWithProgressDialog(final Context context) {
    refreshDirectoryWithProgressDialog(context, null);
  }

  public static void refreshDirectoryWithProgressDialog(final Context context, final DirectoryUpdateFinishedListener listener) {
    if (!OpenchatServicePreferences.isPushRegistered(context)) {
      Toast.makeText(context.getApplicationContext(),
                     context.getString(R.string.SingleContactSelectionActivity_you_are_not_registered_with_the_push_service),
                     Toast.LENGTH_LONG).show();
      return;
    }

    new ProgressDialogAsyncTask<Void,Void,Void>(context,
                                                R.string.SingleContactSelectionActivity_updating_directory,
                                                R.string.SingleContactSelectionActivity_updating_push_directory)
    {
      @Override
      protected Void doInBackground(Void... voids) {
        DirectoryHelper.refreshDirectory(context.getApplicationContext());
        return null;
      }

      @Override
      protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (listener != null) listener.onUpdateFinished();
      }
    }.execute();

  }

  public static void refreshDirectory(final Context context) {
    refreshDirectory(context, OpenchatServiceCommunicationFactory.createManager(context));
  }

  public static void refreshDirectory(final Context context, final OpenchatServiceAccountManager accountManager) {
    refreshDirectory(context, accountManager, OpenchatServicePreferences.getLocalNumber(context));
  }

  public static void refreshDirectory(final Context context, final OpenchatServiceAccountManager accountManager, final String localNumber) {
    OpenchatServiceDirectory       directory              = OpenchatServiceDirectory.getInstance(context);
    Set<String>               eligibleContactNumbers = directory.getPushEligibleContactNumbers(localNumber);
    Map<String, String>       tokenMap               = DirectoryUtil.getDirectoryServerTokenMap(eligibleContactNumbers);
    List<ContactTokenDetails> activeTokens           = accountManager.getContacts(tokenMap.keySet());

    if (activeTokens != null) {
      for (ContactTokenDetails activeToken : activeTokens) {
        eligibleContactNumbers.remove(tokenMap.get(activeToken.getToken()));
        activeToken.setNumber(tokenMap.get(activeToken.getToken()));
      }

      directory.setNumbers(activeTokens, eligibleContactNumbers);
    }
  }

  public static boolean isPushDestination(Context context, Recipients recipients) {
    try {
      if (recipients == null) {
        return false;
      }

      if (!OpenchatServicePreferences.isPushRegistered(context)) {
        return false;
      }

      if (!recipients.isSingleRecipient()) {
        return false;
      }

      if (recipients.isGroupRecipient()) {
        return true;
      }

      final String number = recipients.getPrimaryRecipient().getNumber();

      if (number == null) {
        return false;
      }

      final String e164number = Util.canonicalizeNumber(context, number);

      return OpenchatServiceDirectory.getInstance(context).isActiveNumber(e164number);
    } catch (InvalidNumberException e) {
      Log.w(TAG, e);
      return false;
    } catch (NotInDirectoryException e) {
      return false;
    }
  }

  public static interface DirectoryUpdateFinishedListener {
    public void onUpdateFinished();
  }
}
