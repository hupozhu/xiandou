package cn.sampson.android.xiandou.ui.haoyun.beiyun.qiyuan;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import cn.sampson.android.xiandou.widget.IFragmentsWrapper;

/**
 * Created by chengyang on 2017/7/13.
 */

public class QiyuanFragmentWrapper implements IFragmentsWrapper {

    public String[] titles = new String[]{"我的祈愿", "众愿墙"};

    ArrayList<QiyuanFragment> mFragment;

    public QiyuanFragmentWrapper() {
        mFragment = new ArrayList<>();
        for (int i = 1; i <= titles.length; i++) {
            mFragment.add(createFragment(i));
        }
    }

    private QiyuanFragment createFragment(int index) {
        QiyuanFragment f = new QiyuanFragment();
        Bundle args = new Bundle();
        if (index == 1) {
            args.putInt(QiyuanFragment.TYPE, 1);
        } else {
            args.putInt(QiyuanFragment.TYPE, 0);
        }
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
