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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.RoundedImageView;

import com.openchat.secureim.database.model.ThreadRecord;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.util.BitmapUtil;
import com.openchat.secureim.util.DateUtils;
import com.openchat.secureim.util.Emoji;

import java.util.Set;

public class ShareListItem extends RelativeLayout
                                  implements Recipient.RecipientModifiedListener
{
  private final static String TAG = ShareListItem.class.getSimpleName();

  private Context    context;
  private Recipients recipients;
  private long       threadId;
  private TextView   fromView;

  private RoundedImageView contactPhotoImage;

  private final Handler handler = new Handler();
  private int distributionType;

  public ShareListItem(Context context) {
    super(context);
    this.context = context;
  }

  public ShareListItem(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
  }

  @Override
  protected void onFinishInflate() {
    this.fromView          = (TextView)  findViewById(R.id.from);
    this.contactPhotoImage = (RoundedImageView) findViewById(R.id.contact_photo_image);
  }

  public void set(ThreadRecord thread) {
    this.recipients       = thread.getRecipients();
    this.threadId         = thread.getThreadId();
    this.distributionType = thread.getDistributionType();

    this.recipients.addListener(this);
    this.fromView.setText(formatFrom(recipients));

    setBackground();
    setContactPhoto(this.recipients.getPrimaryRecipient());
  }

  public void unbind() {
    if (this.recipients != null) this.recipients.removeListener(this);
  }

  private void setContactPhoto(final Recipient recipient) {
    if (recipient == null) return;
    contactPhotoImage.setImageBitmap(recipient.getContactPhoto());
  }

  private void setBackground() {
    int[]      attributes = new int[]{R.attr.conversation_list_item_background_read};
    TypedArray drawables  = context.obtainStyledAttributes(attributes);

    setBackgroundDrawable(drawables.getDrawable(0));

    drawables.recycle();
  }

  private CharSequence formatFrom(Recipients from) {
    final String fromString;
    final boolean isUnnamedGroup = from.isGroupRecipient() && TextUtils.isEmpty(from.getPrimaryRecipient().getName());
    if (isUnnamedGroup) {
      fromString = context.getString(R.string.ConversationActivity_unnamed_group);
    } else {
      fromString = from.toShortString();
    }
    SpannableStringBuilder builder = new SpannableStringBuilder(fromString);

    final int typeface;
    if (isUnnamedGroup) typeface = Typeface.ITALIC;
    else                typeface = Typeface.NORMAL;

    builder.setSpan(new StyleSpan(typeface), 0, builder.length(),
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

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
        fromView.setText(formatFrom(recipients));
        setContactPhoto(recipients.getPrimaryRecipient());
      }
    });
  }
}
