package cn.sampson.android.xiandou.ui.main;

import android.support.v4.app.Fragment;

import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.guide.GuideFragment;
import cn.sampson.android.xiandou.ui.settings.SettingsFragment;
import cn.sampson.android.xiandou.ui.training.TrainingFragment;
import cn.sampson.android.xiandou.utils.ContextUtil;
import cn.sampson.android.xiandou.utils.widget.IFragmentsWrapper;

/**
 * Created by chengyang on 2017/1/4.
 */

public class MainFragmentWrapper implements IFragmentsWrapper {

    private String[] mTitles;
    private Fragment[] mFragments;
    private int[] mTitleImgs;

    public MainFragmentWrapper() {

        mTitles = new String[]{
                ContextUtil.getString(R.string.yunyu_zhinan),
                ContextUtil.getString(R.string.meiyue_taijiao),
                ContextUtil.getString(R.string.settings)
        };

        mFragments = new Fragment[]{
                new GuideFragment(),
                new TrainingFragment(),
                new SettingsFragment()
        };

        mTitleImgs = new int[]{
                R.drawable.selector_main_guide,
                R.drawable.selector_main_training,
                R.drawable.selector_main_settings
        };
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

    @Override
    public String getTitle(int index) {
        return mTitles[index];
    }

    @Override
    public Fragment getFragment(int index) {
        return mFragments[index];
    }

    @Override
    public int getTitleImg(int index) {
        return mTitleImgs[index];
    }
}
