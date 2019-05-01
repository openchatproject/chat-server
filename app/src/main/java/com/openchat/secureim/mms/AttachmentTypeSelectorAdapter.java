package com.openchat.secureim.mms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.openchat.secureim.R;
import com.openchat.secureim.util.ResUtil;

import java.util.ArrayList;
import java.util.List;

public class AttachmentTypeSelectorAdapter extends ArrayAdapter<AttachmentTypeSelectorAdapter.IconListItem> {

  public static final int ADD_IMAGE         = 1;
  public static final int ADD_VIDEO         = 2;
  public static final int ADD_SOUND         = 3;
  public static final int ADD_CONTACT_INFO  = 4;
  public static final int TAKE_PHOTO        = 5;

  private final Context context;

  public AttachmentTypeSelectorAdapter(Context context) {
    super(context, R.layout.icon_list_item, getItemList(context));
    this.context = context;
  }

  public int buttonToCommand(int position) {
    return getItem(position).getCommand();
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view;

    if (convertView == null) {
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      view = inflater.inflate(R.layout.icon_list_item, parent, false);
    } else {
      view = convertView;
    }

    TextView  text  = (TextView)  view.findViewById(R.id.text1);
    ImageView image = (ImageView) view.findViewById(R.id.icon);

    text.setText(getItem(position).getTitle());
    image.setImageResource(getItem(position).getResource());

    return view;
  }

  private static List<IconListItem> getItemList(Context context) {
    List<IconListItem> data = new ArrayList<>(4);
    addItem(data, context.getString(R.string.AttachmentTypeSelectorAdapter_take_photo), ResUtil.getDrawableRes(context, R.attr.conversation_attach_photo),        TAKE_PHOTO);
    addItem(data, context.getString(R.string.AttachmentTypeSelectorAdapter_picture),    ResUtil.getDrawableRes(context, R.attr.conversation_attach_image),        ADD_IMAGE);
    addItem(data, context.getString(R.string.AttachmentTypeSelectorAdapter_video),      ResUtil.getDrawableRes(context, R.attr.conversation_attach_video),        ADD_VIDEO);
    addItem(data, context.getString(R.string.AttachmentTypeSelectorAdapter_audio),      ResUtil.getDrawableRes(context, R.attr.conversation_attach_sound),        ADD_SOUND);
    addItem(data, context.getString(R.string.AttachmentTypeSelectorAdapter_contact),    ResUtil.getDrawableRes(context, R.attr.conversation_attach_contact_info), ADD_CONTACT_INFO);

    return data;
  }

  private static void addItem(List<IconListItem> list, String text, int resource, int id) {
    list.add(new IconListItem(text, resource, id));
  }

  public static class IconListItem {
    private final String title;
    private final int    resource;
    private final int    id;

    public IconListItem(String title, int resource, int id) {
      this.resource = resource;
      this.title    = title;
      this.id       = id;
    }

    public int getCommand() {
      return id;
    }

    public String getTitle() {
      return title;
    }

    public int getResource() {
      return resource;
    }
  }
}
