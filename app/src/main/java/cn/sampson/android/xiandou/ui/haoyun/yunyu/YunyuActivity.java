package cn.sampson.android.xiandou.ui.haoyun.yunyu;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.attention.PregnancyAttentionActivity;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.fortyweeks.FortyWeeksActivity;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.taijiaoyinyue.TaijiaoYinyueActivity;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.taijiaozhishi.TaijiaoZhishiActivity;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.twohundredeighty.TwoHundredEightyActivity;
import cn.sampson.android.xiandou.utils.ScreenUtils;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;

/**
 * 孕育主页
 * Created by chengyang on 2017/6/21.
 */

public class YunyuActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rlTitle)
    RelativeLayout rlTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.StatusBarLightMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yun_yu);
        ButterKnife.bind(this);
        StatusBarUtil.transparencyBar(this);
        initSystemBar();
        tvTitle.setText(getString(R.string.yunyu));
    }

    /**
     * 沉浸式状态栏
     */
    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int top = ScreenUtils.getSystemBarHeight();
            ((RelativeLayout.LayoutParams) rlTitle.getLayoutParams()).topMargin = top;
        }
    }

    @OnClick(R.id.huaiyun40zhou)
    public void jumpToHuaiyun40zhou() {
        jumpTo(FortyWeeksActivity.class);
    }

    @OnClick(R.id.m280tian)
    public void jumpTo280Tian() {
        jumpTo(TwoHundredEightyActivity.class);
    }

    @OnClick(R.id.yunqizhuyi)
    public void jumpToYunqizhuyi() {
        jumpTo(PregnancyAttentionActivity.class);
    }

    @OnClick(R.id.taijiaoyinyue)
    public void jumpToTaijiaoYinyue() {
        jumpTo(TaijiaoYinyueActivity.class);
    }

    @OnClick(R.id.taijiaozhishi)
    public void jumpToTaijiaozhishi() {
        jumpTo(TaijiaoZhishiActivity.class);
    }

}
