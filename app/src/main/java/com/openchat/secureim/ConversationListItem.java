package com.openchat.secureim;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Contacts.Intents;
import android.provider.ContactsContract.QuickContact;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.openchat.secureim.database.DatabaseFactory;
import com.openchat.secureim.database.model.ThreadRecord;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.util.DateUtils;
import com.openchat.secureim.util.Emoji;

import java.util.Set;
import java.util.concurrent.ThreadFactory;

public class ConversationListItem extends RelativeLayout
                                  implements Recipient.RecipientModifiedListener
{
  private final static String TAG = ConversationListItem.class.getSimpleName();

  private Context           context;
  private Set<Long>         selectedThreads;
  private Recipients        recipients;
  private long              threadId;
  private TextView          subjectView;
  private TextView          fromView;
  private TextView          dateView;
  private long              count;
  private boolean           read;

  private ImageView         contactPhotoImage;

  private final Handler handler = new Handler();
  private int distributionType;

  public ConversationListItem(Context context) {
    super(context);
    this.context = context;
  }

  public ConversationListItem(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
  }

  @Override
  protected void onFinishInflate() {
    this.subjectView       = (TextView) findViewById(R.id.subject);
    this.fromView          = (TextView) findViewById(R.id.from);
    this.dateView          = (TextView) findViewById(R.id.date);

    this.contactPhotoImage = (ImageView) findViewById(R.id.contact_photo_image);

    initializeContactWidgetVisibility();
  }

  public void set(ThreadRecord thread, Set<Long> selectedThreads, boolean batchMode) {
    this.selectedThreads  = selectedThreads;
    this.recipients       = thread.getRecipients();
    this.threadId         = thread.getThreadId();
    this.count            = thread.getCount();
    this.read             = thread.isRead();
    this.distributionType = thread.getDistributionType();

    this.recipients.addListener(this);
    this.fromView.setText(formatFrom(recipients, count, read));

    this.subjectView.setText(Emoji.getInstance(context).emojify(thread.getDisplayBody(),
                                                                Emoji.EMOJI_SMALL,
                                                                new Emoji.InvalidatingPageLoadedListener(subjectView)),
                             TextView.BufferType.SPANNABLE);

    if (thread.getDate() > 0)
      this.dateView.setText(DateUtils.getBetterRelativeTimeSpanString(getContext(), thread.getDate()));

    setBackground(read, batchMode);
    setContactPhoto(this.recipients.getPrimaryRecipient());
  }

  public void unbind() {
    if (this.recipients != null)
      this.recipients.removeListener(this);
  }

  private void initializeContactWidgetVisibility() {
    contactPhotoImage.setVisibility(View.VISIBLE);
  }

  private void setContactPhoto(final Recipient recipient) {
    if (recipient == null) return;

    contactPhotoImage.setImageBitmap(recipient.getContactPhoto());

    if (!recipient.isGroupRecipient()) {
      contactPhotoImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (recipient.getContactUri() != null) {
            QuickContact.showQuickContact(context, contactPhotoImage, recipient.getContactUri(), QuickContact.MODE_LARGE, null);
          } else {
            Intent intent = new Intent(Intents.SHOW_OR_CREATE_CONTACT,  Uri.fromParts("tel", recipient.getNumber(), null));
            context.startActivity(intent);
          }
        }
      });
    } else {
      contactPhotoImage.setOnClickListener(null);
    }
  }

  private void setBackground(boolean read, boolean batch) {
    int[]      attributes = new int[]{R.attr.conversation_list_item_background_selected,
                                      R.attr.conversation_list_item_background_read,
                                      R.attr.conversation_list_item_background_unread};

    TypedArray drawables  = context.obtainStyledAttributes(attributes);

    if (batch && selectedThreads.contains(threadId)) {
      setBackgroundDrawable(drawables.getDrawable(0));
    } else if (read) {
      setBackgroundDrawable(drawables.getDrawable(1));
    } else {
      setBackgroundDrawable(drawables.getDrawable(2));
    }

    drawables.recycle();
  }

  private CharSequence formatFrom(Recipients from, long count, boolean read) {
    int attributes[]  = new int[] {R.attr.conversation_list_item_count_color};
    TypedArray colors = context.obtainStyledAttributes(attributes);

    final String fromString;
    final boolean isUnnamedGroup = from.isGroupRecipient() && TextUtils.isEmpty(from.getPrimaryRecipient().getName());
    if (isUnnamedGroup) {
      fromString = context.getString(R.string.ConversationActivity_unnamed_group);
    } else {
      fromString = from.toShortString();
    }
    SpannableStringBuilder builder = new SpannableStringBuilder(fromString);

    final int typeface;
    if (isUnnamedGroup) {
      if (!read) typeface = Typeface.BOLD_ITALIC;
      else       typeface = Typeface.ITALIC;
    } else if (!read) {
      typeface = Typeface.BOLD;
    } else {
      typeface = Typeface.NORMAL;
    }

    builder.setSpan(new StyleSpan(typeface), 0, builder.length(),
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

    colors.recycle();
    return builder;
  }

  public Recipients getRecipients() {
    return recipients;
  }

  public long getThreadId() {
    return threadId;
  }

  public int getDistributionType() {
    return distributionType;
  }

  @Override
  public void onModified(Recipient recipient) {
    handler.post(new Runnable() {
      @Override
      public void run() {
        ConversationListItem.this.fromView.setText(formatFrom(recipients, count, read));
        setContactPhoto(ConversationListItem.this.recipients.getPrimaryRecipient());
      }
    });
  }
}
