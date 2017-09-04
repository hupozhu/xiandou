package cn.sampson.android.xiandou.ui.haoyun.yuerbaojian;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;
import cn.sampson.android.xiandou.widget.adapter.TabStateFragmentAdapter;

/**
 * Created by Administrator on 2017/9/4 0004.
 */

public class YuerBaojianActivity extends BaseActivity {

    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.pager)
    ViewPager pager;

    private TabStateFragmentAdapter mPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setContentView(R.layout.activity_yuer_baojian);
        setActionBarBack();
        ButterKnife.bind(this);

        mPagerAdapter = new TabStateFragmentAdapter(getSupportFragmentManager(), new YuerbaojianFragmentWrapper());
        pager.setAdapter(mPagerAdapter);
        tabs.setupWithViewPager(pager);
    }
}
