package com.openchat.secureim;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.openchat.secureim.ImageMediaAdapter.ViewHolder;
import com.openchat.secureim.components.ThumbnailView;
import com.openchat.secureim.crypto.MasterSecret;
import com.openchat.secureim.database.CursorRecyclerViewAdapter;
import com.openchat.secureim.database.PartDatabase.ImageRecord;
import com.openchat.secureim.mms.Slide;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.util.MediaUtil;

import ws.com.google.android.mms.pdu.PduPart;

public class ImageMediaAdapter extends CursorRecyclerViewAdapter<ViewHolder> {
  private static final String TAG = ImageMediaAdapter.class.getSimpleName();

  private final MasterSecret masterSecret;

  public static class ViewHolder extends RecyclerView.ViewHolder {
    public ThumbnailView imageView;

    public ViewHolder(View v) {
      super(v);
      imageView = (ThumbnailView) v.findViewById(R.id.image);
    }
  }

  public ImageMediaAdapter(Context context, MasterSecret masterSecret, Cursor c) {
    super(context, c);
    this.masterSecret = masterSecret;
  }

  @Override
  public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
    final View view = LayoutInflater.from(getContext()).inflate(R.layout.media_overview_item, viewGroup, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final ViewHolder viewHolder, final @NonNull Cursor cursor) {
    final ThumbnailView imageView   = viewHolder.imageView;
    final ImageRecord   imageRecord = ImageRecord.from(cursor);

    PduPart part = new PduPart();

    part.setDataUri(imageRecord.getUri());
    part.setContentType(imageRecord.getContentType().getBytes());
    part.setPartId(imageRecord.getPartId());

    Slide slide = MediaUtil.getSlideForPart(getContext(), part, imageRecord.getContentType());
    if (slide != null) {
      imageView.setImageResource(slide, masterSecret);
    }

    imageView.setOnClickListener(new OnMediaClickListener(imageRecord));
  }

  private class OnMediaClickListener implements OnClickListener {
    private ImageRecord record;

    private OnMediaClickListener(ImageRecord record) {
      this.record = record;
    }

    @Override
    public void onClick(View v) {
      Intent intent = new Intent(getContext(), MediaPreviewActivity.class);
      intent.putExtra(MediaPreviewActivity.DATE_EXTRA, record.getDate());

      if (!TextUtils.isEmpty(record.getAddress())) {
        Recipients recipients = RecipientFactory.getRecipientsFromString(getContext(),
                                                                         record.getAddress(),
                                                                         true);
        if (recipients != null && recipients.getPrimaryRecipient() != null) {
          intent.putExtra(MediaPreviewActivity.RECIPIENT_EXTRA, recipients.getPrimaryRecipient().getRecipientId());
        }
      }
      intent.setDataAndType(record.getUri(), record.getContentType());
      getContext().startActivity(intent);

    }
  }
}
