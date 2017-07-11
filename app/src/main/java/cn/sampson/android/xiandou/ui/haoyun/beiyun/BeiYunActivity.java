package cn.sampson.android.xiandou.ui.haoyun.beiyun;

import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.utils.ScreenUtils;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;

/**
 * 备孕主页
 */
public class BeiYunActivity extends BaseActivity {

    @Bind(R.id.tv_pregnancy_ratio)
    TextView tvPregnancyRatio;
    @Bind(R.id.tv_grade)
    TextView tvGrade;
    @Bind(R.id.baidailasi)
    LinearLayout baidailasi;
    @Bind(R.id.bchaocepai)
    LinearLayout bchaocepai;
    @Bind(R.id.qiyuan)
    LinearLayout qiyuan;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rlTitle)
    RelativeLayout rlTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setContentView(R.layout.activity_bei_yun);
        ButterKnife.bind(this);
        StatusBarUtil.transparencyBar(this);
        initSystemBar();
        tvTitle.setText(getString(R.string.beiyun));
    }

    @OnClick(R.id.fl_back)
    public void onBackClick() {
        onBackPressed();
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

    @OnClick(R.id.fl_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.zhangzishi)
    public void jumpToZhangzishi() {
        jumpTo(ZhangZiShiActivity.class);
    }

}
