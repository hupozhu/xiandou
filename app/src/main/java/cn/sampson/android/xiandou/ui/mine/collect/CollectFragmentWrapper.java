package cn.sampson.android.xiandou.ui.mine.collect;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import cn.sampson.android.xiandou.ui.haoyun.ArticleListFragment;
import cn.sampson.android.xiandou.widget.IFragmentsWrapper;

/**
 * Created by chengyang on 2017/7/13.
 */

public class CollectFragmentWrapper implements IFragmentsWrapper {

    public String[] titles = new String[]{"收藏的帖子", "收藏的资讯"};

    ArrayList<Fragment> mFragment;

    public CollectFragmentWrapper() {
        mFragment = new ArrayList<>();
        mFragment.add(new CollectPostFragment());
        mFragment.add(new CollectNewsFragment());
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
        return mFragment.get(index);
    }

    @Override
    public int getTitleImg(int index) {
        return 0;
    }
}
