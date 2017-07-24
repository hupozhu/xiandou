package cn.sampson.android.xiandou.ui.haoyun.beiyun;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.core.manager.ImageUrlProcesser;
import cn.sampson.android.xiandou.core.presenter.NewsPresenter;
import cn.sampson.android.xiandou.core.presenter.PregnantRatePresenter;
import cn.sampson.android.xiandou.core.presenter.impl.NewPresenterImpl;
import cn.sampson.android.xiandou.core.retroft.Api.NewsApi;
import cn.sampson.android.xiandou.core.retroft.RetrofitWapper;
import cn.sampson.android.xiandou.core.retroft.base.BasePresenter;
import cn.sampson.android.xiandou.core.retroft.base.IView;
import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.model.CommonField;
import cn.sampson.android.xiandou.model.ListItem;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.ui.haoyun.INewsView;
import cn.sampson.android.xiandou.ui.haoyun.NewArticleDetailActivity;
import cn.sampson.android.xiandou.ui.haoyun.beiyun.qiyuan.QiyuanActivity;
import cn.sampson.android.xiandou.ui.haoyun.domain.NewsItem;
import cn.sampson.android.xiandou.utils.ContextUtil;
import cn.sampson.android.xiandou.utils.ScreenUtils;
import cn.sampson.android.xiandou.utils.imageloader.ImageLoader;
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
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.list)
    RecyclerView list;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;

    NewsPresenter mPresenter;
    PregnantRatePresenter mRatePresenter;
    QuickRecycleViewAdapter<NewsItem> mAdapter;
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
        mAdapter = new QuickRecycleViewAdapter<NewsItem>(R.layout.item_news_article, new ArrayList<NewsItem>()) {
            @Override
            protected void onBindData(Context context, int position, final NewsItem item, int itemLayoutId, ViewHelper helper) {
                helper.setText(R.id.tv_title, item.articleTitle);
                helper.setText(R.id.tv_content, item.articleSummary);
                ImageLoader.load(context, ImageUrlProcesser.reSetImageUrlWH(item.cover, ImageUrlProcesser.POSTER_WIDTH, ImageUrlProcesser.POSTER_HEIGHT), (ImageView) helper.getView(R.id.iv_poster));
                helper.getRootView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(BeiYunActivity.this, NewArticleDetailActivity.class);
                        intent.putExtra(NewArticleDetailActivity.ARTICLE_ID, item.articleId);
                        startActivity(intent);
                    }
                });
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
        mRatePresenter = new PregnantRatePresenterImpl(this);
        mRatePresenter.getPregnateRate();
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
    public void jumpToBaidaiLasi() {
        jumpTo(BaidaiLaSiActivity.class);
    }

    @OnClick(R.id.bchaocepai)
    public void jumpToBichaoPaice() {
        jumpTo(BichaoPaiceActivity.class);
    }

    @OnClick(R.id.qiyuan)
    public void jumpToQiyuan() {
        jumpTo(QiyuanActivity.class);
    }

    @Override
    public void setError(int errorCode, String error, String key) {
        refresh.setRefreshing(false);
        setList(null);
    }

    @Override
    public void showNews(ListItem<NewsItem> data) {
        refresh.setRefreshing(false);
        if (data != null && data.total > 0) {
            setList(data.lists);
        }
    }

    private void setList(List<NewsItem> data) {
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

    void onRateComplete(CommonField field) {
        tvPregnancyRatio.setText(field.rate + "%");
    }

    class PregnantRatePresenterImpl extends BasePresenter<BeiYunActivity> implements PregnantRatePresenter {

        public PregnantRatePresenterImpl(BeiYunActivity view) {
            super(view);
        }

        @Override
        public void getPregnateRate() {
            requestData(RetrofitWapper.getInstance().getNetService(NewsApi.class).getPregnantRate(), GET_PREGNATE_RATE);
        }

        @Override
        protected void onResult(Result result, String key) {
            switch (key) {
                case GET_PREGNATE_RATE:
                    onRateComplete((CommonField) result.data);
                    break;
            }
        }
    }

}
