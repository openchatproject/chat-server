package com.openchat.secureim;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.openchat.secureim.crypto.MasterCipher;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.ThreadDatabase;
import com.openchat.secureim.database.model.ThreadRecord;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ConversationListAdapter extends CursorAdapter implements AbsListView.RecyclerListener {

  private final ThreadDatabase threadDatabase;
  private final MasterCipher   masterCipher;
  private final Context        context;
  private final LayoutInflater inflater;

  private final Set<Long> batchSet  = Collections.synchronizedSet(new HashSet<Long>());
  private       boolean   batchMode = false;

  public ConversationListAdapter(Context context, Cursor cursor, MasterSecret masterSecret) {
    super(context, cursor, 0);

    if (masterSecret != null) this.masterCipher = new MasterCipher(masterSecret);
    else                      this.masterCipher = null;

    this.context        = context;
    this.threadDatabase = DatabaseFactory.getThreadDatabase(context);
    this.inflater       = LayoutInflater.from(context);
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    return inflater.inflate(R.layout.conversation_list_item_view, parent, false);
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    if (masterCipher != null) {
      ThreadDatabase.Reader reader = threadDatabase.readerFor(cursor, masterCipher);
      ThreadRecord          record = reader.getCurrent();

      ((ConversationListItem)view).set(record, batchSet, batchMode);
    }
  }

  public void toggleThreadInBatchSet(long threadId) {
    if (batchSet.contains(threadId)) {
      batchSet.remove(threadId);
    } else {
      batchSet.add(threadId);
    }
  }

  public Set<Long> getBatchSelections() {
    return batchSet;
  }

  public void initializeBatchMode(boolean toggle) {
    this.batchMode = toggle;
    unselectAllThreads();
  }

  public void unselectAllThreads() {
    this.batchSet.clear();
    this.notifyDataSetChanged();
  }

  public void selectAllThreads() {
    Cursor cursor = DatabaseFactory.getThreadDatabase(context).getConversationList();

    try {
      while (cursor != null && cursor.moveToNext()) {
        this.batchSet.add(cursor.getLong(cursor.getColumnIndexOrThrow(ThreadDatabase.ID)));
      }
    } finally {
      if (cursor != null)
        cursor.close();
    }

    this.notifyDataSetChanged();
  }

  @Override
  public void onMovedToScrapHeap(View view) {
    ((ConversationListItem)view).unbind();
  }
}
