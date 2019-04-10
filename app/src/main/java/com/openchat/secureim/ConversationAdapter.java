package com.openchat.secureim;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.support.v4.widget.CursorAdapter;

import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.MmsSmsColumns;
import com.openchat.secureim.database.MmsSmsDatabase;
import com.openchat.secureim.database.SmsDatabase;
import com.openchat.secureim.database.model.MessageRecord;
import com.openchat.secureim.util.LRUCache;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.openchat.secureim.ConversationFragment.SelectionClickListener;

public class ConversationAdapter extends CursorAdapter implements AbsListView.RecyclerListener {

  private static final int MAX_CACHE_SIZE = 40;
  private final Map<String,SoftReference<MessageRecord>> messageRecordCache =
      Collections.synchronizedMap(new LRUCache<String, SoftReference<MessageRecord>>(MAX_CACHE_SIZE));

  public static final int MESSAGE_TYPE_OUTGOING = 0;
  public static final int MESSAGE_TYPE_INCOMING = 1;
  public static final int MESSAGE_TYPE_GROUP_ACTION = 2;

  private final Set<MessageRecord> batchSelected = Collections.synchronizedSet(new HashSet<MessageRecord>());

  private final SelectionClickListener selectionClickListener;
  private final Context                context;
  private final MasterSecret           masterSecret;
  private final boolean                groupThread;
  private final boolean                pushDestination;
  private final LayoutInflater         inflater;

  public ConversationAdapter(Context context, MasterSecret masterSecret, SelectionClickListener selectionClickListener,
                             boolean groupThread, boolean pushDestination)
  {
    super(context, null, 0);
    this.context                = context;
    this.masterSecret           = masterSecret;
    this.selectionClickListener = selectionClickListener;
    this.groupThread            = groupThread;
    this.pushDestination        = pushDestination;
    this.inflater               = LayoutInflater.from(context);
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    ConversationItem item       = (ConversationItem)view;
    long id                     = cursor.getLong(cursor.getColumnIndexOrThrow(SmsDatabase.ID));
    String type                 = cursor.getString(cursor.getColumnIndexOrThrow(MmsSmsDatabase.TRANSPORT));
    MessageRecord messageRecord = getMessageRecord(id, cursor, type);

    item.set(masterSecret, messageRecord, batchSelected, selectionClickListener,
             groupThread, pushDestination);
  }

  @Override
  public void changeCursor(Cursor cursor) {
    messageRecordCache.clear();
    super.changeCursor(cursor);
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    View view;

    int type = getItemViewType(cursor);

    switch (type) {
      case ConversationAdapter.MESSAGE_TYPE_OUTGOING:
        view = inflater.inflate(R.layout.conversation_item_sent, parent, false);
        break;
      case ConversationAdapter.MESSAGE_TYPE_INCOMING:
        view = inflater.inflate(R.layout.conversation_item_received, parent, false);
        break;
      case ConversationAdapter.MESSAGE_TYPE_GROUP_ACTION:
        view = inflater.inflate(R.layout.conversation_item_activity, parent, false);
        break;
      default: throw new IllegalArgumentException("unsupported item view type given to ConversationAdapter");
    }

    return view;
  }

  @Override
  public int getViewTypeCount() {
    return 3;
  }

  @Override
  public int getItemViewType(int position) {
    Cursor cursor = (Cursor)getItem(position);
    return getItemViewType(cursor);
  }

  private int getItemViewType(Cursor cursor) {
    long id                     = cursor.getLong(cursor.getColumnIndexOrThrow(MmsSmsColumns.ID));
    String type                 = cursor.getString(cursor.getColumnIndexOrThrow(MmsSmsDatabase.TRANSPORT));
    MessageRecord messageRecord = getMessageRecord(id, cursor, type);

    if      (messageRecord.isGroupAction()) return MESSAGE_TYPE_GROUP_ACTION;
    else if (messageRecord.isOutgoing())    return MESSAGE_TYPE_OUTGOING;
    else                                    return MESSAGE_TYPE_INCOMING;
  }

  private MessageRecord getMessageRecord(long messageId, Cursor cursor, String type) {
    SoftReference<MessageRecord> reference = messageRecordCache.get(type + messageId);

    if (reference != null) {
      MessageRecord record = reference.get();

      if (record != null)
        return record;
    }

    MmsSmsDatabase.Reader reader = DatabaseFactory.getMmsSmsDatabase(context)
                                                  .readerFor(cursor, masterSecret);

    MessageRecord messageRecord = reader.getCurrent();

    messageRecordCache.put(type + messageId, new SoftReference<MessageRecord>(messageRecord));

    return messageRecord;
  }

  public void close() {
    this.getCursor().close();
  }

  public void toggleBatchSelected(MessageRecord messageRecord) {
    if (batchSelected.contains(messageRecord)) {
      batchSelected.remove(messageRecord);
    } else {
      batchSelected.add(messageRecord);
    }
  }

  public Set<MessageRecord> getBatchSelected() {
    return batchSelected;
  }

  @Override
  public void onMovedToScrapHeap(View view) {
    ((ConversationItem)view).unbind();
  }
}
