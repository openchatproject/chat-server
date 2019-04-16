package com.openchat.secureim;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.EncryptingSmsDatabase;
import com.openchat.secureim.database.MmsDatabase;
import com.openchat.secureim.database.MmsSmsDatabase;
import com.openchat.secureim.database.SmsDatabase;
import com.openchat.secureim.database.loaders.MessageDetailsLoader;
import com.openchat.secureim.database.model.MessageRecord;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.util.DateUtils;
import com.openchat.secureim.util.DirectoryHelper;
import com.openchat.secureim.util.DynamicLanguage;
import com.openchat.secureim.util.DynamicTheme;
import com.openchat.secureim.util.GroupUtil;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.LinkedList;

public class MessageDetailsActivity extends PassphraseRequiredActionBarActivity implements LoaderCallbacks<Cursor> {
  private final static String TAG = MessageDetailsActivity.class.getSimpleName();

  public final static String MASTER_SECRET_EXTRA = "master_secret";
  public final static String MESSAGE_ID_EXTRA    = "message_id";
  public final static String IS_PUSH_GROUP_EXTRA = "is_push_group";
  public final static String TYPE_EXTRA          = "type";

  private MasterSecret     masterSecret;
  private boolean          isPushGroup;
  private ConversationItem conversationItem;
  private ViewGroup        itemParent;
  private View             metadataContainer;
  private TextView         errorText;
  private TextView         sentDate;
  private TextView         receivedDate;
  private View             receivedContainer;
  private TextView         transport;
  private TextView         toFrom;
  private ListView         recipientsList;
  private LayoutInflater   inflater;

  private DynamicTheme     dynamicTheme    = new DynamicTheme();
  private DynamicLanguage  dynamicLanguage = new DynamicLanguage();

  @Override
  protected void onPreCreate() {
    dynamicTheme.onCreate(this);
    dynamicLanguage.onCreate(this);
  }

  @Override
  public void onCreate(Bundle bundle, @NonNull MasterSecret masterSecret) {
    setContentView(R.layout.message_details_activity);

    initializeResources();

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportLoaderManager().initLoader(0, null, this);
  }

  @Override
  protected void onResume() {
    super.onResume();
    dynamicTheme.onResume(this);
    dynamicLanguage.onResume(this);
  }

  private void initializeResources() {
    inflater       = LayoutInflater.from(this);
    View header = inflater.inflate(R.layout.message_details_header, recipientsList, false);

    masterSecret      = getIntent().getParcelableExtra(MASTER_SECRET_EXTRA);
    isPushGroup       = getIntent().getBooleanExtra(IS_PUSH_GROUP_EXTRA, false);
    itemParent        = (ViewGroup) findViewById(R.id.item_container );
    recipientsList    = (ListView ) findViewById(R.id.recipients_list);
    metadataContainer =             header.findViewById(R.id.metadata_container);
    errorText         = (TextView ) header.findViewById(R.id.error_text);
    sentDate          = (TextView ) header.findViewById(R.id.sent_time);
    receivedContainer =             header.findViewById(R.id.received_container);
    receivedDate      = (TextView ) header.findViewById(R.id.received_time);
    transport         = (TextView ) header.findViewById(R.id.transport);
    toFrom            = (TextView ) header.findViewById(R.id.tofrom);
    recipientsList.setHeaderDividersEnabled(false);
    recipientsList.addHeaderView(header, null, false);
  }

  private void updateTransport(MessageRecord messageRecord) {
    final String transportText;
    if (messageRecord.isOutgoing() && messageRecord.isFailed()) {
      transportText = "-";
    } else if (messageRecord.isPending()) {
      transportText = getString(R.string.ConversationFragment_pending);
    } else if (messageRecord.isPush()) {
      transportText = getString(R.string.ConversationFragment_push);
    } else if (messageRecord.isMms()) {
      transportText = getString(R.string.ConversationFragment_mms);
    } else {
      transportText = getString(R.string.ConversationFragment_sms);
    }

    transport.setText(transportText);
  }

  private void updateTime(MessageRecord messageRecord) {
    if (messageRecord.isPending() || messageRecord.isFailed()) {
      sentDate.setText("-");
      receivedContainer.setVisibility(View.GONE);
    } else {
      SimpleDateFormat dateFormatter = DateUtils.getDetailedDateFormatter(this);
      sentDate.setText(dateFormatter.format(new Date(messageRecord.getDateSent())));

      if (messageRecord.getDateReceived() != messageRecord.getDateSent() && !messageRecord.isOutgoing()) {
        receivedDate.setText(dateFormatter.format(new Date(messageRecord.getDateReceived())));
        receivedContainer.setVisibility(View.VISIBLE);
      } else {
        receivedContainer.setVisibility(View.GONE);
      }
    }
  }

  private void updateRecipients(MessageRecord messageRecord, Recipients recipients) {
    final int toFromRes;
    if (messageRecord.isMms() && !messageRecord.isPush() && !messageRecord.isOutgoing()) {
      toFromRes = R.string.message_details_header__with;
    } else if (messageRecord.isOutgoing()) {
      toFromRes = R.string.message_details_header__to;
    } else {
      toFromRes = R.string.message_details_header__from;
    }
    toFrom.setText(toFromRes);
    conversationItem.set(masterSecret, messageRecord, new HashSet<MessageRecord>(), new NullSelectionListener(),
                         recipients != messageRecord.getRecipients(),
                         DirectoryHelper.isPushDestination(this, recipients));
    recipientsList.setAdapter(new MessageDetailsRecipientAdapter(this, masterSecret, messageRecord,
                                                                 recipients, isPushGroup));
  }

  private void inflateMessageViewIfAbsent(MessageRecord messageRecord) {
    if (conversationItem == null) {
      if (messageRecord.isGroupAction()) {
        conversationItem = (ConversationItem) inflater.inflate(R.layout.conversation_item_activity, itemParent, false);
      } else if (messageRecord.isOutgoing()) {
        conversationItem = (ConversationItem) inflater.inflate(R.layout.conversation_item_sent, itemParent, false);
      } else {
        conversationItem = (ConversationItem) inflater.inflate(R.layout.conversation_item_received, itemParent, false);
      }
      itemParent.addView(conversationItem);
    }
  }

  private MessageRecord getMessageRecord(Context context, Cursor cursor, String type) {
    switch (type) {
      case MmsSmsDatabase.SMS_TRANSPORT:
        EncryptingSmsDatabase smsDatabase = DatabaseFactory.getEncryptingSmsDatabase(context);
        SmsDatabase.Reader    reader      = smsDatabase.readerFor(masterSecret, cursor);
        return reader.getNext();
      case MmsSmsDatabase.MMS_TRANSPORT:
        MmsDatabase        mmsDatabase = DatabaseFactory.getMmsDatabase(context);
        MmsDatabase.Reader mmsReader   = mmsDatabase.readerFor(masterSecret, cursor);
        return mmsReader.getNext();
      default:
        throw new AssertionError("no valid message type specified");
    }
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return new MessageDetailsLoader(this, getIntent().getStringExtra(TYPE_EXTRA),
                                    getIntent().getLongExtra(MESSAGE_ID_EXTRA, -1));
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
    final MessageRecord messageRecord = getMessageRecord(this, cursor, getIntent().getStringExtra(TYPE_EXTRA));
    new MessageRecipientAsyncTask(this, messageRecord).execute();
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    recipientsList.setAdapter(null);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);

    switch (item.getItemId()) {
      case android.R.id.home: finish(); return true;
    }

    return false;
  }

  private class MessageRecipientAsyncTask extends AsyncTask<Void,Void,Recipients> {
    private WeakReference<Context> weakContext;
    private MessageRecord          messageRecord;

    public MessageRecipientAsyncTask(Context context, MessageRecord messageRecord) {
      this.weakContext   = new WeakReference<>(context);
      this.messageRecord = messageRecord;
    }

    protected Context getContext() {
      return weakContext.get();
    }

    @Override
    public Recipients doInBackground(Void... voids) {
      Context context = getContext();
      if (context == null) {
        Log.w(TAG, "associated context is destroyed, finishing early");
      }

      Recipients recipients;

      final Recipients intermediaryRecipients;
      if (messageRecord.isMms()) {
        intermediaryRecipients = DatabaseFactory.getMmsAddressDatabase(context).getRecipientsForId(messageRecord.getId());
      } else {
        intermediaryRecipients = messageRecord.getRecipients();
      }

      if (!intermediaryRecipients.isGroupRecipient()) {
        Log.w(TAG, "Recipient is not a group, resolving members immediately.");
        recipients = intermediaryRecipients;
      } else {
        try {
          String groupId = intermediaryRecipients.getPrimaryRecipient().getNumber();
          recipients = DatabaseFactory.getGroupDatabase(context)
                                      .getGroupMembers(GroupUtil.getDecodedId(groupId), false);
        } catch (IOException e) {
          Log.w(TAG, e);
         recipients = new Recipients(new LinkedList<Recipient>());
        }
      }

      return recipients;
    }

    @Override
    public void onPostExecute(Recipients recipients) {
      if (getContext() == null) {
        Log.w(TAG, "AsyncTask finished with a destroyed context, leaving early.");
        return;
      }

      inflateMessageViewIfAbsent(messageRecord);

      updateRecipients(messageRecord, recipients);
      if (messageRecord.isFailed()) {
        errorText.setVisibility(View.VISIBLE);
        metadataContainer.setVisibility(View.GONE);
      } else {
        updateTransport(messageRecord);
        updateTime(messageRecord);
        errorText.setVisibility(View.GONE);
        metadataContainer.setVisibility(View.VISIBLE);
      }
    }
  }

  private static class NullSelectionListener implements ConversationFragment.SelectionClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
      return false;
    }
  }
}
