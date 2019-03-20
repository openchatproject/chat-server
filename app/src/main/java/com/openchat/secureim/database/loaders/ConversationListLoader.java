package com.openchat.secureim.database.loaders;

import android.content.Context;
import android.database.Cursor;

import com.openchat.secureim.contacts.ContactAccessor;
import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.util.AbstractCursorLoader;

import java.util.List;

public class ConversationListLoader extends AbstractCursorLoader {

  private final String filter;

  public ConversationListLoader(Context context, String filter) {
    super(context);
    this.filter = filter;
  }

  @Override
  public Cursor getCursor() {
    if (filter != null && filter.trim().length() != 0) {
      List<String> numbers = ContactAccessor.getInstance()
          .getNumbersForThreadSearchFilter(filter, context.getContentResolver());

      return DatabaseFactory.getThreadDatabase(context).getFilteredConversationList(numbers);
    } else {
      return DatabaseFactory.getThreadDatabase(context).getConversationList();
    }
  }
}
