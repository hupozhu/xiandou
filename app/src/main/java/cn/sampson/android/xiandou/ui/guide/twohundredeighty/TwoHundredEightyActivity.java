package cn.sampson.android.xiandou.ui.guide.twohundredeighty;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.ui.guide.fortyweeks.FortyWeeksActivity;
import cn.sampson.android.xiandou.ui.guide.twohundredeighty.db.ReadEveryDayDBHelper;
import cn.sampson.android.xiandou.utils.widget.adapter.TabStateFragmentAdapter;

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
        setContentView(R.layout.activity_two_hundred_eighty);
        ButterKnife.bind(this);

        getDataFromDB();
    }

    private void initView() {
        if (loadingView != null) {
            flRoot.removeView(loadingView);
            loadingView = null;
        }

        mPagerAdapter = new TabStateFragmentAdapter(getSupportFragmentManager(), new TwoHundredEightyFragmentWapper());
        pager.setAdapter(mPagerAdapter);
        tabs.setupWithViewPager(pager);
    }

    private void getDataFromDB() {
        loadingView = LayoutInflater.from(TwoHundredEightyActivity.this).inflate(R.layout._loading_view, null);
        flRoot.addView(loadingView);

        new Thread(new Runnable() {
            @Override
            public void run() {
                ReadEveryDayDBHelper helper = new ReadEveryDayDBHelper(TwoHundredEightyActivity.this);
                infos = helper.getInfos();
            }
        }).start();

    }

    private MyHandler myHandler = new MyHandler(TwoHundredEightyActivity.this);
    View loadingView;

    private static class MyHandler extends Handler {
        private final WeakReference<TwoHundredEightyActivity> mActivity;

        public MyHandler(TwoHundredEightyActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            TwoHundredEightyActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 1:
                        activity.initView();
                        break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myHandler.removeMessages(1);
        myHandler.removeMessages(2);
    }
}
