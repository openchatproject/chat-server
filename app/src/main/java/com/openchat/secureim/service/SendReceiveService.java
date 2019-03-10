package com.openchat.secureim.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.openchat.secureim.crypto.InvalidPassphraseException;
import com.openchat.secureim.crypto.MasterSecretUtil;
import com.openchat.secureim.database.CanonicalSessionMigrator;
import com.openchat.secureim.util.OpenchatServicePreferences;
import com.openchat.secureim.util.WorkerThread;
import com.openchat.secureim.crypto.MasterSecret;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SendReceiveService extends Service {

  public static final String SEND_SMS_ACTION                  = "com.openchat.secureim.SendReceiveService.SEND_SMS_ACTION";
  public static final String SENT_SMS_ACTION                  = "com.openchat.secureim.SendReceiveService.SENT_SMS_ACTION";
  public static final String DELIVERED_SMS_ACTION             = "com.openchat.secureim.SendReceiveService.DELIVERED_SMS_ACTION";
  public static final String SEND_MMS_ACTION                  = "com.openchat.secureim.SendReceiveService.SEND_MMS_ACTION";

  public static final String MASTER_SECRET_EXTRA = "master_secret";

  private static final int SEND_SMS              = 0;
  private static final int SEND_MMS              = 2;

  private ToastHandler        toastHandler;
  private SystemStateListener systemStateListener;

  private SmsSender        smsSender;
  private MmsSender        mmsSender;

  private MasterSecret masterSecret;
  private boolean      hasSecret;

  private NewKeyReceiver newKeyReceiver;
  private ClearKeyReceiver clearKeyReceiver;
  private List<Runnable> workQueue;
  private List<Runnable> pendingSecretList;

  @Override
  public void onCreate() {
    initializeHandlers();
    initializeProcessors();
    initializeAddressCanonicalization();
    initializeWorkQueue();
    initializeMasterSecret();
  }

  @Override
  public void onStart(Intent intent, int startId) {
    if (intent == null) return;

    String action = intent.getAction();

    if (action.equals(SEND_SMS_ACTION))
      scheduleSecretRequiredIntent(SEND_SMS, intent);
    else if (action.equals(SENT_SMS_ACTION))
      scheduleIntent(SEND_SMS, intent);
    else if (action.equals(DELIVERED_SMS_ACTION))
      scheduleIntent(SEND_SMS, intent);
    else if (action.equals(SEND_MMS_ACTION))
      scheduleSecretRequiredIntent(SEND_MMS, intent);
    else
      Log.w("SendReceiveService", "Received intent with unknown action: " + intent.getAction());
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public void onDestroy() {
    Log.w("SendReceiveService", "onDestroy()...");
    super.onDestroy();

    if (newKeyReceiver != null)
      unregisterReceiver(newKeyReceiver);

    if (clearKeyReceiver != null)
      unregisterReceiver(clearKeyReceiver);
  }

  private void initializeHandlers() {
    systemStateListener = new SystemStateListener(this);
    toastHandler        = new ToastHandler();
  }

  private void initializeProcessors() {
    smsSender        = new SmsSender(this, systemStateListener, toastHandler);
    mmsSender        = new MmsSender(this, systemStateListener, toastHandler);
  }

  private void initializeWorkQueue() {
    pendingSecretList = new LinkedList<Runnable>();
    workQueue         = new LinkedList<Runnable>();

    Thread workerThread = new WorkerThread(workQueue, "SendReceveService-WorkerThread");
    workerThread.start();
  }

  private void initializeMasterSecret() {
    hasSecret           = false;
    newKeyReceiver      = new NewKeyReceiver();
    clearKeyReceiver    = new ClearKeyReceiver();

    IntentFilter newKeyFilter = new IntentFilter(KeyCachingService.NEW_KEY_EVENT);
    registerReceiver(newKeyReceiver, newKeyFilter, KeyCachingService.KEY_PERMISSION, null);

    IntentFilter clearKeyFilter = new IntentFilter(KeyCachingService.CLEAR_KEY_EVENT);
    registerReceiver(clearKeyReceiver, clearKeyFilter, KeyCachingService.KEY_PERMISSION, null);

    initializeWithMasterSecret(KeyCachingService.getMasterSecret(this));
  }

  private void initializeWithMasterSecret(MasterSecret masterSecret) {
    Log.w("SendReceiveService", "SendReceive service got master secret.");

    if (masterSecret != null) {
      synchronized (workQueue) {
        this.masterSecret = masterSecret;
        this.hasSecret    = true;

        Iterator<Runnable> iterator = pendingSecretList.iterator();
        while (iterator.hasNext())
          workQueue.add(iterator.next());

        workQueue.notifyAll();
      }
    }
  }

  private void initializeAddressCanonicalization() {
    CanonicalSessionMigrator.migrateSessions(this);
  }

  private MasterSecret getPlaceholderSecret() {
    try {
      return MasterSecretUtil.getMasterSecret(SendReceiveService.this,
                                              MasterSecretUtil.UNENCRYPTED_PASSPHRASE);
    } catch (InvalidPassphraseException e) {
      Log.w("SendReceiveService", e);
      return null;
    }
  }

  private void scheduleIntent(int what, Intent intent) {
    Runnable work = new SendReceiveWorkItem(intent, what);

    synchronized (workQueue) {
      workQueue.add(work);
      workQueue.notifyAll();
    }
  }

  private void scheduleSecretRequiredIntent(int what, Intent intent) {
    Runnable work = new SendReceiveWorkItem(intent, what);

    synchronized (workQueue) {
      if (!hasSecret && OpenchatServicePreferences.isPasswordDisabled(SendReceiveService.this)) {
        initializeWithMasterSecret(getPlaceholderSecret());
      }

      if (hasSecret) {
        workQueue.add(work);
        workQueue.notifyAll();
      } else {
        pendingSecretList.add(work);
      }
    }
  }

  private class SendReceiveWorkItem implements Runnable {
    private final Intent intent;
    private final int what;

    public SendReceiveWorkItem(Intent intent, int what) {
      this.intent = intent;
      this.what   = what;
    }

    @Override
    public void run() {
      MasterSecret masterSecret = SendReceiveService.this.masterSecret;

      if (masterSecret == null && OpenchatServicePreferences.isPasswordDisabled(SendReceiveService.this)) {
        masterSecret = getPlaceholderSecret();
      }

      switch (what) {
      case SEND_SMS:		         smsSender.process(masterSecret, intent);        return;
      case SEND_MMS:             mmsSender.process(masterSecret, intent);        return;
      }
    }
  }

  public class ToastHandler extends Handler {
    public void makeToast(String toast) {
      Message message = this.obtainMessage();
      message.obj     = toast;
      this.sendMessage(message);
    }
    @Override
    public void handleMessage(Message message) {
      Toast.makeText(SendReceiveService.this, (String)message.obj, Toast.LENGTH_LONG).show();
    }
  }

  private class NewKeyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      Log.w("SendReceiveService", "Got a MasterSecret broadcast...");
      initializeWithMasterSecret((MasterSecret)intent.getParcelableExtra(MASTER_SECRET_EXTRA));
    }
  }

  
  private class ClearKeyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      Log.w("SendReceiveService", "Got a clear mastersecret broadcast...");

      synchronized (workQueue) {
        SendReceiveService.this.hasSecret = false;
        workQueue.add(new Runnable() {
          @Override
          public void run() {
            Log.w("SendReceiveService", "Running clear key work item...");

            synchronized (workQueue) {
              if (!SendReceiveService.this.hasSecret) {
                Log.w("SendReceiveService", "Actually clearing key...");
                SendReceiveService.this.masterSecret = null;
              }
            }
          }
        });

        workQueue.notifyAll();
      }
    }
  };
}
