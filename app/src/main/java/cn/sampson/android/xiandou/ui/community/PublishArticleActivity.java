package cn.sampson.android.xiandou.ui.community;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.config.AppCache;
import cn.sampson.android.xiandou.core.manager.UpdatePhotoManager;
import cn.sampson.android.xiandou.core.presenter.CommunityPublishPresenter;
import cn.sampson.android.xiandou.core.retroft.Api.CommunityApi;
import cn.sampson.android.xiandou.core.retroft.RetrofitWapper;
import cn.sampson.android.xiandou.core.retroft.base.BasePresenter;
import cn.sampson.android.xiandou.core.retroft.base.IView;
import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.ui.community.domain.CommunityCategory;
import cn.sampson.android.xiandou.utils.ContextUtil;
import cn.sampson.android.xiandou.utils.FileUtils;
import cn.sampson.android.xiandou.utils.Tip;
import cn.sampson.android.xiandou.utils.imageloader.ImageLoader;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;
import cn.sampson.android.xiandou.widget.richtext.RichTextEditor;
import cn.sampson.android.xiandou.widget.richtext.editimage.IImageLoader;
import cn.sampson.android.xiandou.widget.richtext.editimage.IUploadEngine;
import cn.sampson.android.xiandou.widget.richtext.editimage.YUtils;

/**
 * Created by chengyang on 2017/8/14.
 */

public class PublishArticleActivity extends BaseActivity implements IView {

    @Bind(R.id.et_title)
    EditText etTitle;
    @Bind(R.id.et_new_content)
    RichTextEditor etNewContent;
    @Bind(R.id.fl_img)
    FrameLayout flImg;
    @Bind(R.id.rl_function)
    RelativeLayout rlFunction;
    @Bind(R.id.ll_guide)
    LinearLayout llGuide;
    @Bind(R.id.ll_tag_container)
    LinearLayout llTagContainer;

    private static final int MAX_TRY_UPLOAD_TIME = 10;

    private View clickedView;

    private Map<Uri, UpdatePhotoManager> taskMap = new HashMap<>();
    private boolean isPosting;
    private boolean mPicEnable;

    private int tryPostTime;
    private String mLabel;
    private Handler uiHandler = new Handler();

    CommunityPublishPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setContentView(R.layout.activity_publish_article);
        setActionBarBack();
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mPresenter = new CommunityPublishPresenterImpl(this);
        //现实所有标签
        showTags();

        //添加图片
        flImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPicEnable)
                    getImageFromMatisse();
            }
        });

        etTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b == true) {
                    mPicEnable = false;
                } else {
                    mPicEnable = true;
                }
            }
        });
        //设置图片加载器
        etNewContent.setImageLoader(new IImageLoader() {
            @Override
            public void loadIntoImageView(ImageView imageView, Uri uri) {
                ImageLoader.getPicasso(PublishArticleActivity.this).load(uri).into(imageView);
            }
        });
        //设置上传
        etNewContent.setUploadEngine(new IUploadEngine() {
            @Override
            public void uploadImage(final Uri imgUri, final UploadProgressListener listener) {
                Tip.i("filename ======> " + FileUtils.getRealPathFromURI(imgUri));

                UpdatePhotoManager task = new UpdatePhotoManager();
                taskMap.put(imgUri, task);

                task.setUpdateType(UpdatePhotoManager.TYPE_PHOTO)
                        .setCallBack(new UpdatePhotoManager.PictureUpdateProgressCallback() {
                            @Override
                            public void progress(double percent) {
                                listener.onUploadProgress(imgUri, (int) percent * 100);
                            }
                        }, new UpdatePhotoManager.PictureUpdateResultCallBack() {
                            @Override
                            public void onSuccess(String imageUrl) {
                                int[] wh = YUtils.getImageSize(PublishArticleActivity.this, imgUri);
                                listener.onUploadSuccess(imgUri, imageUrl, wh[0], wh[1]);
                            }

                            @Override
                            public void onFailed() {
                                listener.onUploadFail(imgUri, "upload fail");
                            }
                        }).updatePhoto(new File(FileUtils.getRealPathFromURI(imgUri)));
            }

            @Override
            public void cancelUpload(Uri imgUrl) {
                UpdatePhotoManager task = taskMap.get(imgUrl);
                if (task != null && !task.isCancelled()) {
                    task.cancelUpdate();
                }
            }
        });
    }

    /**
     * 现实当前已有的tag
     */
    private void showTags() {
        final List<CommunityCategory> categories = AppCache.getCommunityCategories();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ContextUtil.dip2Px(26));
        params.setMargins(ContextUtil.dip2Px(6), 0, ContextUtil.dip2Px(6), 0);

        for (int i = 0; i < categories.size(); i++) {
            TextView view = (TextView) LayoutInflater.from(PublishArticleActivity.this).inflate(R.layout.layout_tag, null);
            view.setText("#" + categories.get(i).name);
            view.setTag(categories.get(i).tag);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickedView != null) {
                        clickedView.setSelected(false);
                    }
                    mLabel = view.getTag().toString();
                    view.setSelected(true);
                    clickedView = view;
                }
            });
            view.setLayoutParams(params);
            llTagContainer.addView(view);
        }
    }

    /**
     * 按钮
     */
    private void onPostClick() {
        if (isPosting) {
            return;
        }

        if (TextUtils.isEmpty(mLabel)) {
            Toast.makeText(this, "请先选择一个分类～", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(etTitle.getText())) {
            Toast.makeText(this, "请给一个标题哟～", Toast.LENGTH_SHORT).show();
            return;
        }

        String htmlContent = etNewContent.getHtmlData();
        if (YUtils.isEmpty(htmlContent)) {
            Toast.makeText(this, "内容不能为空～", Toast.LENGTH_SHORT).show();
            return;
        }

        isPosting = true;
        showLoadingDialog();

        tryAndPostContent();
    }

    /**
     * 点击发布之后，检测图片是否都是上传成功，然后将内容转换成html
     */
    private void tryAndPostContent() {
        //看看是不是所有的图片都上传成功了
        int uploadStatus = etNewContent.tryIfSuccessAndReUpload();

        if (IUploadEngine.STATUS_UPLOAD_SUCCESS == uploadStatus) {
            String htmlContent = etNewContent.getHtmlData();
            //上传到服务器
            mPresenter.publishArticle(etTitle.getText().toString(), htmlContent, mLabel);
        } else {
            if (tryPostTime == MAX_TRY_UPLOAD_TIME) {
                Toast.makeText(this, "上传图片超时，请重新提交。", Toast.LENGTH_SHORT).show();
                resetPosting();
                return;
            }
            tryPostTime++;
            uiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tryAndPostContent();
                }
            }, 1000);
        }
    }

    private void resetPosting() {
        hideLoadingDialog();
        tryPostTime = 0;
        isPosting = false;
    }

    /**
     * 从matisse插件中获取到图片
     */
    @Override
    protected void getImgUrlsFromMatisse(List<Uri> imgUris) {
        for (Uri imgUri : imgUris) {
            etNewContent.insertImage(imgUri);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiHandler.removeCallbacksAndMessages(null);
        for (UpdatePhotoManager task : taskMap.values()) {
            task.cancelUpdate();
        }
    }

    private ProgressDialog progressDialog;

    private void showLoadingDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("提交中");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    private void hideLoadingDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_publish, menu);
        return true;
    }

    @Override
    protected void onToolbarPublish() {
        onPostClick();
    }

    void publishArticleComplete() {
        hideLoadingDialog();
        //这里我们简单退出
        Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void setError(int errorCode, String error, String key) {
        hideLoadingDialog();
    }

    class CommunityPublishPresenterImpl extends BasePresenter<PublishArticleActivity> implements CommunityPublishPresenter {

        public CommunityPublishPresenterImpl(PublishArticleActivity view) {
            super(view);
        }

        @Override
        public void publishArticle(String title, String content, String tag) {
            requestData(RetrofitWapper.getInstance().getNetService(CommunityApi.class).publishArticle(title, content, tag), PUBLISH_ARTICLE);
        }

        @Override
        protected void onResult(Result result, String key) {
            switch (key) {
                case PUBLISH_ARTICLE:
                    publishArticleComplete();
                    break;
            }
        }
    }
}
