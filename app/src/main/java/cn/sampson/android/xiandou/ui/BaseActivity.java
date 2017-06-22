package cn.sampson.android.xiandou.ui;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import cn.sampson.android.xiandou.R;

/**
 * Created by Administrator on 2017/6/5.
 */

public class BaseActivity extends AppCompatActivity {

    public MyHandler mHandler = new MyHandler(this);
    private View loadingView;

    protected ActionBar mActionToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    protected void setActionBarBack() {
        mActionToolbar = getSupportActionBar();
        mActionToolbar.setHomeAsUpIndicator(R.mipmap.ic_title_back);
        mActionToolbar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return true;
    }

    // ========================================== 跳转 ====================================================

    public void jumpTo(Class activityClazz) {
        jumpTo(activityClazz, new Bundle());
    }

    public void jumpTo(Class activityClazz, Bundle bundle) {
        Intent intent = new Intent(this, activityClazz);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void jumpForResult(int requestCode, Class activityClazz) {
        jumpForResult(requestCode, activityClazz, new Bundle());
    }

    public void jumpForResult(int requestCode, Class activityClazz, Bundle bundle) {
        Intent intent = new Intent(this, activityClazz);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    public void goBack() {
        finish();
    }

    // ======================================   异步处理数据的handle ===========================================
    private static class MyHandler extends Handler {
        private final WeakReference<BaseActivity> mActivity;

        public MyHandler(BaseActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 1:
                        activity.onDataComplete();
                        break;
                }
            }
        }
    }

    protected void onDataComplete() {
    }

    protected void sendHandlerMessage() {
        mHandler.sendEmptyMessage(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(1);
    }

    protected void showLoadingView(ViewGroup rlRoot) {
        loadingView = LayoutInflater.from(BaseActivity.this).inflate(R.layout._loading_view, rlRoot, false);
        rlRoot.addView(loadingView);
    }

    protected void removeLoadingView(ViewGroup rlRoot) {
        if (loadingView != null) {
            rlRoot.removeView(loadingView);
            loadingView = null;
        }
    }


}
