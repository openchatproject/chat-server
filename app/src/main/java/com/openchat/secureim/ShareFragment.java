package com.openchat.secureim;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.openchat.secureim.database.loaders.ConversationListLoader;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.crypto.MasterSecret;

public class ShareFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

  private ConversationSelectedListener listener;
  private MasterSecret masterSecret;

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    masterSecret = getArguments().getParcelable("master_secret");
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
    return inflater.inflate(R.layout.share_fragment, container, false);
  }

  @Override
  public void onActivityCreated(Bundle bundle) {
    super.onActivityCreated(bundle);

    initializeListAdapter();
    getLoaderManager().initLoader(0, null, this);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    this.listener = (ConversationSelectedListener) activity;
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    if (v instanceof ShareListItem) {
      ShareListItem headerView = (ShareListItem) v;

      handleCreateConversation(headerView.getThreadId(), headerView.getRecipients(),
                               headerView.getDistributionType());
    }
  }

  private void initializeListAdapter() {
    this.setListAdapter(new ShareListAdapter(getActivity(), null, masterSecret));
    getListView().setRecyclerListener((ShareListAdapter) getListAdapter());
    getLoaderManager().restartLoader(0, null, this);
  }

  private void handleCreateConversation(long threadId, Recipients recipients, int distributionType) {
    listener.onCreateConversation(threadId, recipients, distributionType);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
    return new ConversationListLoader(getActivity(), null);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
    ((CursorAdapter)getListAdapter()).changeCursor(cursor);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> arg0) {
    ((CursorAdapter)getListAdapter()).changeCursor(null);
  }

  public interface ConversationSelectedListener {
    public void onCreateConversation(long threadId, Recipients recipients, int distributionType);
  }
}
