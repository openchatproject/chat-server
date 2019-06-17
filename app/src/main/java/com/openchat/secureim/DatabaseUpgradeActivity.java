package com.openchat.secureim;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.openchat.secureim.crypto.IdentityKeyUtil;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.crypto.storage.OpenchatServicePreKeyStore;
import com.openchat.secureim.crypto.storage.OpenchatServiceSessionStore;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.MmsDatabase;
import com.openchat.secureim.database.MmsDatabase.Reader;
import com.openchat.secureim.database.PushDatabase;
import com.openchat.secureim.database.model.MessageRecord;
import com.openchat.secureim.jobs.AttachmentDownloadJob;
import com.openchat.secureim.jobs.CreateSignedPreKeyJob;
import com.openchat.secureim.jobs.DirectoryRefreshJob;
import com.openchat.secureim.jobs.PushDecryptJob;
import com.openchat.secureim.notifications.MessageNotifier;
import com.openchat.secureim.util.Util;
import com.openchat.secureim.util.VersionTracker;

import java.io.File;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import ws.com.google.android.mms.pdu.PduPart;

public class DatabaseUpgradeActivity extends BaseActivity {
  private static final String TAG = DatabaseUpgradeActivity.class.getSimpleName();

  public static final int NO_MORE_KEY_EXCHANGE_PREFIX_VERSION  = 46;
  public static final int MMS_BODY_VERSION                     = 46;
  public static final int TOFU_IDENTITIES_VERSION              = 50;
  public static final int CURVE25519_VERSION                   = 63;
  public static final int ASYMMETRIC_MASTER_SECRET_FIX_VERSION = 73;
  public static final int NO_V1_VERSION                        = 83;
  public static final int SIGNED_PREKEY_VERSION                = 83;
  public static final int NO_DECRYPT_QUEUE_VERSION             = 113;
  public static final int PUSH_DECRYPT_SERIAL_ID_VERSION       = 131;
  public static final int MIGRATE_SESSION_PLAINTEXT            = 136;
  public static final int CONTACTS_ACCOUNT_VERSION             = 136;
  public static final int MEDIA_DOWNLOAD_CONTROLS_VERSION      = 146;

  private static final SortedSet<Integer> UPGRADE_VERSIONS = new TreeSet<Integer>() {{
    add(NO_MORE_KEY_EXCHANGE_PREFIX_VERSION);
    add(TOFU_IDENTITIES_VERSION);
    add(CURVE25519_VERSION);
    add(ASYMMETRIC_MASTER_SECRET_FIX_VERSION);
    add(NO_V1_VERSION);
    add(SIGNED_PREKEY_VERSION);
    add(NO_DECRYPT_QUEUE_VERSION);
    add(PUSH_DECRYPT_SERIAL_ID_VERSION);
    add(MIGRATE_SESSION_PLAINTEXT);
    add(MEDIA_DOWNLOAD_CONTROLS_VERSION);
  }};

  private MasterSecret masterSecret;

  @Override
  public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    this.masterSecret = getIntent().getParcelableExtra("master_secret");

    if (needsUpgradeTask()) {
      Log.w("DatabaseUpgradeActivity", "Upgrading...");
      setContentView(R.layout.database_upgrade_activity);

      ProgressBar indeterminateProgress = (ProgressBar)findViewById(R.id.indeterminate_progress);
      ProgressBar determinateProgress   = (ProgressBar)findViewById(R.id.determinate_progress);

      new DatabaseUpgradeTask(indeterminateProgress, determinateProgress)
          .execute(VersionTracker.getLastSeenVersion(this));
    } else {
      VersionTracker.updateLastSeenVersion(this);
      updateNotifications(this, masterSecret);
      startActivity((Intent)getIntent().getParcelableExtra("next_intent"));
      finish();
    }
  }

  private boolean needsUpgradeTask() {
    int currentVersionCode = Util.getCurrentApkReleaseVersion(this);
    int lastSeenVersion    = VersionTracker.getLastSeenVersion(this);

    Log.w("DatabaseUpgradeActivity", "LastSeenVersion: " + lastSeenVersion);

    if (lastSeenVersion >= currentVersionCode)
      return false;

    for (int version : UPGRADE_VERSIONS) {
      Log.w("DatabaseUpgradeActivity", "Comparing: " + version);
      if (lastSeenVersion < version)
        return true;
    }

    return false;
  }

  public static boolean isUpdate(Context context) {
    try {
      int currentVersionCode  = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
      int previousVersionCode = VersionTracker.getLastSeenVersion(context);

      return previousVersionCode < currentVersionCode;
    } catch (PackageManager.NameNotFoundException e) {
      throw new AssertionError(e);
    }
  }

  private void updateNotifications(final Context context, final MasterSecret masterSecret) {
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... params) {
        MessageNotifier.updateNotification(context, masterSecret);
        return null;
      }
    }.execute();
  }

  public interface DatabaseUpgradeListener {
    public void setProgress(int progress, int total);
  }

  private class DatabaseUpgradeTask extends AsyncTask<Integer, Double, Void>
      implements DatabaseUpgradeListener
  {

    private final ProgressBar indeterminateProgress;
    private final ProgressBar determinateProgress;

    public DatabaseUpgradeTask(ProgressBar indeterminateProgress, ProgressBar determinateProgress) {
      this.indeterminateProgress = indeterminateProgress;
      this.determinateProgress   = determinateProgress;
    }

    @Override
    protected Void doInBackground(Integer... params) {
      Context context = DatabaseUpgradeActivity.this.getApplicationContext();

      Log.w("DatabaseUpgradeActivity", "Running background upgrade..");
      DatabaseFactory.getInstance(DatabaseUpgradeActivity.this)
                     .onApplicationLevelUpgrade(context, masterSecret, params[0], this);

      if (params[0] < CURVE25519_VERSION) {
        IdentityKeyUtil.migrateIdentityKeys(context, masterSecret);
      }

      if (params[0] < NO_V1_VERSION) {
        File v1sessions = new File(context.getFilesDir(), "sessions");

        if (v1sessions.exists() && v1sessions.isDirectory()) {
          File[] contents = v1sessions.listFiles();

          if (contents != null) {
            for (File session : contents) {
              session.delete();
            }
          }

          v1sessions.delete();
        }
      }

      if (params[0] < SIGNED_PREKEY_VERSION) {
        ApplicationContext.getInstance(getApplicationContext())
                          .getJobManager()
                          .add(new CreateSignedPreKeyJob(context));
      }

      if (params[0] < NO_DECRYPT_QUEUE_VERSION) {
        scheduleMessagesInPushDatabase(context);
      }

      if (params[0] < PUSH_DECRYPT_SERIAL_ID_VERSION) {
        scheduleMessagesInPushDatabase(context);
      }

      if (params[0] < MIGRATE_SESSION_PLAINTEXT) {
        new OpenchatServiceSessionStore(context, masterSecret).migrateSessions();
        new OpenchatServicePreKeyStore(context, masterSecret).migrateRecords();

        IdentityKeyUtil.migrateIdentityKeys(context, masterSecret);
        scheduleMessagesInPushDatabase(context);;
      }

      if (params[0] < CONTACTS_ACCOUNT_VERSION) {
        ApplicationContext.getInstance(getApplicationContext())
                          .getJobManager()
                          .add(new DirectoryRefreshJob(getApplicationContext()));
      }

      if (params[0] < MEDIA_DOWNLOAD_CONTROLS_VERSION) {
        schedulePendingIncomingParts(context);
      }

      return null;
    }

    private void schedulePendingIncomingParts(Context context) {
      MmsDatabase   db           = DatabaseFactory.getMmsDatabase(context);
      List<PduPart> pendingParts = DatabaseFactory.getPartDatabase(context).getPendingParts();

      Log.w(TAG, pendingParts.size() + " pending parts.");
      for (PduPart part : pendingParts) {
        final Reader        reader = db.readerFor(masterSecret, db.getMessage(part.getMmsId()));
        final MessageRecord record = reader.getNext();

        if (record != null && !record.isOutgoing() && record.isPush()) {
          Log.w(TAG, "queuing new attachment download job for incoming push part.");
          ApplicationContext.getInstance(context)
                            .getJobManager()
                            .add(new AttachmentDownloadJob(context, part.getMmsId(), part.getPartId()));
        }
        reader.close();
      }
    }

    private void scheduleMessagesInPushDatabase(Context context) {
      PushDatabase pushDatabase = DatabaseFactory.getPushDatabase(context);
      Cursor       pushReader   = null;

      try {
        pushReader = pushDatabase.getPending();

        while (pushReader != null && pushReader.moveToNext()) {
          ApplicationContext.getInstance(getApplicationContext())
                            .getJobManager()
                            .add(new PushDecryptJob(getApplicationContext(),
                                                    pushReader.getLong(pushReader.getColumnIndexOrThrow(PushDatabase.ID)),
                                                    pushReader.getString(pushReader.getColumnIndexOrThrow(PushDatabase.SOURCE))));
        }
      } finally {
        if (pushReader != null)
          pushReader.close();
      }
    }

    @Override
    protected void onProgressUpdate(Double... update) {
      indeterminateProgress.setVisibility(View.GONE);
      determinateProgress.setVisibility(View.VISIBLE);

      double scaler = update[0];
      determinateProgress.setProgress((int)Math.floor(determinateProgress.getMax() * scaler));
    }

    @Override
    protected void onPostExecute(Void result) {
      VersionTracker.updateLastSeenVersion(DatabaseUpgradeActivity.this);
      updateNotifications(DatabaseUpgradeActivity.this, masterSecret);

      startActivity((Intent)getIntent().getParcelableExtra("next_intent"));
      finish();
    }

    @Override
    public void setProgress(int progress, int total) {
      publishProgress(((double)progress / (double)total));
    }
  }

}
