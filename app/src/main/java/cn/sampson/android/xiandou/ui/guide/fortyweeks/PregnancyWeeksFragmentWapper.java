package cn.sampson.android.xiandou.ui.guide.fortyweeks;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.utils.ContextUtil;
import cn.sampson.android.xiandou.utils.widget.IFragmentsWrapper;

/**
 * Created by chengyang on 2017/6/6.
 */

public class PregnancyWeeksFragmentWapper implements IFragmentsWrapper {

    private String[] titles;
    private ArrayList<Fragment> mFragments;

    public PregnancyWeeksFragmentWapper() {
        titles = ContextUtil.getResource().getStringArray(R.array.pregnancy_week);
        mFragments = new ArrayList<>(titles.length);
        for (int i = 0; i < titles.length; i++) {
            initFragment(i);
        }
    }

    private void initFragment(int index) {
        Fragment f = new PregnancyWeeksFragment();
        Bundle args = new Bundle();
        args.putInt(PregnancyWeeksFragment.TYPE, index);
        f.setArguments(args);
        mFragments.add(index, f);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public String getTitle(int index) {
        return titles[index];
    }

    @Override
    public Fragment getFragment(int index) {
        return mFragments.get(index);
    }

    @Override
    public int getTitleImg(int index) {
        return 0;
    }
}
