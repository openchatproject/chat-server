package com.openchat.secureim;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;

import com.openchat.secureim.crypto.MasterCipher;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.CursorRecyclerViewAdapter;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.ThreadDatabase;
import com.openchat.secureim.database.model.ThreadRecord;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class ConversationListAdapter extends CursorRecyclerViewAdapter<ConversationListAdapter.ViewHolder> {

  private final ThreadDatabase    threadDatabase;
  private final MasterCipher      masterCipher;
  private final Locale            locale;
  private final Context           context;
  private final LayoutInflater    inflater;
  private final ItemClickListener clickListener;

  private final Set<Long> batchSet  = Collections.synchronizedSet(new HashSet<Long>());
  private       boolean   batchMode = false;

  protected static class ViewHolder extends RecyclerView.ViewHolder {
    public ViewHolder(final @NonNull ConversationListItem itemView,
                      final @Nullable ItemClickListener clickListener) {
      super(itemView);
      itemView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          if (clickListener != null) clickListener.onItemClick(itemView);
        }
      });
      itemView.setOnLongClickListener(new OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
          if (clickListener != null) clickListener.onItemLongClick(itemView);
          return true;
        }
      });
    }

    public ConversationListItem getItem() {
      return (ConversationListItem) itemView;
    }
  }

  public ConversationListAdapter(@NonNull Context context,
                                 @NonNull MasterSecret masterSecret,
                                 @NonNull Locale locale,
                                 @Nullable Cursor cursor,
                                 @Nullable ItemClickListener clickListener) {
    super(context, cursor);
    this.masterCipher   = new MasterCipher(masterSecret);
    this.context        = context;
    this.threadDatabase = DatabaseFactory.getThreadDatabase(context);
    this.locale         = locale;
    this.inflater       = LayoutInflater.from(context);
    this.clickListener  = clickListener;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder((ConversationListItem)inflater.inflate(R.layout.conversation_list_item_view,
                                                                 parent, false), clickListener);
  }

  @Override public void onViewRecycled(ViewHolder holder) {
    holder.getItem().unbind();
  }

  @Override
  public void onBindViewHolder(ViewHolder viewHolder, @NonNull Cursor cursor) {
    ThreadDatabase.Reader reader = threadDatabase.readerFor(cursor, masterCipher);
    ThreadRecord          record = reader.getCurrent();

    viewHolder.getItem().set(record, locale, batchSet, batchMode);
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
    for (int i = 0; i < getItemCount(); i++) {
      batchSet.add(getItemId(i));
    }
    this.notifyDataSetChanged();
  }

  public interface ItemClickListener {
    void onItemClick(ConversationListItem item);
    void onItemLongClick(ConversationListItem item);
  }
}
