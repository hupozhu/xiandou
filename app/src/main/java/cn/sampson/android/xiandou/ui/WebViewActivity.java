package cn.sampson.android.xiandou.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.utils.DeviceUtils;
import cn.sampson.android.xiandou.utils.Tip;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;


/**
 * Created by Administrator on 2016/8/3.
 */
public class WebViewActivity extends BaseActivity {

    public static final String URL = "url";
    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.root)
    RelativeLayout root;

    private String url;
    private String fristPath = "";

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        setActionBarBack();

        url = getIntent().getStringExtra(URL);

        initView();
    }

    private void initView() {
        dialog = ProgressDialog.show(this, null, "加载中...");
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    dialog.dismiss();
                }

                return false;
            }
        });
        webView.canGoBack();
        WebSettings webSettings = webView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        //加入js调用
        webView.addJavascriptInterface(new LoadListener(), "HTMLOUT");

        webSettings.setBlockNetworkImage(false);

        webSettings.setSupportZoom(true);

        webSettings.setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", MODE_PRIVATE).getPath();
        webSettings.setGeolocationEnabled(true);
        webSettings.setDatabasePath(dir);
        webSettings.setGeolocationDatabasePath(dir);
        webSettings.setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")) {
                    DeviceUtils.callUpBySystem(WebViewActivity.this, url.substring(3, url.length()));
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                dialog.dismiss();
                if (!TextUtils.isEmpty(view.getTitle())) {
                   getSupportActionBar().setTitle(view.getTitle());
                } else {
                }
                Tip.i("originUrl =======> " + url);
                view.loadUrl("javascript:window.HTMLOUT.processHTML(document.getElementById('description').content);");

                webView.loadUrl("javascript:playPause()");
                if (null != fristPath && fristPath.equals("")) {
                    fristPath = webView.getUrl();
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                dialog.show();
            }

        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota,
                                                long estimatedDatabaseSize, long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {
                super.onExceededDatabaseQuota(url, databaseIdentifier, quota, estimatedDatabaseSize, totalQuota,
                        quotaUpdater);
                quotaUpdater.updateQuota(estimatedDatabaseSize * 2);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });
        webView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.loadUrl("");
    }

    @Override
    public void onBackPressed() {
//        if (webView != null && webView.getUrl() != null) {
//            if (webView.getUrl().equals(fristPath)) {
//                finish();
//            } else {
//                webView.goBack();
//            }
//        } else
            super.onBackPressed();
    }

    class LoadListener {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html) {
            if (!TextUtils.isEmpty(html)) {
                Tip.i("===============>>>>>>>>>>>>>>>>>>>>description = " + html);
            } else {
            }
        }
    }

    private String processUrl(String originUrl) {
        int index = originUrl.lastIndexOf('.');
        if (index > 0) {
            String subString = originUrl.substring(index);
            Tip.i("subString =======> " + subString);
            if (TextUtils.equals(subString, ".html")) {
                return originUrl.substring(0, index);
            }
        }
        return originUrl;
    }
}
