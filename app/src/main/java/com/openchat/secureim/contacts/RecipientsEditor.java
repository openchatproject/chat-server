package com.openchat.secureim.contacts;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.text.Annotation;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.MultiAutoCompleteTextView;

import com.openchat.secureim.recipients.Recipient;
import com.openchat.secureim.recipients.RecipientFactory;
import com.openchat.secureim.recipients.RecipientFormattingException;
import com.openchat.secureim.recipients.Recipients;
import com.openchat.secureim.recipients.RecipientsFormatter;

import java.util.ArrayList;
import java.util.List;

public class RecipientsEditor extends MultiAutoCompleteTextView {
    private int mLongPressedPosition = -1;
    private final RecipientsEditorTokenizer mTokenizer;
    private char mLastSeparator = ',';
    private Context mContext;

    public RecipientsEditor(Context context, AttributeSet attrs) {
        super(context, attrs, android.R.attr.autoCompleteTextViewStyle);
        mContext = context;
        mTokenizer = new RecipientsEditorTokenizer(context, this);
        setTokenizer(mTokenizer);
        setImeOptions(EditorInfo.IME_ACTION_NEXT);

        
        addTextChangedListener(new TextWatcher() {
            private Annotation[] mAffected;

            public void beforeTextChanged(CharSequence s, int start,
                    int count, int after) {
                mAffected = ((Spanned) s).getSpans(start, start + count,
                        Annotation.class);
            }

            public void onTextChanged(CharSequence s, int start,
                    int before, int after) {
                if (before == 0 && after == 1) {    // inserting a character
                    char c = s.charAt(start);
                    if (c == ',' || c == ';') {
                        mLastSeparator = c;
                    }
                }
            }

            public void afterTextChanged(Editable s) {
                if (mAffected != null) {
                    for (Annotation a : mAffected) {
                        s.removeSpan(a);
                    }
                }

                mAffected = null;
            }
        });
    }

    @Override
    public boolean enoughToFilter() {
        if (!super.enoughToFilter()) {
            return false;
        }
        int end = getSelectionEnd();
        int len = getText().length();

        return end == len;
    }

    public int getRecipientCount() {
        return mTokenizer.getNumbers().size();
    }

    public List<String> getNumbers() {
        return mTokenizer.getNumbers();
    }

    public Recipients constructContactsFromInput() {
    	Recipients r = null;
        try {
			r = RecipientFactory.getRecipientsFromString(mContext, mTokenizer.getRawString(), false);
		} catch (RecipientFormattingException e) {
			Log.w( "RecipientsEditor", e);
		}
		return r;
    }

    private boolean isValidAddress(String number, boolean isMms) {
        
        return PhoneNumberUtils.isWellFormedSmsAddress(number);
    }

    public boolean hasValidRecipient(boolean isMms) {
        for (String number : mTokenizer.getNumbers()) {
            if (isValidAddress(number, isMms))
                return true;
        }
        return false;
    }

    

    public String formatInvalidNumbers(boolean isMms) {
        StringBuilder sb = new StringBuilder();
        for (String number : mTokenizer.getNumbers()) {
            if (!isValidAddress(number, isMms)) {
                if (sb.length() != 0) {
                    sb.append(", ");
                }
                sb.append(number);
            }
        }
        return sb.toString();
    }

    

    public static CharSequence contactToToken(Recipient c) {
      String name       = c.getName();
      String number     = c.getNumber();
      SpannableString s = new SpannableString(RecipientsFormatter.formatNameAndNumber(name, number));
      int len           = s.length();

      if (len == 0) {
        return s;
      }

      s.setSpan(new Annotation("number", c.getNumber()), 0, len,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

      return s;
    }

    public void populate(Recipients list) {
        SpannableStringBuilder sb = new SpannableStringBuilder();

        for (Recipient c : list.getRecipientsList()) {
            if (sb.length() != 0) {
                sb.append(", ");
            }

            sb.append(contactToToken(c));
        }

        setText(sb);
    }

    private int pointToPosition(int x, int y) {
        x -= getCompoundPaddingLeft();
        y -= getExtendedPaddingTop();

        x += getScrollX();
        y += getScrollY();

        Layout layout = getLayout();
        if (layout == null) {
            return -1;
        }

        int line = layout.getLineForVertical(y);
        int off = layout.getOffsetForHorizontal(line, x);

        return off;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        final int x = (int) ev.getX();
        final int y = (int) ev.getY();

        if (action == MotionEvent.ACTION_DOWN) {
            mLongPressedPosition = pointToPosition(x, y);
        }

        return super.onTouchEvent(ev);
    }

    private static String getNumberAt(Spanned sp, int start, int end, Context context) {
        return getFieldAt("number", sp, start, end, context);
    }

    private static int getSpanLength(Spanned sp, int start, int end, Context context) {
        Annotation[] a = sp.getSpans(start, end, Annotation.class);
        if (a.length > 0) {
            return sp.getSpanEnd(a[0]);
        }
        return 0;
    }

    private static String getFieldAt(String field, Spanned sp, int start, int end,
            Context context) {
        Annotation[] a = sp.getSpans(start, end, Annotation.class);
        String fieldValue = getAnnotation(a, field);
        if (TextUtils.isEmpty(fieldValue)) {
            fieldValue = TextUtils.substring(sp, start, end);
        }
        return fieldValue;

    }

    private static String getAnnotation(Annotation[] a, String key) {
        for (int i = 0; i < a.length; i++) {
            if (a[i].getKey().equals(key)) {
                return a[i].getValue();
            }
        }

        return "";
    }

    private class RecipientsEditorTokenizer
            implements MultiAutoCompleteTextView.Tokenizer {
        private final MultiAutoCompleteTextView mList;
        private final Context mContext;

        RecipientsEditorTokenizer(Context context, MultiAutoCompleteTextView list) {
            mList = list;
            mContext = context;
        }

        
        public int findTokenStart(CharSequence text, int cursor) {
            int i = cursor;
            char c;

            while (i > 0 && (c = text.charAt(i - 1)) != ',' && c != ';') {
                i--;
            }
            while (i < cursor && text.charAt(i) == ' ') {
                i++;
            }

            return i;
        }

        
        public int findTokenEnd(CharSequence text, int cursor) {
            int i = cursor;
            int len = text.length();
            char c;

            while (i < len) {
                if ((c = text.charAt(i)) == ',' || c == ';') {
                    return i;
                } else {
                    i++;
                }
            }

            return len;
        }

        
        public CharSequence terminateToken(CharSequence text) {
            int i = text.length();

            while (i > 0 && text.charAt(i - 1) == ' ') {
                i--;
            }

            char c;
            if (i > 0 && ((c = text.charAt(i - 1)) == ',' || c == ';')) {
                return text;
            } else {
                String separator = mLastSeparator + " ";
                if (text instanceof Spanned) {
                    SpannableString sp = new SpannableString(text + separator);
                    TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
                                            Object.class, sp, 0);
                    return sp;
                } else {
                    return text + separator;
                }
            }
        }
        public String getRawString() {
        	return mList.getText().toString();
        }
        public List<String> getNumbers() {
            Spanned sp = mList.getText();
            int len = sp.length();
            List<String> list = new ArrayList<String>();

            int start = 0;
            int i = 0;
            while (i < len + 1) {
                char c;
                if ((i == len) || ((c = sp.charAt(i)) == ',') || (c == ';')) {
                    if (i > start) {
                        list.add(getNumberAt(sp, start, i, mContext));

                        int spanLen = getSpanLength(sp, start, i, mContext);
                        if (spanLen > i) {
                            i = spanLen;
                        }
                    }

                    i++;

                    while ((i < len) && (sp.charAt(i) == ' ')) {
                        i++;
                    }

                    start = i;
                } else {
                    i++;
                }
            }

            return list;
        }
    }

    static class RecipientContextMenuInfo implements ContextMenuInfo {
        final Recipient recipient;

        RecipientContextMenuInfo(Recipient r) {
            recipient = r;
        }
    }
}
