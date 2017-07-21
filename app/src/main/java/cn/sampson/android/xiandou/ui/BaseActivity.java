package cn.sampson.android.xiandou.ui;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.File;
import java.lang.ref.WeakReference;

import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.config.Constants;
import cn.sampson.android.xiandou.core.UpdatePhotoManager;
import cn.sampson.android.xiandou.ui.takephoto.ImageCropActivity;
import cn.sampson.android.xiandou.utils.ContextUtil;
import cn.sampson.android.xiandou.utils.ToastUtils;
import cn.sampson.android.xiandou.utils.permission.PermissionReq;
import cn.sampson.android.xiandou.utils.permission.PermissionResult;
import cn.sampson.android.xiandou.widget.dialog.PhotoPickDialog;

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

    // =========================================== menu处理 =============================================

    protected void setActionBarBack() {
        mActionToolbar = getSupportActionBar();
        mActionToolbar.setHomeAsUpIndicator(R.mipmap.ic_title_back);
        mActionToolbar.setDisplayHomeAsUpEnabled(true);
    }

    protected void setSupportToolbar(Toolbar toolbar, String title) {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_title_back);
        toolbar.setTitleTextColor(ContextUtil.getColor(R.color.black));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (null != title)
            getSupportActionBar().setTitle(title);
        else
            getSupportActionBar().setTitle(" ");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_save:
                onToolbarSave();
                break;
        }
        return true;
    }

    protected void onToolbarSave() {
    }

    /**
     * 让view获取焦点
     */
    public void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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


    // =========================================== 拍照 =============================================


    //图片压缩质量
    private int photo_compression_quality = 100;

    /**
     * 点击头像弹出选择头像对话框，拍照 或者 从相册选择
     */
    protected void showPhotoPickDialog(int quality) {
        photo_compression_quality = quality;
        showPhotoPickDialog();
    }

    /**
     * 点击头像弹出选择头像对话框，拍照 或者 从相册选择
     */
    protected void showPhotoPickDialog() {
        PhotoPickDialog dialog = new PhotoPickDialog(BaseActivity.this);
        dialog.setClickListener(new PhotoPickDialog.PhotoPickListener() {
            @Override
            public void fromCamera() {
                openCamera();
            }

            @Override
            public void fromAlbum() {
                openAlbum("get_photo");
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void openCamera() {
        PermissionReq.with(BaseActivity.this)
                .permissions(android.Manifest.permission.CAMERA)
                .result(new PermissionResult() {
                    @Override
                    public void onGranted() {
                        openAlbum("take_photo");
                    }

                    @Override
                    public void onDenied() {
                        ToastUtils.show(R.string.msg_no_camera);
                    }
                }).request();
    }

    private void openAlbum(final String type) {
        PermissionReq.with(BaseActivity.this)
                .permissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .result(new PermissionResult() {
                    @Override
                    public void onGranted() {
                        Intent mIntent = new Intent(BaseActivity.this, ImageCropActivity.class);
                        mIntent.putExtra("state", type);
                        mIntent.putExtra(ImageCropActivity.QUALITY, photo_compression_quality);
                        startActivityForResult(mIntent, Constants.REQUEST_PHOTO);
                    }

                    @Override
                    public void onDenied() {
                        ToastUtils.show(R.string.please_open_write_sdcard_permission);
                    }
                }).request();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == 200) {
            switch (requestCode) {
                case Constants.REQUEST_PHOTO:
                    uploadPhoto(data.getStringExtra(ImageCropActivity.RESULT_PHOTO_PATH));
                    break;
            }
        }
    }

    protected void uploadPhoto(String photoPath) {
        UpdatePhotoManager.getInstance().updatePhoto(new File(photoPath));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionReq.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
