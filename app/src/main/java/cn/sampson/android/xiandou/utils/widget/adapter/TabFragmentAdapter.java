package cn.sampson.android.xiandou.utils.widget.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.sampson.android.xiandou.utils.widget.IFragmentsWrapper;


/**
 * Created by Administrator on 2016/4/13.
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {


    IFragmentsWrapper mWrapper;

    public TabFragmentAdapter(FragmentManager fm, IFragmentsWrapper wrapper) {
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
