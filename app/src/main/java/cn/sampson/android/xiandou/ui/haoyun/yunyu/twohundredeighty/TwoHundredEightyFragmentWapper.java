package cn.sampson.android.xiandou.ui.haoyun.yunyu.twohundredeighty;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.utils.ContextUtil;
import cn.sampson.android.xiandou.utils.widget.IFragmentsWrapper;

/**
 * Created by chengyang on 2017/6/6.
 */

public class TwoHundredEightyFragmentWapper implements IFragmentsWrapper {

    private String[] titles;
    private ArrayList<Fragment> mFragments;

    public TwoHundredEightyFragmentWapper() {
        titles = ContextUtil.getResource().getStringArray(R.array.two_hundred_eighty);
        mFragments = new ArrayList<>(titles.length);
        for (int i = 0; i < titles.length; i++) {
            initFragment(i);
        }
    }

    private void initFragment(int index) {
        Fragment f = new TwoHundredEightyFragment();
        Bundle args = new Bundle();
        args.putInt(TwoHundredEightyFragment.TYPE, index);
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
