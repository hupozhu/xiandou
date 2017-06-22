package cn.sampson.android.xiandou.ui.haoyun.yunyu.taijiaozhishi;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.fortyweeks.PregnancyWeeksFragment;
import cn.sampson.android.xiandou.utils.ContextUtil;
import cn.sampson.android.xiandou.utils.widget.IFragmentsWrapper;

/**
 * Created by chengyang on 2017/6/21.
 */

public class TaijiaoFragmentWapper implements IFragmentsWrapper {

    private String[] mTitle;
    private ArrayList<Fragment> mFragments;

    public TaijiaoFragmentWapper() {
        mTitle = ContextUtil.getResource().getStringArray(R.array.taijiao);
        mFragments = new ArrayList<>(mTitle.length);
        for (int i = 0; i < mTitle.length; i++) {
            initFragment(i);
        }
    }

    private void initFragment(int index) {
        Fragment f = new TaijiaoListFragment();
        Bundle args = new Bundle();
        args.putInt(TaijiaoListFragment.TYPE, index);
        f.setArguments(args);
        mFragments.add(index, f);
    }

    @Override
    public int getCount() {
        return mTitle.length;
    }

    @Override
    public String getTitle(int index) {
        return mTitle[index];
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
