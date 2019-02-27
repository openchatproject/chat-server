package com.openchat.secureim;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.ThreadDatabase;
import com.openchat.secureim.database.model.ThreadRecord;
import com.openchat.imservice.crypto.MasterCipher;
import com.openchat.imservice.crypto.MasterSecret;

public class ShareListAdapter extends CursorAdapter implements AbsListView.RecyclerListener {

  private final ThreadDatabase threadDatabase;
  private final MasterCipher   masterCipher;
  private final Context        context;
  private final LayoutInflater inflater;

  public ShareListAdapter(Context context, Cursor cursor, MasterSecret masterSecret) {
    super(context, cursor, 0);

    if (masterSecret != null) this.masterCipher = new MasterCipher(masterSecret);
    else                      this.masterCipher = null;

    this.context        = context;
    this.threadDatabase = DatabaseFactory.getThreadDatabase(context);
    this.inflater       = LayoutInflater.from(context);
  }

  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    return inflater.inflate(R.layout.share_list_item_view, parent, false);
  }

  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    if (masterCipher != null) {
      ThreadDatabase.Reader reader = threadDatabase.readerFor(cursor, masterCipher);
      ThreadRecord          record = reader.getCurrent();

      ((ShareListItem)view).set(record);
    }
  }

  @Override
  public void onMovedToScrapHeap(View view) {
    ((ShareListItem)view).unbind();
  }
}
