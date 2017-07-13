package cn.sampson.android.xiandou.ui.haoyun.beiyun;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.core.presenter.NewsPresenter;
import cn.sampson.android.xiandou.core.presenter.impl.NewPresenterImpl;
import cn.sampson.android.xiandou.model.ListItem;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.ui.haoyun.INewsView;
import cn.sampson.android.xiandou.ui.haoyun.domain.ArticleItem;
import cn.sampson.android.xiandou.utils.ContextUtil;
import cn.sampson.android.xiandou.utils.ScreenUtils;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.QuickRecycleViewAdapter;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.ViewHelper;

/**
 * 备孕主页
 */
public class BeiYunActivity extends BaseActivity implements INewsView, SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.tv_pregnancy_ratio)
    TextView tvPregnancyRatio;
    @Bind(R.id.tv_grade)
    TextView tvGrade;
    @Bind(R.id.zhangzishi)
    LinearLayout zhangzishi;
    @Bind(R.id.baidailasi)
    LinearLayout baidailasi;
    @Bind(R.id.bchaocepai)
    LinearLayout bchaocepai;
    @Bind(R.id.qiyuan)
    LinearLayout qiyuan;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.list)
    RecyclerView list;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;

    NewsPresenter mPresenter;
    QuickRecycleViewAdapter<ArticleItem> mAdapter;
    int page = 1;
    int pageNum = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setContentView(R.layout.activity_bei_yun);
        ButterKnife.bind(this);
        setSupportToolbar(toolbar, "");
        StatusBarUtil.transparencyBar(this);
        initSystemBar();
        initView();
    }

    private void initView() {
        toolbarLayout.setTitle(getString(R.string.beiyun));
        toolbarLayout.setExpandedTitleColor(ContextUtil.getColor(R.color.transparent));
        toolbarLayout.setCollapsedTitleTextColor(ContextUtil.getColor(R.color.gray7));

        refresh.setOnRefreshListener(this);
        list.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new QuickRecycleViewAdapter<ArticleItem>(R.layout.item_news_article, new ArrayList<ArticleItem>()) {
            @Override
            protected void onBindData(Context context, int position, ArticleItem item, int itemLayoutId, ViewHelper helper) {
                helper.setText(R.id.tv_title, item.articleTitle);
                helper.setText(R.id.tv_content, item.articleContent);
            }
        };
        mAdapter.setOnLoadMoreListener(new QuickRecycleViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                mPresenter.getNewsByTag("1", page, pageNum);
            }
        });
        list.setAdapter(mAdapter);

        mPresenter = new NewPresenterImpl(this);
        onRefresh();
    }

    /**
     * 沉浸式状态栏
     */
    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int top = ScreenUtils.getSystemBarHeight();
            ((CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams()).topMargin = top;
        }
    }

    @OnClick(R.id.zhangzishi)
    public void jumpToZhangzishi() {
        jumpTo(ZhangZiShiActivity.class);
    }

    @OnClick(R.id.baidailasi)
    public void jumpToBaidaiLasi(){
        jumpTo(BaidaiLaSiActivity.class);
    }

    @Override
    public void setError(int errorCode, String error, String key) {
        refresh.setRefreshing(false);
        setList(null);
    }

    @Override
    public void showNews(ListItem<ArticleItem> data) {
        refresh.setRefreshing(false);
        if (data != null && data.total > 0) {
            setList(data.lists);
        }
    }

    private void setList(List<ArticleItem> data) {
        if (page == 1) {
            mAdapter.setRefresh(data, pageNum);
        } else {
            mAdapter.setLoaded(data, pageNum);
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        mPresenter.getNewsByTag("1", page, pageNum);
    }
}
