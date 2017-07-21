package cn.sampson.android.xiandou.ui.haoyun.beiyun;

import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;
import cn.sampson.android.xiandou.widget.largeimage.ImageSource;
import cn.sampson.android.xiandou.widget.largeimage.SubsamplingScaleImageView;

/**
 * Created by chengyang on 2017/7/13.
 */

public class BaidaiLaSiActivity extends BaseActivity {

    @Bind(R.id.image)
    SubsamplingScaleImageView image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setContentView(R.layout.activity_big_image);
        setActionBarBack();
        ButterKnife.bind(this);

        image.setImage(ImageSource.asset("baidailasi.png"));

    }
}
