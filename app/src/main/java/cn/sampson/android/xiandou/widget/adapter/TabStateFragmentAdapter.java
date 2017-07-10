package cn.sampson.android.xiandou.widget.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import cn.sampson.android.xiandou.widget.IFragmentsWrapper;


/**
 * Created by Administrator on 2016/4/13.
 */
public class TabStateFragmentAdapter extends FragmentStatePagerAdapter {


    IFragmentsWrapper mWrapper;

    public TabStateFragmentAdapter(FragmentManager fm, IFragmentsWrapper wrapper) {
        super(fm);
        mWrapper = wrapper;
    }

    @Override
    public Fragment getItem(int position) {
        return mWrapper.getFragment(position);
    }

    @Override
    public int getCount() {
        return mWrapper.getCount();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mWrapper.getTitle(position);
    }
}
