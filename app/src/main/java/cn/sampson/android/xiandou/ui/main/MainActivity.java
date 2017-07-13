package cn.sampson.android.xiandou.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.core.AppCache;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.ui.community.CommunityFragment;
import cn.sampson.android.xiandou.ui.haoyun.HaoYunFragment;
import cn.sampson.android.xiandou.ui.mine.MineFragment;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;
import cn.sampson.android.xiandou.widget.TabAdapter;
import cn.sampson.android.xiandou.widget.TabFragmentAdapter;

public class MainActivity extends BaseActivity {

    private static String POSITION = "position";

    @Bind(R.id.tabs)
    TabLayout mTabs;

    private TabAdapter mPagerAdapter;

    private HaoYunFragment mHaoyunFragment;
    private CommunityFragment mComunityFragment;
    private MineFragment mMineFragment;
    private Fragment originFragment;

    int currentTab;

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
        mPagerAdapter = new TabAdapter(this, new MainTabWrapper());
        mTabs.setTabMode(TabLayout.MODE_FIXED);
        mTabs.setSelectedTabIndicatorColor(Color.TRANSPARENT);
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {
            TabLayout.Tab tab = mTabs.newTab();
            tab.setCustomView(mPagerAdapter.getTabView(i));
            mTabs.addTab(tab);
        }
        mTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //选中的fragment
                showFragment(tab.getPosition());
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
        mTabs.getTabAt(0).getCustomView().setSelected(true);
        openHaoyunFragment();
    }

    /**
     * 切换fregment
     */
    public void jumpToFragment(Fragment fragment, boolean isAdd) {
        FragmentTransaction mFt = getSupportFragmentManager().beginTransaction();
        if (originFragment != null)
            mFt.hide(originFragment);
        if (isAdd) {
            mFt.add(R.id.container, fragment);
        } else {
            mFt.show(fragment);
        }
        mFt.commit();
        originFragment = fragment;
    }

    /**
     * 打开好孕页面
     */
    public void openHaoyunFragment() {
        boolean add = false;
        if (mHaoyunFragment == null) {
            add = true;
            mHaoyunFragment = HaoYunFragment.getInstance();
        }

        if (currentTab == 1) {
            //当前页面
        } else {
            jumpToFragment(mHaoyunFragment, add);
        }
        currentTab = 1;
    }

    /**
     * 打开社区页面
     */
    public void openCommunityFragment() {
        boolean add = false;
        if (mComunityFragment == null) {
            add = true;
            mComunityFragment = CommunityFragment.getInstance();
        }

        if (currentTab == 2) {
            //当前页面
        } else {
            jumpToFragment(mComunityFragment, add);
        }
        currentTab = 2;
    }

    /**
     * 打开我的页面
     */
    public void openMineFragment() {
        boolean add = false;
        if (mMineFragment == null) {
            add = true;
            mMineFragment = MineFragment.getInstance();
        }
        if (currentTab == 3) {

        } else {
            jumpToFragment(mMineFragment, add);
        }
        currentTab = 3;
    }

    public void showFragment(int position) {
        switch (position) {
            case 0:
                openHaoyunFragment();
                break;
            case 1:
                openCommunityFragment();
                break;
            case 2:
                openMineFragment();
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, mTabs.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        showFragment(savedInstanceState.getInt(POSITION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppCache.setMainActiivty(null);
    }
}
