package cn.sampson.android.xiandou.widget.htmlParseText;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

public class HtmlTextView extends TextView {

    Html.ImageGetter imgGetter;

    public HtmlTextView(Context context) {
        super(context);
    }

    public HtmlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HtmlTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Parses String containing HTML to Android's Spannable format and displays
     * it in this TextView.
     *
     * @param html String containing HTML, for example: "<b>Hello world!</b>"
     */
    public void setHtmlFromString(String html, boolean useLocalDrawables) {
        if (useLocalDrawables) {
            imgGetter = new LocalImageGetter(getContext());
        } else {
            imgGetter = new UrlImageGetter(this, getContext());
        }
        setText(Html.fromHtml(html, imgGetter, new HtmlTagHandler()));
        setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void doDestory() {
        if (imgGetter != null && imgGetter instanceof UrlImageGetter) {
            ((UrlImageGetter) imgGetter).doDestroy();
        }
    }

}
