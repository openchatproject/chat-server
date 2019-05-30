package com.openchat.secureim.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.openchat.secureim.R;
import com.openchat.secureim.contacts.ContactsDatabase;
import com.openchat.secureim.database.NotInDirectoryException;
import com.openchat.secureim.database.OpenchatServiceDirectory;
import com.openchat.secureim.push.OpenchatServiceCommunicationFactory;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.protocal.util.guava.Optional;
import com.openchat.imservice.api.OpenchatServiceAccountManager;
import com.openchat.imservice.api.push.ContactTokenDetails;
import com.openchat.imservice.api.util.InvalidNumberException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DirectoryHelper {
  private static final String TAG = DirectoryHelper.class.getSimpleName();

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
        try {
          DirectoryHelper.refreshDirectory(context.getApplicationContext());
        } catch (IOException e) {
          Log.w(TAG, e);
        }
        return null;
      }

      @Override
      protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (listener != null) listener.onUpdateFinished();
      }
    }.execute();

  }

  public static void refreshDirectory(final Context context) throws IOException {
    refreshDirectory(context, OpenchatServiceCommunicationFactory.createManager(context));
  }

  public static void refreshDirectory(final Context context, final OpenchatServiceAccountManager accountManager)
      throws IOException
  {
    refreshDirectory(context, accountManager, OpenchatServicePreferences.getLocalNumber(context));
  }

  public static void refreshDirectory(final Context context, final OpenchatServiceAccountManager accountManager, final String localNumber)
      throws IOException
  {
    OpenchatServiceDirectory       directory              = OpenchatServiceDirectory.getInstance(context);
    Optional<Account>         account                = getOrCreateAccount(context);
    Set<String>               eligibleContactNumbers = directory.getPushEligibleContactNumbers(localNumber);
    List<ContactTokenDetails> activeTokens           = accountManager.getContacts(eligibleContactNumbers);

    if (activeTokens != null) {
      for (ContactTokenDetails activeToken : activeTokens) {
        eligibleContactNumbers.remove(activeToken.getNumber());
        activeToken.setNumber(activeToken.getNumber());
      }

      directory.setNumbers(activeTokens, eligibleContactNumbers);

      if (account.isPresent()) {
        List<String> e164numbers = new LinkedList<>();

        for (ContactTokenDetails contactTokenDetails : activeTokens) {
          e164numbers.add(contactTokenDetails.getNumber());
        }

        try {
          new ContactsDatabase(context).setRegisteredUsers(account.get(), e164numbers);
        } catch (RemoteException | OperationApplicationException e) {
          Log.w(TAG, e);
        }
      }
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

  private static Optional<Account> getOrCreateAccount(Context context) {
    AccountManager accountManager = AccountManager.get(context);
    Account[]      accounts       = accountManager.getAccountsByType("com.openchat.secureim");

    if (accounts.length == 0) return createAccount(context);
    else                      return Optional.of(accounts[0]);
  }

  private static Optional<Account> createAccount(Context context) {
    AccountManager accountManager = AccountManager.get(context);
    Account        account        = new Account(context.getString(R.string.app_name), "com.openchat.secureim");

    if (accountManager.addAccountExplicitly(account, null, null)) {
      Log.w(TAG, "Created new account...");
      ContentResolver.setIsSyncable(account, ContactsContract.AUTHORITY, 1);
      return Optional.of(account);
    } else {
      Log.w(TAG, "Failed to create account!");
      return Optional.absent();
    }
  }

  public static interface DirectoryUpdateFinishedListener {
    public void onUpdateFinished();
  }
}
