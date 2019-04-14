package com.openchat.secureim.util;

import android.content.Context;

import com.afollestad.materialdialogs.AlertDialogWrapper;

import com.openchat.secureim.R;

public class Dialogs {
  public static void showAlertDialog(Context context, String title, String message) {
    AlertDialogWrapper.Builder dialog = new AlertDialogWrapper.Builder(context);
    dialog.setTitle(title);
    dialog.setMessage(message);
    dialog.setIconAttribute(R.attr.dialog_alert_icon);
    dialog.setPositiveButton(android.R.string.ok, null);
    dialog.show();
  }

  public static void showInfoDialog(Context context, String title, String message) {
    AlertDialogWrapper.Builder dialog = new AlertDialogWrapper.Builder(context);
    dialog.setTitle(title);
    dialog.setMessage(message);
    dialog.setIconAttribute(R.attr.dialog_info_icon);
    dialog.setPositiveButton(android.R.string.ok, null);
    dialog.show();
  }
}
