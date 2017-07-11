package cn.sampson.android.xiandou.ui.haoyun.yuer;

import android.content.Intent;
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
import cn.sampson.android.xiandou.ui.haoyun.yunyu.info.StoryActivity;
import cn.sampson.android.xiandou.utils.ScreenUtils;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;

/**
 * Created by chengyang on 2017/6/21.
 */

public class YuerActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rlTitle)
    RelativeLayout rlTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setContentView(R.layout.activity_yu_er);
        ButterKnife.bind(this);

        StatusBarUtil.transparencyBar(this);
        initSystemBar();
        tvTitle.setText(getString(R.string.yuer));
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

    @OnClick(R.id.yiqianlinyiye)
    public void jumpToYiqianlingyiye() {
        Intent intentYiqianyiye = new Intent(YuerActivity.this, StoryActivity.class);
        intentYiqianyiye.putExtra(StoryActivity.TITLE, getString(R.string.yiqianyiye));
        intentYiqianyiye.putExtra(StoryActivity.TYPE, 3);
        startActivity(intentYiqianyiye);
    }

    @OnClick(R.id.antusheng)
    public void jumpToAntusheng() {
        Intent intentAntusheng = new Intent(YuerActivity.this, StoryActivity.class);
        intentAntusheng.putExtra(StoryActivity.TITLE, getString(R.string.antusheng));
        intentAntusheng.putExtra(StoryActivity.TYPE, 2);
        startActivity(intentAntusheng);
    }

    @OnClick(R.id.gelin)
    public void jumpToGelin() {
        Intent intentGeling = new Intent(YuerActivity.this, StoryActivity.class);
        intentGeling.putExtra(StoryActivity.TITLE, getString(R.string.gelintonghua));
        intentGeling.putExtra(StoryActivity.TYPE, 1);
        startActivity(intentGeling);
    }
}
