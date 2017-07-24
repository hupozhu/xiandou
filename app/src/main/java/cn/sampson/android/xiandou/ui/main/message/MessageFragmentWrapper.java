package cn.sampson.android.xiandou.ui.main.message;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

import cn.sampson.android.xiandou.ui.mine.collect.CollectNewsFragment;
import cn.sampson.android.xiandou.ui.mine.collect.CollectPostFragment;
import cn.sampson.android.xiandou.widget.IFragmentsWrapper;

/**
 * Created by chengyang on 2017/7/13.
 */

public class MessageFragmentWrapper implements IFragmentsWrapper {

    public String[] titles = new String[]{"资讯消息", "帖子消息"};

    ArrayList<Fragment> mFragment;

    public MessageFragmentWrapper() {
        mFragment = new ArrayList<>();
        mFragment.add(new MessageNewsFragment());
        mFragment.add(new MessagePostFragment());
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
