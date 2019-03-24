package com.openchat.secureim.components;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;

import com.openchat.secureim.R;
import com.openchat.secureim.util.OpenchatServicePreferences;

public class OutgoingSmsPreference extends DialogPreference {
  private CheckBox dataUsers;
  private CheckBox askForFallback;
  private CheckBox neverFallbackMms;
  private CheckBox nonDataUsers;

  public OutgoingSmsPreference(Context context, AttributeSet attrs) {
    super(context, attrs);
    setPersistent(false);
    setDialogLayoutResource(R.layout.outgoing_sms_preference);
  }

  @Override
  protected void onBindDialogView(final View view) {
    super.onBindDialogView(view);
    dataUsers        = (CheckBox) view.findViewById(R.id.data_users);
    askForFallback   = (CheckBox) view.findViewById(R.id.ask_before_fallback_data);
    neverFallbackMms = (CheckBox) view.findViewById(R.id.never_send_mms);
    nonDataUsers     = (CheckBox) view.findViewById(R.id.non_data_users);

    dataUsers.setChecked(OpenchatServicePreferences.isFallbackSmsAllowed(getContext()));
    askForFallback.setChecked(OpenchatServicePreferences.isFallbackSmsAskRequired(getContext()));
    neverFallbackMms.setChecked(!OpenchatServicePreferences.isFallbackMmsEnabled(getContext()));
    nonDataUsers.setChecked(OpenchatServicePreferences.isDirectSmsAllowed(getContext()));

    dataUsers.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        updateEnabledViews();
      }
    });

    updateEnabledViews();
  }

  private void updateEnabledViews() {
    askForFallback.setEnabled(dataUsers.isChecked());
    neverFallbackMms.setEnabled(dataUsers.isChecked());
  }

  @Override
  protected void onDialogClosed(boolean positiveResult) {
    super.onDialogClosed(positiveResult);

    if (positiveResult) {
      OpenchatServicePreferences.setFallbackSmsAllowed(getContext(), dataUsers.isChecked());
      OpenchatServicePreferences.setFallbackSmsAskRequired(getContext(), askForFallback.isChecked());
      OpenchatServicePreferences.setDirectSmsAllowed(getContext(), nonDataUsers.isChecked());
      OpenchatServicePreferences.setFallbackMmsEnabled(getContext(), !neverFallbackMms.isChecked());
      if (getOnPreferenceChangeListener() != null) getOnPreferenceChangeListener().onPreferenceChange(this, null);
    }
  }
}
