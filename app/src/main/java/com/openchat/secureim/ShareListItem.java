package com.openchat.secureim;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.openchat.secureim.components.AvatarImageView;
import com.openchat.secureim.components.FromTextView;
import com.openchat.secureim.database.model.ThreadRecord;
import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.Recipients;

public class ShareListItem extends RelativeLayout
                        implements Recipient.RecipientModifiedListener
{
  private final static String TAG = ShareListItem.class.getSimpleName();

  private Context      context;
  private Recipients   recipients;
  private long         threadId;
  private FromTextView fromView;

  private AvatarImageView contactPhotoImage;

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
    this.fromView          = (FromTextView)    findViewById(R.id.from);
    this.contactPhotoImage = (AvatarImageView) findViewById(R.id.contact_photo_image);
  }

  public void set(ThreadRecord thread) {
    this.recipients       = thread.getRecipients();
    this.threadId         = thread.getThreadId();
    this.distributionType = thread.getDistributionType();

    this.recipients.addListener(this);
    this.fromView.setText(recipients);

    setBackground();
    this.contactPhotoImage.setAvatar(this.recipients.getPrimaryRecipient(), false);
  }

  public void unbind() {
    if (this.recipients != null) this.recipients.removeListener(this);
  }

  private void setBackground() {
    int[]      attributes = new int[]{R.attr.conversation_list_item_background_read};
    TypedArray drawables  = context.obtainStyledAttributes(attributes);

    setBackgroundDrawable(drawables.getDrawable(0));

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
        fromView.setText(recipients);
        contactPhotoImage.setAvatar(recipients.getPrimaryRecipient(), false);
      }
    });
  }
}
