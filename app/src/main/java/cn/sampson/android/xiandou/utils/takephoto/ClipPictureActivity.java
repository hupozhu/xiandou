package cn.sampson.android.xiandou.utils.takephoto;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.sampson.android.xiandou.R;

public class ClipPictureActivity extends Activity implements OnTouchListener, OnClickListener {
    public static final String RESULT_PHOTO_PATH = "resultPhotoPath";
    public static final String IS_URI = "isUri";

    private ImageView srcPic;
    private RelativeLayout ll;
    private RelativeLayout content;

    private View sure;
    private View reset;
    private ClipView clipview;

    private Matrix matrix = new Matrix();

    public static ClipPictureActivity instance;

    private ArrayList<String> mResults = new ArrayList<>();

    // 保存的范围
    private Matrix savedMatrix = new Matrix();
    private int operateType;

    /**
     * 拍照
     */

    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;

    private static final int REQUEST_CODE_PICK_IMAGE = 2;
    /**
     * 没有动作
     */
    private static final int NONE = 0;
    /**
     * 拖动
     */
    private static final int DRAG = 1;
    /**
     * 缩放
     */
    private static final int ZOOM = 2;
    /**
     * 初始化动作标志
     */
    private int mode = NONE;

    /**
     * 记录起始坐标
     */
    private PointF start = new PointF();
    /**
     * 记录缩放时两指中间点坐标
     */
    private PointF mid = new PointF();
    private float oldDist = 1f;

    /**
     * 要裁剪的图片
     */
    private Bitmap bitmap;

    private String userId;

    private float imageWidth;
    private float imageHeight;

    private int windWidth;
    private int windHeight;
    private int statusBarHeight;
    private Rect frame;

    int location[] = new int[2];

    Matrix matrix_2;

    Rect rect_2;

    float[] values;

    ImageState mapState;

    private Map<String, Float> haMap = new HashMap<String, Float>();

    // 判断是否有内存卡

    // 照片存储的地址
    File tempFile = null;
    private boolean isCrash = false;
    private String oldFilePath;

    private int currentType;
    private Uri tempUri;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout._activity_clippicture);

        doInit(savedInstanceState);
    }

    void doInit(final Bundle savedInstanceState) {
        srcPic = (ImageView) this.findViewById(R.id.src_pic);
        windWidth = getWindowManager().getDefaultDisplay().getWidth();
        preInit(savedInstanceState);
    }

    void preInit(final Bundle savedInstanceState) {
        // 判断是否有SD卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            tempFile = Utils.createAvatarFileFromSDCard();
            InitView(savedInstanceState);
        } else {//没有sd卡
            tempFile = new File("/data/data/DCIM/Camera/temp", getPhotoFileName());
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            InitView(savedInstanceState);
        }
    }

    private void InitView(Bundle savedInstanceState) {
        // 拍照按钮
        ll = (RelativeLayout) findViewById(R.id.ll);
        content = (RelativeLayout) findViewById(R.id.rl_content);
        // 监听被截取图片的触摸事件
        srcPic.setOnTouchListener(this);
        sure = (View) this.findViewById(R.id.sure);
        sure.setOnClickListener(this);
        reset = (View) this.findViewById(R.id.reset);
        reset.setOnClickListener(this);


        Intent in = getIntent();
        if (savedInstanceState != null) {
            Tip.i("重新开启Activity，处理崩溃情况");
            try {
                Tip.i("崩溃后图片路径为" + tempFile.getAbsolutePath());
                isCrash = savedInstanceState.getBoolean("isCrash");
                oldFilePath = savedInstanceState.getString("oldPath");
                takePhotoManage();
            } catch (Exception e) {
                finish();
            }
        } else {
            if (in.getStringExtra("state").equals("take_photo")) {
                Tip.i("拍照");
                currentType = PHOTO_REQUEST_TAKEPHOTO;

                launchCamera();

            } else if (in.getStringExtra("state").equals("get_photo")) {
                currentType = REQUEST_CODE_PICK_IMAGE;
                Tip.i("从相册获取");

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");// 相片类型
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                reset.setVisibility(View.GONE);
            }
        }
    }

    public void launchCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {

            cameraIntent.putExtra(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

            SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMdd_HH:mm:ss");
            String filename = timeStampFormat.format(new Date());
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, filename);
            tempUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
            startActivityForResult(cameraIntent, PHOTO_REQUEST_TAKEPHOTO);
        } else {
            Toast.makeText(this, R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Tip.i("beng l beng l beng l ");
        if (tempFile != null && currentType == PHOTO_REQUEST_TAKEPHOTO) {
            super.onSaveInstanceState(outState);
            outState.putBoolean("isCrash", true);
            outState.putString("oldPath", tempFile.getAbsolutePath());
        }
    }

    private void takePhotoManage() {
        ll.setVisibility(View.VISIBLE);
        // 拿到将要被截取的图片
        if (isCrash) {
            bitmap = Utils.getCompressImage(oldFilePath);
            isCrash = false;
        } else {
            bitmap = Utils.getCompressImage(tempFile.getAbsolutePath());
        }
        InitPhoto();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tip.i("requestCode = " + requestCode);
        Tip.i("resultCode = " + resultCode);

        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:
                operateType = PHOTO_REQUEST_TAKEPHOTO;
                Tip.i("PHOTO_REQUEST_TAKEPHOTO");
                // 截图
                if (resultCode == RESULT_OK) {

                    Uri result = null;
                    if (data != null && data.getData() != null) {
                        result = data.getData();
                    } else {
                        result = tempUri;
                    }

                    try {
                        tempFile = new File(Utils.getRealPathFromURI(ClipPictureActivity.this, result));
                        if (tempFile != null && tempFile.exists()) {
                            takePhotoManage();
                        } else {
                            Toast.makeText(ClipPictureActivity.this, "图片解析失败", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } catch (Exception e) {
                        Toast.makeText(ClipPictureActivity.this, "图片解析失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    finish();
                }

                break;
            case REQUEST_CODE_PICK_IMAGE:// 从相册中获取图片的回调接口
                operateType = REQUEST_CODE_PICK_IMAGE;
                Tip.i("REQUEST_CODE_PICK_IMAGE");
                if (resultCode == RESULT_OK) {
                    try {
                        if (data != null) {
                            Uri selectedImage = data.getData();
                            bitmap = Utils.getCompressImage(getRealPathFromURI(selectedImage));
                        }

                        InitPhoto();
                    } catch (Exception e) {
                        Toast.makeText(ClipPictureActivity.this, "图片错误", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } else {
                    ll.setVisibility(View.INVISIBLE);
                    finish();
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file
            // path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String path = cursor.getString(idx);
            cursor.close();
            return path;
        }
    }

    private void InitPhoto() {
        // 内存观察者，观察视图树的改变
        ViewTreeObserver observer = srcPic.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            public void onGlobalLayout() {
                srcPic.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                initClipView(srcPic.getTop());
            }
        });
    }

    /**
     * 初始化截图区域，并将源图按裁剪框比例缩放
     *
     * @param top
     */
    private void initClipView(int top) {

        // 自定义view
        clipview = new ClipView(ClipPictureActivity.this);

        // 自定义View设置
        clipview.setCustomTopBarHeight(top);
        // 图片按下监听事件
        clipview.addOnDrawCompleteListener(new ClipView.OnDrawListenerComplete() {

            public void onDrawCompelete() {

                // 先将之前的图片监听事件移除掉
                clipview.removeOnDrawCompleteListener();

                // 获取到截取框的高度和宽度
                int clipHeight = clipview.getClipHeight();
                int clipWidth = clipview.getClipWidth();

                // 初始化被截取图片放置的位置 将图片的中心也放在截取框内的中心位置
                int midX = clipview.getClipLeftMargin() + (clipWidth / 2);
                int midY = clipview.getClipTopMargin() + (clipHeight / 2);

                // 获取到图片的高度和宽度
                if (null != bitmap) {
                    imageWidth = bitmap.getWidth();
                    imageHeight = bitmap.getHeight();
                } else {
                    setResult(10);
                }

                // 将图片的顶点的信息存储到map中

                // 按裁剪框求缩放比例 默认的是一张竖直的图片
                float scale = (clipWidth * 1.0f) / imageWidth;
                if (imageWidth > imageHeight) {
                    scale = (clipHeight * 1.0f) / imageHeight;
                }

                // 起始中心点
                float imageMidX = imageWidth * scale / 2;
                float imageMidY = clipview.getCustomTopBarHeight() + imageHeight * scale / 2;
                srcPic.setScaleType(ScaleType.MATRIX);

                // 缩放
                matrix.postScale(scale, scale);
                // 平移
                matrix.postTranslate(midX - imageMidX, midY - imageMidY);

                srcPic.setImageMatrix(matrix);
                srcPic.setImageBitmap(bitmap);

                // 后来添加上去的
                matrix_2 = srcPic.getImageMatrix();
                rect_2 = srcPic.getDrawable().getBounds();
                values = new float[9];
                mapState = new ImageState();

            }
        });
        content.addView(clipview, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public boolean onTouch(View v, MotionEvent event) {

        // 控制图片的缩放
        ImageView view = (ImageView) v;
        frame = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
        try {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                // 多点触摸事件

                case MotionEvent.ACTION_DOWN:
                    savedMatrix.set(matrix);

                    // 设置开始点位置
                    start.set(event.getX(), event.getY());
                    // 改变模式，模式为拖拽
                    mode = DRAG;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN: // 多点触控

                    // 取得两点之间的距离
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        // 设置选中的矩形区域
                        savedMatrix.set(matrix);

                        // 得到两点之间的中心的坐标
                        midPoint(mid, event);

                        // 模式改变 模式为缩放
                        mode = ZOOM;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    // 手指抬起，模式为none
                    if (mode == ZOOM) {

                        if ((mapState.getLeft() + 1) >= (clipview.getClipLeftMargin()) || (mapState.getRight() + 1) <= (clipview.getClipLeftMargin() + clipview.getClipHeight())) {

                            matrix.set(savedMatrix);

                        }
                    }

                    // 处理，从新初始化图片的位置的参数

                    // 处理手指抬起的时候，判断图片的各个定点位置和截取框之间的关系，看他们进行左右上下的回弹
                    // 左右也要在其中判断，因为有时候会同时出现左右离开边界的同事上下也离开边界，这样就需要做左右回弹和上下回弹来解决，所以需要做出对应的判断
                    if ((mapState.getTop() + location[1]) >= (clipview.getClipTopMargin() + statusBarHeight)) {
                        matrix.postTranslate(0, -((mapState.getTop() + location[1]) - (clipview.getClipTopMargin() + statusBarHeight)));

                    } else if ((location[1] + mapState.getBottom()) <= (clipview.getClipTopMargin() + statusBarHeight + clipview.getClipHeight())) {

                        matrix.postTranslate(0, ((clipview.getClipTopMargin() + statusBarHeight + clipview.getClipHeight()) - (location[1] + mapState.getBottom())));

                    }
                    mode = NONE;
                    break;
                case MotionEvent.ACTION_MOVE: // 分为缩放，和移动

                    if (mode == DRAG) { // 简单的移动
                        matrix.set(savedMatrix);

                        float dx = event.getX() - start.x;
                        float dy = event.getY() - start.y;
                        // x 轴和 Y轴移动的距离
                /* matrix.postTranslate(dx, dy); */
                        // 这里有个问题，怎样得到移动中图片的坐标，移动的，只要能得到他的left，top，right，和buttom，下面就可以计算截取的图片的位置
                        srcPic.getLocationInWindow(location);

                        matrix_2.getValues(values);
                        mapState.setLeft(values[2]);
                        mapState.setTop(values[5]);
                        mapState.setRight(mapState.getLeft() + rect_2.width() * values[0]);
                        mapState.setBottom(mapState.getTop() + rect_2.height() * values[0]);
                        if (mapState.getRight() - mapState.getLeft() < clipview.getClipWidth()) {
                            dx = 0;
                        } else if (mapState.getLeft() + dx > 0 && dx > 0) {

                            dx = -mapState.getLeft();
                        } else if (mapState.getRight() + dx < (clipview.getClipLeftMargin() + clipview.getClipWidth()) && dx < 0) {

                            dx = (clipview.getClipLeftMargin() + clipview.getClipWidth()) - mapState.getRight();

                        }

                        // matrix.postScale(1.01f,1.01f, mid.x, mid.y);
                        matrix.postTranslate(dx, dy);

                    } else if (mode == ZOOM) { // 缩放
                        float newDist = spacing(event);
                        if (newDist > 10f) { // 两点之间的距离达到一定的程度才算是缩放，不然就不算缩放
                            matrix.set(savedMatrix);
                            float scale = newDist / oldDist; // 缩放的比例

                            matrix_2.getValues(values);
                            mapState.setLeft(values[2]);
                            mapState.setTop(values[5]);
                            mapState.setRight(mapState.getLeft() + rect_2.width() * values[0]);
                            mapState.setBottom(mapState.getTop() + rect_2.height() * values[0]);

                            matrix.postScale(scale, scale, mid.x, mid.y); // 缩放

                        }
                    }

                    break;
            }
        } catch (Exception e) {
            finish();
        }
        // 设置图片的Matrix，设置移动或者缩放过后图片的样式
        view.setImageMatrix(matrix);

        return true;
    }

    /**
     * 多点触控时，计算最先放下的两指 距离
     *
     * @param event
     * @return
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 多点触控时，计算最先放下的两指 中心坐标
     *
     * @param point
     * @param event
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);

        // 设置中心点的坐标
        point.set(x / 2, y / 2);
    }

    // 确认保存截图 设置头像 同时上传到服务器上
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sure:
                Bitmap clipBitmap = getBitmap();
                try {
                    FileOutputStream fos = new FileOutputStream(tempFile);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    clipBitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos); // 写到文件中
//                    clipBitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos); // 写到内存，进行跳转显示
//                    byte[] bitmapByte = baos.toByteArray();
                    Intent intent = new Intent();
                    intent.putExtra(RESULT_PHOTO_PATH, tempFile.getAbsolutePath());
                /* startActivity(intent); */

                    setResult(200, intent);
                    finish();
                    baos.close();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.reset: {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定调用相机拍照后照片的储存路径
                if (tempFile != null && tempFile.exists()) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                }
                startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
            }
            default:
                break;
        }

    }

    /**
     * 获取裁剪框内所截取到的图片 如果是铺满或则大于剪裁框，取得剪裁框中的内容，如果小于，只取出图片的部分
     *
     * @return
     */
    private Bitmap getBitmap() {
        // 获取截屏
        View view = this.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        Rect frame = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        // 真正截图 需要处理，在截图的时候获取到的是图片

        Bitmap finalBitmap = null;

		/*
         * 扩展，只截取有图片的部分 ，还没完成
		 */
        int bitleftwidth = 0;
        float bitleft_1 = mapState.getLeft();
        float bittop_1 = mapState.getTop();
        float bitright = mapState.getRight();
        float bitbuttom = mapState.getBottom();

        int bitleft = new Float(bitleft_1).intValue() + 1;
        int bittop = new Float(bittop_1).intValue() + 1;

        int scaleImageWidth = new Float(bitright - bitleft_1 + 1).intValue() - 1;
        int scaleImageHeight = new Float(bitbuttom - bittop_1 + 1).intValue();

        imageHeight = scaleImageHeight;
        imageWidth = scaleImageWidth;

        finalBitmap = Bitmap.createBitmap(view.getDrawingCache(), clipview.getClipLeftMargin(), clipview.getClipTopMargin() + statusBarHeight, clipview.getClipWidth(), clipview.getClipHeight());
        // 释放资源
        view.destroyDrawingCache();
        return finalBitmap;
    }

    // 图片名字
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    // 存储图片四个位置的变量
    public class ImageState {
        private float left;
        private float top;
        private float right;
        private float bottom;

        public float getLeft() {
            return left;
        }

        public void setLeft(float left) {
            this.left = left;
        }

        public float getTop() {
            return top;
        }

        public void setTop(float top) {
            this.top = top;
        }

        public float getRight() {
            return right;
        }

        public void setRight(float right) {
            this.right = right;
        }

        public float getBottom() {
            return bottom;
        }

        public void setBottom(float bottom) {
            this.bottom = bottom;
        }

    }

}
