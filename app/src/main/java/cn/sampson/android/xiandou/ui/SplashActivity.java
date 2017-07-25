package cn.sampson.android.xiandou.ui;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.main.MainActivity;
import cn.sampson.android.xiandou.utils.ScreenUtils;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;

/**
 * Created by chengyang on 2017/6/10.
 */
public class SplashActivity extends BaseActivity {

    private final int MAX_TIME = 1;

    @Bind(R.id.splash_container)
    FrameLayout splashContainer;
    @Bind(R.id.skip_view)
    TextView skipView;
    @Bind(R.id.splash_holder)
    ImageView splashHolder;
    @Bind(R.id.fl_container)
    RelativeLayout flContainer;
    private int count;

    private static final String SKIP_TEXT = "点击跳过 %d";

    public boolean canJump = false;

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

    /**
     * 沉浸式状态栏
     */
    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int top = ScreenUtils.getSystemBarHeight();
            flContainer.setPadding(0, top, 0, 0);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        StatusBarUtil.transparencyBar(this);
        initSystemBar();

        countDown();
    }


    public void countDown() {
        count = MAX_TIME;
        mHandler.sendEmptyMessage(0);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
    }

    /**
     * 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
