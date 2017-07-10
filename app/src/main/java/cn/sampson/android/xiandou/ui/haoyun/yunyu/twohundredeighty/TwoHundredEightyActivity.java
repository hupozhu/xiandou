package cn.sampson.android.xiandou.ui.haoyun.yunyu.twohundredeighty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.db.ReadEveryDayDBHelper;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;
import cn.sampson.android.xiandou.widget.adapter.TabStateFragmentAdapter;

/**
 * Created by chengyang on 2017/6/7.
 */

public class TwoHundredEightyActivity extends BaseActivity {

    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.fl_root)
    FrameLayout flRoot;

    private TabStateFragmentAdapter mPagerAdapter;
    private Map<Integer, String> infos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setActionBarBack();
        
        setContentView(R.layout.activity_two_hundred_eighty);
        ButterKnife.bind(this);

        getDataFromDB();
    }

    @Override
    protected void onDataComplete() {
        removeLoadingView(flRoot);
        mPagerAdapter = new TabStateFragmentAdapter(getSupportFragmentManager(), new TwoHundredEightyFragmentWapper());
        pager.setAdapter(mPagerAdapter);
        tabs.setupWithViewPager(pager);
    }

    public Map<Integer, String> getInfos() {
        return infos;
    }

    private void getDataFromDB() {
        showLoadingView(flRoot);

        new Thread(new Runnable() {
            @Override
            public void run() {
                ReadEveryDayDBHelper helper = new ReadEveryDayDBHelper(TwoHundredEightyActivity.this);
                infos = helper.getInfos();
                sendHandlerMessage();
            }
        }).start();
    }

}
