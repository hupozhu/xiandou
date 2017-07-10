package cn.sampson.android.xiandou.widget.htmlParseText;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


public class UrlImageGetter implements Html.ImageGetter {

    Context c;
    TextView container;
    int width;

    final UrlDrawable urlDrawable = new UrlDrawable();

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap source, Picasso.LoadedFrom from) {
            float scaleWidth = ((float) width) / source.getWidth();
            // 取得想要缩放的matrix参数
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleWidth);
            source = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
            urlDrawable.bitmap = source;
            urlDrawable.setBounds(0, 0, source.getWidth(), source.getHeight());
            container.invalidate();
            container.setText(container.getText()); // 解决图文重叠
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };

    /**
     * @param t
     * @param c
     */
    public UrlImageGetter(TextView t, Context c) {
        this.c = c;
        this.container = t;
        width = c.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public Drawable getDrawable(String source) {

        Picasso.with(c).load(source).into(target);

        return urlDrawable;
    }

    public void doDestroy() {
        Picasso.with(c).cancelRequest(target);
    }

    @SuppressWarnings("deprecation")
    public class UrlDrawable extends BitmapDrawable {
        protected Bitmap bitmap;

        @Override
        public void draw(Canvas canvas) {
            // override the draw to facilitate refresh function later
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, 0, 0, getPaint());
            }
        }
    }
}
