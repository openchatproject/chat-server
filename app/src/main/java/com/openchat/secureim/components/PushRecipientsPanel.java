package com.openchat.secureim.components;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.openchat.secureim.R;
import com.openchat.secureim.contacts.ContactAccessor;
import com.openchat.secureim.contacts.RecipientsAdapter;
import com.openchat.secureim.contacts.RecipientsEditor;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.RecipientFormattingException;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.recipients.Recipients.RecipientsModifiedListener;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class PushRecipientsPanel extends RelativeLayout implements RecipientsModifiedListener {
  private final String                         TAG = PushRecipientsPanel.class.getSimpleName();
  private       RecipientsPanelChangedListener panelChangeListener;

  private RecipientsEditor recipientsText;
  private View             panel;

  private static final int RECIPIENTS_MAX_LENGTH = 312;

  public PushRecipientsPanel(Context context) {
    super(context);
    initialize();
  }

  public PushRecipientsPanel(Context context, AttributeSet attrs) {
    super(context, attrs);
    initialize();
  }

  public PushRecipientsPanel(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initialize();
  }

  public void addRecipient(String name, String number) {
    if (name != null) recipientsText.append(name + "< " + number + ">, ");
    else recipientsText.append(number + ", ");
  }

  public void addRecipients(Recipients recipients) {
    List<Recipient> recipientList = recipients.getRecipientsList();
    Iterator<Recipient> iterator = recipientList.iterator();

    while (iterator.hasNext()) {
      Recipient recipient = iterator.next();
      addRecipient(recipient.getName(), recipient.getNumber());
    }
  }

  public Recipients getRecipients() throws RecipientFormattingException {
    String rawText = recipientsText.getText().toString();
    Recipients recipients = RecipientFactory.getRecipientsFromString(getContext(), rawText, true);

    if (recipients.isEmpty())
      throw new RecipientFormattingException("Recipient List Is Empty!");

    return recipients;
  }

  public void disable() {
    recipientsText.setText("");
    panel.setVisibility(View.GONE);
  }

  public void setPanelChangeListener(RecipientsPanelChangedListener panelChangeListener) {
    this.panelChangeListener = panelChangeListener;
  }

  private void initialize() {
    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    inflater.inflate(R.layout.push_recipients_panel, this, true);

    View imageButton = findViewById(R.id.contacts_button);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
      ((MarginLayoutParams) imageButton.getLayoutParams()).topMargin = 0;

    panel = findViewById(R.id.recipients_panel);
    initRecipientsEditor();
  }

  private void initRecipientsEditor() {
    Recipients recipients;
    recipientsText = (RecipientsEditor)findViewById(R.id.recipients_text);

    try {
      recipients = getRecipients();
    } catch (RecipientFormattingException e) {
      recipients = RecipientFactory.getRecipientsFor(getContext(), new LinkedList<Recipient>(), true);
    }
    recipients.addListener(this);

    recipientsText.setAdapter(new RecipientsAdapter(this.getContext()));
    recipientsText.populate(recipients);

    recipientsText.setOnFocusChangeListener(new FocusChangedListener());
    recipientsText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (panelChangeListener != null) {
          try {
            panelChangeListener.onRecipientsPanelUpdate(getRecipients());
          } catch (RecipientFormattingException rfe) {
            panelChangeListener.onRecipientsPanelUpdate(null);
          }
        }
        recipientsText.setText("");
      }
    });
  }

  @Override public void onModified(Recipients recipients) {
    recipientsText.populate(recipients);
  }

  private class FocusChangedListener implements View.OnFocusChangeListener {
    public void onFocusChange(View v, boolean hasFocus) {
      if (!hasFocus && (panelChangeListener != null)) {
        try {
          panelChangeListener.onRecipientsPanelUpdate(getRecipients());
        } catch (RecipientFormattingException rfe) {
          panelChangeListener.onRecipientsPanelUpdate(null);
        }
      }
    }
  }

  public interface RecipientsPanelChangedListener {
    public void onRecipientsPanelUpdate(Recipients recipients);
  }

}
