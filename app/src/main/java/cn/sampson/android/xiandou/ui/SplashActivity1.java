//package cn.sampson.android.xiandou.ui;
//
//import android.Manifest;
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.provider.Settings;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.qq.e.ads.splash.SplashAD;
//import com.qq.e.ads.splash.SplashADListener;
//
//import java.lang.ref.WeakReference;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import cn.sampson.android.xiandou.R;
//import cn.sampson.android.xiandou.config.Constants;
//import cn.sampson.android.xiandou.ui.main.MainActivity;
//import cn.sampson.android.xiandou.utils.ScreenUtils;
//import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;
//
///**
// * Created by chengyang on 2017/6/10.
// */
//public class SplashActivity1 extends BaseActivity implements SplashADListener {
//
//    private final int MAX_TIME = 1;
//
//    @Bind(R.id.splash_container)
//    FrameLayout splashContainer;
//    @Bind(R.id.skip_view)
//    TextView skipView;
//    @Bind(R.id.splash_holder)
//    ImageView splashHolder;
//    @Bind(R.id.fl_container)
//    RelativeLayout flContainer;
//    private int count;
//
//    private SplashAD splashAD;
//    private static final String SKIP_TEXT = "点击跳过 %d";
//
//    public boolean canJump = false;
//
//    private final MyHandler mHandler = new MyHandler(this);
//
//    private static class MyHandler extends Handler {
//        private final WeakReference<SplashActivity1> mActivity;
//
//        public MyHandler(SplashActivity1 activity) {
//            mActivity = new WeakReference<>(activity);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            SplashActivity1 activity = mActivity.get();
//            if (activity != null) {
//                switch (msg.what) {
//                    case 0:
//                        if (activity.count == 0) {//如果倒计时完了，位置还没有
//                            sendEmptyMessageDelayed(1, 1000);
//                        } else {//倒计时
//                            if (activity.count > 0)
//                                activity.count--;
//                            sendEmptyMessageDelayed(0, 1000);
//                        }
//                        break;
//                    case 1:
//                        activity.jumpTo(MainActivity.class);
//                        activity.finish();
//                        break;
//                }
//
//            }
//        }
//    }
//
//    /**
//     * 沉浸式状态栏
//     */
//    private void initSystemBar() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            int top = ScreenUtils.getSystemBarHeight();
//            flContainer.setPadding(0, top, 0, 0);
//        }
//    }
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
//        ButterKnife.bind(this);
//        StatusBarUtil.transparencyBar(this);
//        initSystemBar();
//
//        // 如果targetSDKVersion >= 23，就要申请好权限。如果您的App没有适配到Android6.0（即targetSDKVersion < 23），那么只需要在这里直接调用fetchSplashAD接口。
//        if (Build.VERSION.SDK_INT >= 23) {
//            checkAndRequestPermission();
//        } else {
//            // 如果是Android6.0以下的机器，默认在安装时获得了所有权限，可以直接调用SDK
//            fetchSplashAD(this, splashContainer, skipView, Constants.APPID, Constants.SplashPosID, this, 0);
//        }
//    }
//
//    /**
//     * ----------非常重要----------
//     * <p>
//     * Android6.0以上的权限适配简单示例：
//     * <p>
//     * 如果targetSDKVersion >= 23，那么必须要申请到所需要的权限，再调用广点通SDK，否则广点通SDK不会工作。
//     * <p>
//     * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。
//     * 注意：下面的`checkSelfPermission`和`requestPermissions`方法都是在Android6.0的SDK中增加的API，如果您的App还没有适配到Android6.0以上，则不需要调用这些方法，直接调用广点通SDK即可。
//     */
//    @TargetApi(Build.VERSION_CODES.M)
//    private void checkAndRequestPermission() {
//        List<String> lackedPermission = new ArrayList<String>();
//        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
//            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
//        }
//
//        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
//            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        }
//
//        if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
//            lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        }
//
//        // 权限都已经有了，那么直接调用SDK
//        if (lackedPermission.size() == 0) {
//            fetchSplashAD(this, splashContainer, skipView, Constants.APPID, Constants.SplashPosID, this, 0);
//        } else {
//            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
//            String[] requestPermissions = new String[lackedPermission.size()];
//            lackedPermission.toArray(requestPermissions);
//            requestPermissions(requestPermissions, 1024);
//        }
//    }
//
//    public void countDown() {
//        count = MAX_TIME;
//        mHandler.sendEmptyMessageAtTime(0, 1000);
//    }
//
//    /**
//     * 拉取开屏广告，开屏广告的构造方法有3种，详细说明请参考开发者文档。
//     *
//     * @param activity      展示广告的activity
//     * @param adContainer   展示广告的大容器
//     * @param skipContainer 自定义的跳过按钮：传入该view给SDK后，SDK会自动给它绑定点击跳过事件。SkipView的样式可以由开发者自由定制，其尺寸限制请参考activity_splash.xml或者接入文档中的说明。
//     * @param appId         应用ID
//     * @param posId         广告位ID
//     * @param adListener    广告状态监听器
//     * @param fetchDelay    拉取广告的超时时长：取值范围[3000, 5000]，设为0表示使用广点通SDK默认的超时时长。
//     */
//    private void fetchSplashAD(Activity activity, ViewGroup adContainer, View skipContainer,
//                               String appId, String posId, SplashADListener adListener, int fetchDelay) {
//        splashAD = new SplashAD(activity, adContainer, skipContainer, appId, posId, adListener, fetchDelay);
//    }
//
//    @Override
//    public void onADPresent() {
//        Log.i("AD_DEMO", "SplashADPresent");
//        splashHolder.setVisibility(View.INVISIBLE); // 广告展示后一定要把预设的开屏图片隐藏起来
//    }
//
//    @Override
//    public void onADClicked() {
//        Log.i("AD_DEMO", "SplashADClicked");
//    }
//
//    /**
//     * 倒计时回调，返回广告还将被展示的剩余时间。
//     * 通过这个接口，开发者可以自行决定是否显示倒计时提示，或者还剩几秒的时候显示倒计时
//     *
//     * @param millisUntilFinished 剩余毫秒数
//     */
//    @Override
//    public void onADTick(long millisUntilFinished) {
//        Log.i("AD_DEMO", "SplashADTick " + millisUntilFinished + "ms");
//        skipView.setText(String.format(SKIP_TEXT, Math.round(millisUntilFinished / 1000f)));
//    }
//
//    @Override
//    public void onADDismissed() {
//        Log.i("AD_DEMO", "SplashADDismissed");
//        next();
//    }
//
//    @Override
//    public void onNoAD(int errorCode) {
//        Log.i("AD_DEMO", "LoadSplashADFail, eCode=" + errorCode);
//        /** 如果加载广告失败，则直接跳转 */
//        this.startActivity(new Intent(this, MainActivity.class));
//        this.finish();
//    }
//
//    /**
//     * 设置一个变量来控制当前开屏页面是否可以跳转，当开屏广告为普链类广告时，点击会打开一个广告落地页，此时开发者还不能打开自己的App主页。当从广告落地页返回以后，
//     * 才可以跳转到开发者自己的App主页；当开屏广告是App类广告时只会下载App。
//     */
//    private void next() {
//        if (canJump) {
//            this.startActivity(new Intent(this, MainActivity.class));
//            this.finish();
//        } else {
//            canJump = true;
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        canJump = false;
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (canJump) {
//            next();
//        }
//        canJump = true;
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mHandler.removeMessages(0);
//    }
//
//    /**
//     * 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费
//     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    private boolean hasAllPermissionsGranted(int[] grantResults) {
//        for (int grantResult : grantResults) {
//            if (grantResult == PackageManager.PERMISSION_DENIED) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
//            fetchSplashAD(this, splashContainer, skipView, Constants.APPID, Constants.SplashPosID, this, 0);
//        } else {
//            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
//            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//            intent.setData(Uri.parse("package:" + getPackageName()));
//            startActivity(intent);
//            finish();
//        }
//    }
//}