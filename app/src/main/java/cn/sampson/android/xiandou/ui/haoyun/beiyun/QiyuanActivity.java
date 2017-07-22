package cn.sampson.android.xiandou.ui.haoyun.beiyun;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.core.retroft.base.IView;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.ui.haoyun.IndexFragmentWrapper;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;
import cn.sampson.android.xiandou.widget.adapter.TabStateFragmentAdapter;

/**
 * Created by chengyang on 2017/7/14.
 */

public class QiyuanActivity extends BaseActivity implements IView, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.et_input)
    EditText etInput;
    @Bind(R.id.tv_qiyuan)
    TextView tvQiyuan;
    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.refresh_root)
    FrameLayout refreshRoot;

    TabStateFragmentAdapter mPagerFragmentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setContentView(R.layout.activity_qiyuan);
        ButterKnife.bind(this);
        setActionBarBack();

        initView();
    }

    private void initView() {
        refresh.setOnRefreshListener(this);
        refresh.setProgressViewOffset(true, -50, 50);
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    refresh.setEnabled(true);
                } else {
                    refresh.setEnabled(false);
                }
            }
        });

        mPagerFragmentAdapter = new TabStateFragmentAdapter(getSupportFragmentManager(), new IndexFragmentWrapper());
        pager.setAdapter(mPagerFragmentAdapter);
        tabs.setupWithViewPager(pager);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void setError(int errorCode, String error, String key) {

    }
}
