package com.openchat.secureim;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.util.DirectoryHelper;
import com.openchat.secureim.util.DynamicLanguage;
import com.openchat.secureim.util.DynamicNoActionBarTheme;
import com.openchat.secureim.util.DynamicTheme;
import com.openchat.secureim.util.OpenchatServicePreferences;

import java.util.ArrayList;
import java.util.List;

public class PushContactSelectionActivity extends ContactSelectionActivity {

  private final static String TAG = PushContactSelectionActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle icicle, @NonNull MasterSecret masterSecret) {
    super.onCreate(icicle, masterSecret);
    contactsFragment.setMultiSelect(true);

    action.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_white_24dp));
    action.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent       resultIntent     = getIntent();
        List<String> selectedContacts = contactsFragment.getSelectedContacts();

        if (selectedContacts != null) {
          resultIntent.putStringArrayListExtra("contacts", new ArrayList<>(selectedContacts));
        }

        setResult(RESULT_OK, resultIntent);
        finish();
      }
    });
  }
}
