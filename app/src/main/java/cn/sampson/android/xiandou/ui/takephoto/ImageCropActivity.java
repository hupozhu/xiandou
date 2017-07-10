package cn.sampson.android.xiandou.ui.takephoto;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.utils.FileUtils;
import cn.sampson.android.xiandou.utils.Tip;
import cn.sampson.android.xiandou.utils.ToastUtils;
import rx.Observable;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static android.graphics.Bitmap.CompressFormat.JPEG;
import static rx.android.schedulers.AndroidSchedulers.mainThread;
import static rx.schedulers.Schedulers.io;

/**
 * Created by chengyang on 2017/5/9.
 */

public class ImageCropActivity extends BaseActivity implements View.OnTouchListener, View.OnClickListener {

    public static final String RATIO = "ratio";
    public static final String QUALITY = "quality";
    public static final String RESULT_PHOTO_PATH = "resultPhotoPath";

    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;
    private static final int REQUEST_CODE_PICK_IMAGE = 2;

    @Bind(R.id.crop_view)
    CropView cropView;

    @Bind(R.id.reset)
    TextView reset;
    @Bind(R.id.sure)
    TextView sure;

    private int currentType;
    private Uri tempUri;

    private float mRatio;
    //默认的图片压缩质量为100
    private int mQuality = 100;

    CompositeSubscription subscriptions = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout._activity_crop_bitmap);
        ButterKnife.bind(this);

        init(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Tip.i("==========   beng l beng l beng l  ==========");

        if (tempUri != null && currentType == PHOTO_REQUEST_TAKEPHOTO) {
            super.onSaveInstanceState(outState);
            outState.putString("oldPath", tempUri.getPath());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        Tip.i("====> doInit");
        Intent in = getIntent();
        if (savedInstanceState != null) {
            Tip.i("重新开启Activity，处理崩溃情况");
            tempUri = Uri.parse(savedInstanceState.getString("oldPath"));
            cropView.extensions().cameraUsing(this, tempUri, PHOTO_REQUEST_TAKEPHOTO);
        } else {
            mRatio = in.getFloatExtra(RATIO, 1.0f);
            mQuality = in.getIntExtra(QUALITY, 100);
            if (in.getStringExtra("state").equals("take_photo")) {
                Tip.i("拍照");
                currentType = PHOTO_REQUEST_TAKEPHOTO;
                launchCamera();
            } else if (in.getStringExtra("state").equals("get_photo")) {
                currentType = REQUEST_CODE_PICK_IMAGE;
                Tip.i("从相册获取");
                cropView.extensions().pickUsing(this, REQUEST_CODE_PICK_IMAGE);
            }
        }
        cropView.setViewportRatio(mRatio);
        cropView.setOnTouchListener(this);
        reset.setOnClickListener(this);
        sure.setOnClickListener(this);

    }

    public void launchCamera() {
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMdd_HH:mm:ss");
        String filename = timeStampFormat.format(new Date());
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, filename);
        tempUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        cropView.extensions().cameraUsing(this, tempUri, PHOTO_REQUEST_TAKEPHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICK_IMAGE) {

                Uri galleryPictureUri = data.getData();
                cropView.extensions().load(galleryPictureUri);
                setTextButtonVisible();

            } else if (requestCode == PHOTO_REQUEST_TAKEPHOTO) {

                Uri result;
                if (data != null && data.getData() != null) {
                    result = data.getData();
                } else {
                    result = tempUri;
                }

                File file = new File(FileUtils.getRealPathFromURI(ImageCropActivity.this, result));
                if (file != null && file.exists()) {
                    cropView.extensions().load(file);
                } else {
                    ToastUtils.show("图片解析失败");
                    goBack();
                }
            } else {
                goBack();
            }
        } else {
            goBack();
        }
    }

    /**
     * 让text按钮显示
     */
    private void setTextButtonVisible() {
        if (currentType == PHOTO_REQUEST_TAKEPHOTO) {
            reset.setVisibility(View.VISIBLE);
        } else {
            reset.setVisibility(View.GONE);
        }
        sure.setVisibility(View.VISIBLE);
    }

    private void setTextButtonGone() {
        reset.setVisibility(View.GONE);
        sure.setVisibility(View.GONE);
    }

    private void cropBitmap() {
        final File croppedFile = new File(getCacheDir(), "cropped" + CropViewConfig.cropNum++ + ".jpg");

        Observable<Void> onSave = Observable.from(cropView.extensions()
                .crop()
                .quality(mQuality)
                .format(JPEG)
                .into(croppedFile))
                .subscribeOn(io())
                .observeOn(mainThread());

        subscriptions.add(onSave
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void nothing) {
                        Intent intent = new Intent();
                        intent.putExtra(RESULT_PHOTO_PATH, croppedFile.getAbsolutePath());
                        setResult(200, intent);
                        goBack();
                    }
                }));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getPointerCount() > 1 || cropView.getImageBitmap() == null) {
            return true;
        }

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                setTextButtonGone();
                break;
            default:
                setTextButtonVisible();
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset:
                launchCamera();
                break;
            case R.id.sure:
                cropBitmap();
                break;
        }
    }
}
