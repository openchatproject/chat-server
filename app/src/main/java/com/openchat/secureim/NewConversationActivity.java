package com.openchat.secureim;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.ThreadDatabase;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.Recipients;

public class NewConversationActivity extends ContactSelectionActivity {

  private static final String TAG = NewConversationActivity.class.getSimpleName();

  @Override
  public void onCreate(Bundle bundle, MasterSecret masterSecret) {
    super.onCreate(bundle, masterSecret);

    action.setVisibility(View.GONE);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public void onContactSelected(String number) {
    Recipients recipients = RecipientFactory.getRecipientsFromString(this, number, true);

    if (recipients != null) {
      Intent intent = new Intent(this, ConversationActivity.class);
      intent.putExtra(ConversationActivity.RECIPIENTS_EXTRA, recipients.getIds());
      intent.putExtra(ConversationActivity.DRAFT_TEXT_EXTRA, getIntent().getStringExtra(ConversationActivity.DRAFT_TEXT_EXTRA));
      intent.putExtra(ConversationActivity.DRAFT_AUDIO_EXTRA, getIntent().getParcelableExtra(ConversationActivity.DRAFT_AUDIO_EXTRA));
      intent.putExtra(ConversationActivity.DRAFT_VIDEO_EXTRA, getIntent().getParcelableExtra(ConversationActivity.DRAFT_VIDEO_EXTRA));
      intent.putExtra(ConversationActivity.DRAFT_IMAGE_EXTRA, getIntent().getParcelableExtra(ConversationActivity.DRAFT_IMAGE_EXTRA));

      long existingThread = DatabaseFactory.getThreadDatabase(this).getThreadIdIfExistsFor(recipients);

      intent.putExtra(ConversationActivity.THREAD_ID_EXTRA, existingThread);
      intent.putExtra(ConversationActivity.DISTRIBUTION_TYPE_EXTRA, ThreadDatabase.DistributionTypes.DEFAULT);
      startActivity(intent);
      finish();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);

    switch (item.getItemId()) {
      case android.R.id.home: super.onBackPressed(); return true;
    }

    return false;
  }

}
