package cn.sampson.android.xiandou.ui.mine.reply;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

import cn.sampson.android.xiandou.ui.mine.collect.CollectNewsFragment;
import cn.sampson.android.xiandou.ui.mine.collect.CollectPostFragment;
import cn.sampson.android.xiandou.widget.IFragmentsWrapper;

/**
 * Created by chengyang on 2017/7/13.
 */

public class ReplyFragmentWrapper implements IFragmentsWrapper {

    public String[] titles = new String[]{"帖子回复", "资讯回复"};

    ArrayList<Fragment> mFragment;

    public ReplyFragmentWrapper() {
        mFragment = new ArrayList<>();
        mFragment.add(new ReplyPostFragment());
        mFragment.add(new ReplyNewsFragment());
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
