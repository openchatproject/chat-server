package com.openchat.secureim;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.openchat.secureim.database.model.ThreadRecord;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.util.DateUtils;
import com.openchat.secureim.util.Emoji;
import com.openchat.secureim.util.RecipientViewUtil;

import java.util.Locale;
import java.util.Set;

import static com.openchat.secureim.util.SpanUtil.color;

public class ConversationListItem extends RelativeLayout
                                  implements Recipient.RecipientModifiedListener
{
  private final static String TAG = ConversationListItem.class.getSimpleName();

  private final static Typeface BOLD_TYPEFACE  = Typeface.create("sans-serif", Typeface.BOLD);
  private final static Typeface LIGHT_TYPEFACE = Typeface.create("sans-serif-light", Typeface.NORMAL);

  private Context           context;
  private Set<Long>         selectedThreads;
  private Recipients        recipients;
  private long              threadId;
  private TextView          subjectView;
  private TextView          fromView;
  private TextView          dateView;
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

  public void set(ThreadRecord thread, Locale locale, Set<Long> selectedThreads, boolean batchMode) {
    this.selectedThreads  = selectedThreads;
    this.recipients       = thread.getRecipients();
    this.threadId         = thread.getThreadId();
    this.read             = thread.isRead();
    this.distributionType = thread.getDistributionType();

    this.recipients.addListener(this);
    this.fromView.setText(RecipientViewUtil.formatFrom(context, recipients, read));

    this.subjectView.setText(Emoji.getInstance(context).emojify(thread.getDisplayBody(),
                                                                Emoji.EMOJI_SMALL,
                                                                new Emoji.InvalidatingPageLoadedListener(subjectView)),
                             TextView.BufferType.SPANNABLE);
    this.subjectView.setTypeface(read ? LIGHT_TYPEFACE : BOLD_TYPEFACE);

    if (thread.getDate() > 0) {
      CharSequence date = DateUtils.getBriefRelativeTimeSpanString(context, locale, thread.getDate());
      dateView.setText(read ? date : color(getResources().getColor(R.color.openchatservice_primary), date));
      dateView.setTypeface(read ? LIGHT_TYPEFACE : BOLD_TYPEFACE);
    }

    setBackground(read, batchMode);
    RecipientViewUtil.setContactPhoto(context, contactPhotoImage, recipients.getPrimaryRecipient(), true);
  }

  public void unbind() {
    if (this.recipients != null)
      this.recipients.removeListener(this);
  }

  private void initializeContactWidgetVisibility() {
    contactPhotoImage.setVisibility(View.VISIBLE);
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
        ConversationListItem.this.fromView.setText(RecipientViewUtil.formatFrom(context, recipients, read));
        RecipientViewUtil.setContactPhoto(context, contactPhotoImage, recipients.getPrimaryRecipient(), true);
      }
    });
  }
}
