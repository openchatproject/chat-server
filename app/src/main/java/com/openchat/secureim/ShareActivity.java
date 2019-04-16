package com.openchat.secureim;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.util.DynamicLanguage;
import com.openchat.secureim.util.DynamicTheme;

import ws.com.google.android.mms.ContentType;

public class ShareActivity extends PassphraseRequiredActionBarActivity
    implements ShareFragment.ConversationSelectedListener
{
  private final DynamicTheme    dynamicTheme    = new DynamicTheme   ();
  private final DynamicLanguage dynamicLanguage = new DynamicLanguage();

  @Override
  protected void onPreCreate() {
    dynamicTheme.onCreate(this);
    dynamicLanguage.onCreate(this);
  }

  @Override
  protected void onCreate(Bundle icicle, @NonNull MasterSecret masterSecret) {
    setContentView(R.layout.share_activity);
    initFragment(R.id.drawer_layout, new ShareFragment(), masterSecret);
  }

  @Override
  protected void onNewIntent(Intent intent) {
      super.onNewIntent(intent);
      setIntent(intent);
  }

  @Override
  public void onResume() {
    super.onResume();
    dynamicTheme.onResume(this);
    dynamicLanguage.onResume(this);
    getSupportActionBar().setTitle(R.string.ShareActivity_share_with);
  }

  @Override
  public void onPause() {
    super.onPause();
    if (!isFinishing()) finish();
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    MenuInflater inflater = this.getMenuInflater();
    menu.clear();

    inflater.inflate(R.menu.share, menu);
    super.onPrepareOptionsMenu(menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);
    switch (item.getItemId()) {
    case R.id.menu_new_message: handleNewConversation(); return true;
    case android.R.id.home:     finish();                return true;
    }
    return false;
  }

  private void handleNewConversation() {
    Intent intent = getBaseShareIntent(NewConversationActivity.class);
    startActivity(intent);
  }

  @Override
  public void onCreateConversation(long threadId, Recipients recipients, int distributionType) {
    createConversation(threadId, recipients, distributionType);
  }

  private void createConversation(long threadId, Recipients recipients, int distributionType) {
    final Intent intent = getBaseShareIntent(ConversationActivity.class);
    intent.putExtra(ConversationActivity.RECIPIENTS_EXTRA, recipients.getIds());
    intent.putExtra(ConversationActivity.THREAD_ID_EXTRA, threadId);
    intent.putExtra(ConversationActivity.DISTRIBUTION_TYPE_EXTRA, distributionType);

    startActivity(intent);
  }

  private Intent getBaseShareIntent(final Class<?> target) {
    final Intent intent      = new Intent(this, target);
    final String textExtra   = getIntent().getStringExtra(ConversationActivity.DRAFT_TEXT_EXTRA);
    final Uri    streamExtra = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
    final String type        = streamExtra != null ? getMimeType(streamExtra) : getIntent().getType();

    if (ContentType.isImageType(type)) {
      intent.putExtra(ConversationActivity.DRAFT_IMAGE_EXTRA, streamExtra);
    } else if (ContentType.isAudioType(type)) {
      intent.putExtra(ConversationActivity.DRAFT_AUDIO_EXTRA, streamExtra);
    } else if (ContentType.isVideoType(type)) {
      intent.putExtra(ConversationActivity.DRAFT_VIDEO_EXTRA, streamExtra);
    }
    intent.putExtra(ConversationActivity.DRAFT_TEXT_EXTRA, textExtra);

    return intent;
  }

  private String getMimeType(Uri uri) {
    String type = getContentResolver().getType(uri);

    if (type == null) {
      String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
      type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

    return type;
  }
}
