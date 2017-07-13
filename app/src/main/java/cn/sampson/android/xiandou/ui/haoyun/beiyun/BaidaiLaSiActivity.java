package cn.sampson.android.xiandou.ui.haoyun.beiyun;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.utils.imageloader.ImageLoader;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;

/**
 * Created by chengyang on 2017/7/13.
 */

public class BaidaiLaSiActivity extends BaseActivity {
    @Bind(R.id.tv_content)
    ImageView tvContent;

//    @Bind(R.id.large_image)
//    LargeImageView largeImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setContentView(R.layout.activity_baidailasi);
        setActionBarBack();
        ButterKnife.bind(this);

//        try {
//            InputStream inputStream = getAssets().open("baidailasi.png");
//            largeImage.setInputStream(inputStream);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        ImageLoader.getPicasso(this).load(R.mipmap.baidailasi).config(Bitmap.Config.RGB_565).fit().into(tvContent);
    }
}
