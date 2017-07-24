package cn.sampson.android.xiandou.ui.haoyun.beiyun.qiyuan;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.core.presenter.QiyuanPresenter;
import cn.sampson.android.xiandou.core.retroft.Api.UserApi;
import cn.sampson.android.xiandou.core.retroft.RetrofitWapper;
import cn.sampson.android.xiandou.core.retroft.base.BasePresenter;
import cn.sampson.android.xiandou.core.retroft.base.IView;
import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.utils.ToastUtils;
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
    @Bind(R.id.tv_length_num)
    TextView tvLengthNum;
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
    QiyuanPresenter mPresenter;

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
        mPresenter = new QiyuanPresenterImpl(this);

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

        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.length();
                tvLengthNum.setText(length + "/140");

            }
        });
        tvQiyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etInput.getText())) {
                    ToastUtils.show("请先许下您的心愿");
                    return;
                }

                mPresenter.qiyuan(etInput.getText().toString());
            }
        });

        mPagerFragmentAdapter = new TabStateFragmentAdapter(getSupportFragmentManager(), new QiyuanFragmentWrapper());
        pager.setAdapter(mPagerFragmentAdapter);
        tabs.setupWithViewPager(pager);
        tabs.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tabs, 60, 60);

                onRefresh();
            }
        });

    }

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    @Override
    public void onRefresh() {
        for (int i = 0; i < mPagerFragmentAdapter.getCount(); i++) {
            ((QiyuanFragment) mPagerFragmentAdapter.getItem(i)).onRefresh();
        }
    }

    @Override
    public void setError(int errorCode, String error, String key) {

    }

    void onQiyuanComplete() {
        etInput.setText("");
        onRefresh();
        pickUpKeyBoard();
    }

    void onQiyuanListComplete() {
        refresh.setRefreshing(false);
    }

    class QiyuanPresenterImpl extends BasePresenter<QiyuanActivity> implements QiyuanPresenter {

        public QiyuanPresenterImpl(QiyuanActivity view) {
            super(view);
        }

        @Override
        public void qiyuan(String content) {
            requestData(RetrofitWapper.getInstance().getNetService(UserApi.class).qiyuan(content), QI_YUAN);
        }

        @Override
        protected void onResult(Result result, String key) {
            switch (key) {
                case QI_YUAN:
                    onQiyuanComplete();
                    break;
            }
        }
    }
}
