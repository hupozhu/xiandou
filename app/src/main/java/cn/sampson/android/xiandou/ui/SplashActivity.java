package cn.sampson.android.xiandou.ui;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

import cn.sampson.android.xiandou.ui.main.MainActivity;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by chengyang on 2017/6/10.
 */
@RuntimePermissions
public class SplashActivity extends BaseActivity {

    private final int MAX_TIME = 1;
    private int count;

    private final MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<SplashActivity> mActivity;

        public MyHandler(SplashActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SplashActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 0:
                        if (activity.count == 0) {//如果倒计时完了，位置还没有
                            sendEmptyMessageDelayed(1, 1000);
                        } else {//倒计时
                            if (activity.count > 0)
                                activity.count--;
                            sendEmptyMessageDelayed(0, 1000);
                        }
                        break;
                    case 1:
                        activity.jumpTo(MainActivity.class);
                        activity.finish();
                        break;
                }

            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SplashActivityPermissionsDispatcher.countDownWithCheck(SplashActivity.this);
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void countDown() {
        count = MAX_TIME;
        mHandler.sendEmptyMessageAtTime(0, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
