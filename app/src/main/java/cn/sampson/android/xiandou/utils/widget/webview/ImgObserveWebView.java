package cn.sampson.android.xiandou.utils.widget.webview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * Created by Administrator on 2016/7/20.
 */
public class ImgObserveWebView extends WebView {

    private OnImgClickListener mListener;
    float x, y;

    public ImgObserveWebView(Context context) {
        super(context);
        init(context);
    }

    public ImgObserveWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImgObserveWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ImgObserveWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @SuppressLint("JavascriptInterface")
    private void init(Context context) {
        getSettings().setJavaScriptEnabled(true); //支持JS
        addJavascriptInterface(new JsInterface(context), "imageClick"); //JS交互
        setWebViewClient(new MyWebViewClient());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //通过wenview的touch来响应web上的图片点击事件
        float density = getResources().getDisplayMetrics().density; //屏幕密度
        float touchX = event.getX() / density;  //必须除以屏幕密度
        float touchY = event.getY() / density;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            x = touchX;
            y = touchY;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            float dx = Math.abs(touchX - x);
            float dy = Math.abs(touchY - y);
            if (dx < 10.0 / density && dy < 10.0 / density) {

                HitTestResult hitTestResult = getHitTestResult();
                if (hitTestResult.getType() == HitTestResult.IMAGE_TYPE || hitTestResult.getType() == HitTestResult.IMAGE_ANCHOR_TYPE || hitTestResult.getType() == HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    if (mListener != null) {
//                        String s = "http://stjia.cn/Public/Home/style/kindeditor/attached/image/20160623/20160623070911_44709.png";
//                        mListener.onClick(s);
                        mListener.onClick(hitTestResult.getExtra().toString());
                    }
                }
//              clickImage(touchX, touchY);
            }
        }
        return super.onTouchEvent(event);
    }

    private void clickImage(float touchX, float touchY) {
        //通过触控的位置来获取图片URL
        String js = "javascript:(function(){" +
                "var  obj=document.elementFromPoint(" + touchX + "," + touchY + ");"
                + "if(obj.src!=null){" + " window.imageClick.click(obj.src);}" +
                "})()";
        loadUrl(js);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageFinished(view, url);
            // html加载完成之后，添加监听图片的点击js函数
//            addImageClickListner();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    public class JsInterface implements JavascriptCallback {
        Context context;

        public JsInterface(Context context) {
            this.context = context;
        }

        //查看图片url
        public void click(String url) {
            if (mListener != null)
                mListener.onClick(url);
        }
    }

    public void setImgClickListener(OnImgClickListener listener) {
        mListener = listener;
    }

    public interface OnImgClickListener {
        void onClick(String url);
    }
}
