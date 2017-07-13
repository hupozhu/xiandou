package cn.sampson.android.xiandou.ui.main;

import android.support.v4.app.Fragment;

import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.community.CommunityFragment;
import cn.sampson.android.xiandou.ui.haoyun.HaoYunFragment;
import cn.sampson.android.xiandou.ui.mine.MineFragment;
import cn.sampson.android.xiandou.utils.ContextUtil;
import cn.sampson.android.xiandou.widget.IFragmentsWrapper;

/**
 * Created by chengyang on 2017/1/4.
 */

public class MainTabWrapper implements IFragmentsWrapper {

    private String[] mTitles;
    private int[] mTitleImgs;

    public MainTabWrapper() {
        mTitles = new String[]{
                ContextUtil.getString(R.string.haoyun),
                ContextUtil.getString(R.string.shequ),
                ContextUtil.getString(R.string.wode)
        };

        mTitleImgs = new int[]{
                R.drawable.selector_main_haoyun,
                R.drawable.selector_main_community,
                R.drawable.selector_main_mine
        };
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public String getTitle(int index) {
        return mTitles[index];
    }

    @Override
    public Fragment getFragment(int index) {
        return null;
    }

    @Override
    public int getTitleImg(int index) {
        return mTitleImgs[index];
    }
}
