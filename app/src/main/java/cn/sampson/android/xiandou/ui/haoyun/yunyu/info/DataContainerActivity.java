package cn.sampson.android.xiandou.ui.haoyun.yunyu.info;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.model.MyString;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.info.InformationFragment;

/**
 * Created by chengyang on 2017/6/15.
 */

public class DataContainerActivity extends BaseActivity {

    protected List<MyString> listItems = new ArrayList<>();
    protected List<String> datas = new ArrayList<>();

    public List<MyString> getList() {
        return listItems;
    }

    public void jumpToInfoPage(int pos) {
        Bundle bundle = new Bundle();
        bundle.putString("info", datas.get(pos));
        Fragment f = new InformationFragment();
        f.setArguments(bundle);
        showFragment(f);
    }

    protected void showFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_container, fragment);
        ft.addToBackStack("InfoStack");
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 1) {
            super.onBackPressed();
        } else {
            finish();
        }
    }

}
