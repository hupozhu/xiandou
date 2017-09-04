package cn.sampson.android.xiandou.ui.haoyun.yuerbaojian;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import cn.sampson.android.xiandou.widget.IFragmentsWrapper;

/**
 * Created by Administrator on 2017/9/4 0004.
 */

public class YuerbaojianFragmentWrapper implements IFragmentsWrapper {

    public String[] titles = new String[]{"春季", "夏季", "秋季", "冬季"};

    ArrayList<YuerbaojianFragment> mFragment;

    public YuerbaojianFragmentWrapper() {
        mFragment = new ArrayList<>();
        for (int i = 1; i <= titles.length; i++) {
            mFragment.add(createFragment(i));
        }
    }

    private YuerbaojianFragment createFragment(int index) {
        YuerbaojianFragment f = new YuerbaojianFragment();
        Bundle args = new Bundle();
        args.putString(YuerbaojianFragment.TYPE, String.valueOf(index+40));
        f.setArguments(args);
        return f;
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }

    @Override
    public String getTitle(int index) {
        return titles[index];
    }

    @Override
    public Fragment getFragment(int index) {
        return mFragment.get(index);
    }

    @Override
    public int getTitleImg(int index) {
        return 0;
    }
}
