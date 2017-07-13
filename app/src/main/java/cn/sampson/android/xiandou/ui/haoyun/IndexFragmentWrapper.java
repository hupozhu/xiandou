package cn.sampson.android.xiandou.ui.haoyun;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import cn.sampson.android.xiandou.widget.IFragmentsWrapper;

/**
 * Created by chengyang on 2017/7/13.
 */

public class IndexFragmentWrapper implements IFragmentsWrapper {

    public String[] titles = new String[]{"热门", "最新"};

    ArrayList<ArticleListFragment> mFragment;

    public IndexFragmentWrapper() {
        mFragment = new ArrayList<>();
        for (int i = 1; i <= titles.length; i++) {
            mFragment.add(createFragment(i));
        }
    }

    private ArticleListFragment createFragment(int index) {
        ArticleListFragment f = new ArticleListFragment();
        Bundle args = new Bundle();
        args.putInt(ArticleListFragment.TYPE, index);
        f.setArguments(args);
        return f;
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
