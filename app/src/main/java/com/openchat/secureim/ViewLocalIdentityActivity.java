package com.openchat.secureim;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import com.openchat.secureim.crypto.IdentityKeyUtil;
import com.openchat.secureim.util.Dialogs;
import com.openchat.imservice.crypto.MasterSecret;

public class ViewLocalIdentityActivity extends ViewIdentityActivity {

  private MasterSecret masterSecret;

  public void onCreate(Bundle bundle) {
    this.masterSecret = getIntent().getParcelableExtra("master_secret");

    getIntent().putExtra("identity_key", IdentityKeyUtil.getIdentityKey(this));
    getIntent().putExtra("title", getString(R.string.ViewIdentityActivity_my_identity_fingerprint));
    super.onCreate(bundle);
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);

    MenuInflater inflater = this.getSupportMenuInflater();
    inflater.inflate(R.menu.local_identity, menu);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);

    switch (item.getItemId()) {
      case android.R.id.home:        finish();                        return true;
    }

    return false;
  }

  private void promptToRegenerateIdentityKey() {
    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    dialog.setIcon(Dialogs.resolveIcon(this, R.attr.dialog_alert_icon));
    dialog.setTitle(getString(R.string.ViewLocalIdentityActivity_reset_identity_key));
    dialog.setMessage(getString(R.string.ViewLocalIdentityActivity_by_regenerating_your_identity_key_your_existing_contacts_will_receive_warnings));
    dialog.setNegativeButton(getString(R.string.ViewLocalIdentityActivity_cancel), null);
    dialog.setPositiveButton(getString(R.string.ViewLocalIdentityActivity_continue),
                             new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        regenerateIdentityKey();
      }
    });
    dialog.show();
  }

  private void regenerateIdentityKey() {
    new AsyncTask<Void, Void, Void>() {
      private ProgressDialog progressDialog;

      @Override
      protected void onPreExecute() {
        progressDialog = ProgressDialog.show(ViewLocalIdentityActivity.this,
                                             getString(R.string.ViewLocalIdentityActivity_regenerating),
                                             getString(R.string.ViewLocalIdentityActivity_regenerating_identity_key),
                                             true, false);
      }

      @Override
      public Void doInBackground(Void... params) {
        IdentityKeyUtil.generateIdentityKeys(ViewLocalIdentityActivity.this, masterSecret);
        return null;
      }

      @Override
      protected void onPostExecute(Void result) {
        if (progressDialog != null)
          progressDialog.dismiss();

        Toast.makeText(ViewLocalIdentityActivity.this,
                       getString(R.string.ViewLocalIdentityActivity_regenerated),
                       Toast.LENGTH_LONG).show();

        getIntent().putExtra("identity_key",
                             IdentityKeyUtil.getIdentityKey(ViewLocalIdentityActivity.this));
        initialize();
      }

    }.execute();
  }

}
