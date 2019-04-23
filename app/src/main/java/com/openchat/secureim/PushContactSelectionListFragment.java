package com.openchat.secureim;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.openchat.secureim.contacts.ContactAccessor;
import com.openchat.secureim.contacts.ContactAccessor.ContactData;
import com.openchat.secureim.contacts.ContactSelectionListAdapter;
import com.openchat.secureim.contacts.ContactSelectionListAdapter.ViewHolder;
import com.openchat.secureim.contacts.ContactSelectionListAdapter.DataHolder;
import com.openchat.secureim.contacts.ContactsDatabase;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class PushContactSelectionListFragment extends    Fragment
                                              implements LoaderManager.LoaderCallbacks<Cursor>
{
  private static final String TAG = "ContactSelectFragment";

  private TextView emptyText;

  private Map<Long, ContactData>    selectedContacts;
  private OnContactSelectedListener onContactSelectedListener;
  private boolean                   multi = false;
  private StickyListHeadersListView listView;
  private EditText                  filterEditText;
  private String                    cursorFilter;

  @Override
  public void onActivityCreated(Bundle icicle) {
    super.onCreate(icicle);
    initializeResources();
    initializeCursor();
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  @Override
  public void onPause() {
    super.onPause();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.push_contact_selection_list_activity, container, false);
  }

  public List<ContactData> getSelectedContacts() {
    if (selectedContacts == null) return null;

    List<ContactData> selected = new LinkedList<ContactData>();
    selected.addAll(selectedContacts.values());

    return selected;
  }

  public void setMultiSelect(boolean multi) {
    this.multi = multi;
  }

  private void addContact(DataHolder data) {
    final ContactData contactData = new ContactData(data.id, data.name);
    final CharSequence label = ContactsContract.CommonDataKinds.Phone.getTypeLabel(getResources(),
                                                                                   data.numberType, "");
    contactData.numbers.add(new ContactAccessor.NumberData(label.toString(), data.number));
    if (multi) {
      selectedContacts.put(contactData.id, contactData);
    }
    if (onContactSelectedListener != null) {
      onContactSelectedListener.onContactSelected(contactData);
    }
  }

  private void removeContact(DataHolder contactData) {
    selectedContacts.remove(contactData.id);
  }

  private void initializeCursor() {
    ContactSelectionListAdapter adapter = new ContactSelectionListAdapter(getActivity(), null, multi);
    selectedContacts = adapter.getSelectedContacts();
    listView.setAdapter(adapter);
    this.getLoaderManager().initLoader(0, null, this);
  }

  private void initializeResources() {
    emptyText = (TextView) getView().findViewById(android.R.id.empty);
    listView  = (StickyListHeadersListView) getView().findViewById(android.R.id.list);
    listView.setFocusable(true);
    listView.setFastScrollEnabled(true);
    listView.setDrawingListUnderStickyHeader(false);
    listView.setOnItemClickListener(new ListClickListener());
    filterEditText = (EditText) getView().findViewById(R.id.filter);
    filterEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        cursorFilter = charSequence.toString();
        getLoaderManager().restartLoader(0, null, PushContactSelectionListFragment.this);
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });
    cursorFilter = null;
  }

  public void update() {
    this.getLoaderManager().restartLoader(0, null, this);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    if (getActivity().getIntent().getBooleanExtra(PushContactSelectionActivity.PUSH_ONLY_EXTRA, false)) {
      return ContactAccessor.getInstance().getCursorLoaderForPushContacts(getActivity(), cursorFilter);
    } else {
      return ContactAccessor.getInstance().getCursorLoaderForContacts(getActivity(), cursorFilter);
    }
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    ((CursorAdapter) listView.getAdapter()).changeCursor(data);
    emptyText.setText(R.string.contact_selection_group_activity__no_contacts);
    if (data != null && data.getCount() < 40) listView.setFastScrollAlwaysVisible(false);
    else                                      listView.setFastScrollAlwaysVisible(true);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    ((CursorAdapter) listView.getAdapter()).changeCursor(null);
  }

  private class ListClickListener implements AdapterView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
      final DataHolder contactData = (DataHolder) v.getTag(R.id.contact_info_tag);
      final ViewHolder holder      = (ViewHolder) v.getTag(R.id.holder_tag);

      if (holder == null) {
        Log.w(TAG, "ViewHolder was null, can't proceed with click logic.");
        return;
      }

      if (multi) holder.checkBox.toggle();

      if (!multi || holder.checkBox.isChecked()) {
        addContact(contactData);
      } else if (multi) {
        removeContact(contactData);
      }
    }
  }

  public void setOnContactSelectedListener(OnContactSelectedListener onContactSelectedListener) {
    this.onContactSelectedListener = onContactSelectedListener;
  }

  public interface OnContactSelectedListener {
    public void onContactSelected(ContactData contactData);
  }
}
