package cn.sampson.android.xiandou.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.core.AppCache;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;
import cn.sampson.android.xiandou.utils.widget.TabFragmentAdapter;

public class MainActivity extends BaseActivity {

    private static String POSITION = "position";

    @Bind(R.id.pager)
    ViewPager mPager;
    @Bind(R.id.tabs)
    TabLayout mTabs;

    private TabFragmentAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //用来判断首页是否存在
        AppCache.setMainActiivty(this);
        initView();
    }

    protected void initView() {
        initPager();
    }

    private void initPager() {
        mPagerAdapter = new TabFragmentAdapter(this, getSupportFragmentManager(), new MainFragmentWrapper());
        mPager.setAdapter(mPagerAdapter);
        mTabs.setupWithViewPager(mPager);
        mTabs.setTabMode(TabLayout.MODE_FIXED);
        mTabs.setSelectedTabIndicatorColor(Color.TRANSPARENT);
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            TabLayout.Tab tab = mTabs.getTabAt(i);
            tab.setCustomView(mPagerAdapter.getTabView(i));
        }
        mTabs.getTabAt(0).getCustomView().setSelected(true);
        mTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPager.setCurrentItem(tab.getPosition());
                tab.getCustomView().setSelected(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getCustomView().setSelected(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, mTabs.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPager.setCurrentItem(savedInstanceState.getInt(POSITION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppCache.setMainActiivty(null);
    }
}
