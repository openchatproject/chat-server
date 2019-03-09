package com.openchat.secureim;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.openchat.imservice.crypto.MasterSecret;
import com.openchat.secureim.util.Dialogs;
import com.openchat.secureim.database.NoExternalStorageException;
import com.openchat.secureim.database.PlaintextBackupExporter;

import java.io.IOException;

public class ExportFragment extends Fragment {

  private static final int SUCCESS    = 0;
  private static final int NO_SD_CARD = 1;
  private static final int IO_ERROR   = 2;

  private MasterSecret masterSecret;

  public void setMasterSecret(MasterSecret masterSecret) {
    this.masterSecret = masterSecret;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
    View layout              = inflater.inflate(R.layout.export_fragment, container, false);
    View exportPlaintextView = layout.findViewById(R.id.export_plaintext_backup);

    exportPlaintextView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        handleExportPlaintextBackup();
      }
    });

    return layout;
  }

  private void handleExportPlaintextBackup() {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setIcon(Dialogs.resolveIcon(getActivity(), R.attr.dialog_alert_icon));
    builder.setTitle(getActivity().getString(R.string.ExportFragment_export_plaintext_to_sd_card));
    builder.setMessage(getActivity().getString(R.string.ExportFragment_warning_this_will_export_the_plaintext_contents));
    builder.setPositiveButton(getActivity().getString(R.string.ExportFragment_export), new Dialog.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        new ExportPlaintextTask().execute();
      }
    });
    builder.setNegativeButton(getActivity().getString(R.string.ExportFragment_cancel), null);
    builder.show();
  }

  private class ExportPlaintextTask extends AsyncTask<Void, Void, Integer> {
    private ProgressDialog dialog;

    @Override
    protected void onPreExecute() {
      dialog = ProgressDialog.show(getActivity(), 
                                   getActivity().getString(R.string.ExportFragment_exporting), 
                                   getActivity().getString(R.string.ExportFragment_exporting_plaintext_to_sd_card),
                                   true, false);
    }

    @Override
    protected Integer doInBackground(Void... params) {
      try {
        PlaintextBackupExporter.exportPlaintextToSd(getActivity(), masterSecret);
        return SUCCESS;
      } catch (NoExternalStorageException e) {
        Log.w("ExportFragment", e);
        return NO_SD_CARD;
      } catch (IOException e) {
        Log.w("ExportFragment", e);
        return IO_ERROR;
      }
    }

    @Override
    protected void onPostExecute(Integer result) {
      Context context = getActivity();

      if (dialog != null)
        dialog.dismiss();

      if (context == null)
        return;

      switch (result) {
        case NO_SD_CARD:
          Toast.makeText(context,
                         context.getString(R.string.ExportFragment_error_unable_to_write_to_sd_card),
                         Toast.LENGTH_LONG).show();
          break;
        case IO_ERROR:
          Toast.makeText(context,
                         context.getString(R.string.ExportFragment_error_while_writing_to_sd_card),
                         Toast.LENGTH_LONG).show();
          break;
        case SUCCESS:
          Toast.makeText(context,
                         context.getString(R.string.ExportFragment_success),
                         Toast.LENGTH_LONG).show();
          break;
      }
    }
  }

}
