package cn.sampson.android.xiandou.utils.widget;

import android.support.v4.app.Fragment;

public interface IFragmentsWrapper {

    int getCount();

    String getTitle(int index);

    Fragment getFragment(int index);

    int getTitleImg(int index);

}