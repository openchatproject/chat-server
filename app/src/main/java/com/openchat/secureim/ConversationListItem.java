package com.openchat.secureim;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.RippleDrawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.openchat.secureim.components.AvatarImageView;
import com.openchat.secureim.components.FromTextView;
import com.openchat.secureim.database.model.ThreadRecord;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.util.DateUtils;
import com.openchat.secureim.util.ResUtil;

import java.util.Locale;
import java.util.Set;

import static com.openchat.secureim.util.SpanUtil.color;

public class ConversationListItem extends RelativeLayout
                                  implements Recipients.RecipientsModifiedListener
{
  private final static String TAG = ConversationListItem.class.getSimpleName();

  private final static Typeface BOLD_TYPEFACE  = Typeface.create("sans-serif", Typeface.BOLD);
  private final static Typeface LIGHT_TYPEFACE = Typeface.create("sans-serif-light", Typeface.NORMAL);

  private Set<Long>       selectedThreads;
  private Recipients      recipients;
  private long            threadId;
  private TextView        subjectView;
  private FromTextView    fromView;
  private TextView        dateView;
  private boolean         read;
  private AvatarImageView contactPhotoImage;

  private final @DrawableRes int readBackground;
  private final @DrawableRes int unreadBackround;

  private final Handler handler = new Handler();
  private int distributionType;

  public ConversationListItem(Context context) {
    this(context, null);
  }

  public ConversationListItem(Context context, AttributeSet attrs) {
    super(context, attrs);
    readBackground  = ResUtil.getDrawableRes(context, R.attr.conversation_list_item_background_read);
    unreadBackround = ResUtil.getDrawableRes(context, R.attr.conversation_list_item_background_unread);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    this.subjectView       = (TextView)        findViewById(R.id.subject);
    this.fromView          = (FromTextView)    findViewById(R.id.from);
    this.dateView          = (TextView)        findViewById(R.id.date);
    this.contactPhotoImage = (AvatarImageView) findViewById(R.id.contact_photo_image);
  }

  public void set(ThreadRecord thread, Locale locale, Set<Long> selectedThreads, boolean batchMode) {
    this.selectedThreads  = selectedThreads;
    this.recipients       = thread.getRecipients();
    this.threadId         = thread.getThreadId();
    this.read             = thread.isRead();
    this.distributionType = thread.getDistributionType();

    this.recipients.addListener(this);
    this.fromView.setText(recipients, read);

    this.subjectView.setText(thread.getDisplayBody());
    this.subjectView.setTypeface(read ? LIGHT_TYPEFACE : BOLD_TYPEFACE);

    if (thread.getDate() > 0) {
      CharSequence date = DateUtils.getBriefRelativeTimeSpanString(getContext(), locale, thread.getDate());
      dateView.setText(read ? date : color(getResources().getColor(R.color.openchatservice_primary), date));
      dateView.setTypeface(read ? LIGHT_TYPEFACE : BOLD_TYPEFACE);
    }

    setBatchState(batchMode);
    setBackground(thread);
    setRippleColor(recipients);
    this.contactPhotoImage.setAvatar(recipients, true);
  }

  public void unbind() {
    if (this.recipients != null)
      this.recipients.removeListener(this);
  }

  private void setBatchState(boolean batch) {
    setSelected(batch && selectedThreads.contains(threadId));
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

  private void setBackground(ThreadRecord thread) {
    if (thread.isRead()) setBackgroundResource(readBackground);
    else                 setBackgroundResource(unreadBackround);
  }

  @TargetApi(VERSION_CODES.LOLLIPOP)
  private void setRippleColor(Recipients recipients) {
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      ((RippleDrawable)(getBackground()).mutate())
          .setColor(ColorStateList.valueOf(recipients.getColor().toConversationColor(getContext())));
    }
  }

  @Override
  public void onModified(final Recipients recipients) {
    handler.post(new Runnable() {
      @Override
      public void run() {
        fromView.setText(recipients, read);
        contactPhotoImage.setAvatar(recipients, true);
        setRippleColor(recipients);
      }
    });
  }
}
