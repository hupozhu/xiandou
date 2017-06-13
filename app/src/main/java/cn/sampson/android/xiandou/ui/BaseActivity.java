package cn.sampson.android.xiandou.ui;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;

/**
 * Created by Administrator on 2017/6/5.
 */

public class BaseActivity extends AppCompatActivity {
    protected Handler mHandler = new Handler(Looper.getMainLooper());

    protected Toolbar mToolbar;
    protected ActionBar mActionToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
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

}
