package com.openchat.secureim.components.reminder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.openchat.secureim.R;
import com.openchat.secureim.RegistrationActivity;
import com.openchat.secureim.util.TextSecurePreferences;
import com.openchat.secureim.crypto.MasterSecret;

public class PushRegistrationReminder extends Reminder {

  public PushRegistrationReminder(final Context context, final MasterSecret masterSecret) {
    super(context.getString(R.string.reminder_header_push_title),
          context.getString(R.string.reminder_header_push_text));

    final OnClickListener okListener = new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(context, RegistrationActivity.class);
        intent.putExtra("master_secret", masterSecret);
        intent.putExtra("cancel_button", true);
        context.startActivity(intent);
      }
    };

    setOkListener(okListener);
  }

  @Override
  public boolean isDismissable() {
    return false;
  }

  public static boolean isEligible(Context context) {
    return !TextSecurePreferences.isPushRegistered(context);
  }
}
