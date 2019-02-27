package com.openchat.secureim;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

import com.openchat.secureim.contacts.ContactAccessor;
import com.openchat.secureim.contacts.ContactAccessor.ContactData;
import com.openchat.secureim.contacts.ContactAccessor.GroupData;
import com.openchat.secureim.contacts.ContactAccessor.NumberData;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.Recipients;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ContactSelectionGroupsFragment extends SherlockListFragment
    implements LoaderManager.LoaderCallbacks<Cursor>
{

  private final HashMap<Long, GroupData> selectedGroups = new HashMap<Long, GroupData>();

  @Override
  public void onActivityCreated(Bundle icicle) {
    super.onActivityCreated(icicle);

    initializeResources();
    initializeCursor();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.contact_selection_group_activity, container, false);
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    ((GroupItemView)v).selected();
  }

  private void initializeCursor() {
    setListAdapter(new GroupSelectionListAdapter(getActivity(), null));
    this.getLoaderManager().initLoader(0, null, this);
  }

  private void initializeResources() {
    this.getListView().setFocusable(true);
  }

  public List<ContactData> getSelectedContacts(Context context) {
    List<ContactData> contacts = new LinkedList<ContactData>();

    for (GroupData groupData : selectedGroups.values()) {
      List<ContactData> contactDataList = ContactAccessor.getInstance()
                                                         .getGroupMembership(context, groupData.id);

      contacts.addAll(contactDataList);
    }

    return contacts;
  }

  private void addGroup(GroupData groupData) {
    selectedGroups.put(groupData.id, groupData);
  }

  private void removeGroup(GroupData groupData) {
    selectedGroups.remove(groupData.id);
  }

  private class GroupSelectionListAdapter extends CursorAdapter {

    public GroupSelectionListAdapter(Context context, Cursor cursor) {
      super(context, cursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
      GroupItemView view = new GroupItemView(context);
      bindView(view, context, cursor);
      return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
      GroupData groupData = ContactAccessor.getInstance().getGroupData(getActivity(), cursor);
      ((GroupItemView)view).set(groupData);
    }
  }

  private class GroupItemView extends LinearLayout {
    private GroupData groupData;
    private CheckedTextView name;

    public GroupItemView(Context context) {
      super(context);

      LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      li.inflate(R.layout.contact_selection_group_item, this, true);

      this.name   = (CheckedTextView)findViewById(R.id.name);
    }

    public void selected() {
      name.toggle();

      if (name.isChecked()) {
        addGroup(groupData);
      } else {
        removeGroup(groupData);
      }
    }

    public void set(GroupData groupData) {
      this.groupData = groupData;

      if (selectedGroups.containsKey(groupData.id))
        this.name.setChecked(true);
      else
        this.name.setChecked(false);

      this.name.setText(groupData.name);
    }
  }

  @Override
  public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
    return ContactAccessor.getInstance().getCursorLoaderForContactGroups(getActivity());
  }

  @Override
  public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
    ((CursorAdapter)getListAdapter()).changeCursor(cursor);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> arg0) {
    ((CursorAdapter)getListAdapter()).changeCursor(null);
  }
}
