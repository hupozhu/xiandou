package cn.sampson.android.xiandou.ui.haoyun.yunyu;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.core.manager.ImageUrlProcesser;
import cn.sampson.android.xiandou.core.presenter.NewsPresenter;
import cn.sampson.android.xiandou.core.presenter.impl.NewPresenterImpl;
import cn.sampson.android.xiandou.model.ListItem;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.ui.haoyun.INewsView;
import cn.sampson.android.xiandou.ui.haoyun.NewArticleDetailActivity;
import cn.sampson.android.xiandou.ui.haoyun.beiyun.BeiYunActivity;
import cn.sampson.android.xiandou.ui.haoyun.domain.ArticleItem;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.attention.PregnancyAttentionActivity;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.fortyweeks.FortyWeeksActivity;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.taijiaoyinyue.TaijiaoYinyueActivity;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.taijiaozhishi.TaijiaoZhishiActivity;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.twohundredeighty.TwoHundredEightyActivity;
import cn.sampson.android.xiandou.utils.ContextUtil;
import cn.sampson.android.xiandou.utils.ScreenUtils;
import cn.sampson.android.xiandou.utils.imageloader.ImageLoader;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.QuickRecycleViewAdapter;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.ViewHelper;

/**
 * 孕育主页
 * Created by chengyang on 2017/6/21.
 */

public class YunyuActivity extends BaseActivity implements INewsView, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @Bind(R.id.list)
    RecyclerView list;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;

    NewsPresenter mPresenter;
    QuickRecycleViewAdapter<ArticleItem> mAdapter;
    int page = 1;
    int pageNum = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.StatusBarLightMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yun_yu);
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
            protected void onBindData(Context context, int position, final ArticleItem item, int itemLayoutId, ViewHelper helper) {
                helper.setText(R.id.tv_title, item.articleTitle);
                helper.setText(R.id.tv_content, item.articleSummary);
                ImageLoader.load(context,  ImageUrlProcesser.reSetImageUrlWH(item.cover, ImageUrlProcesser.POSTER_WIDTH, ImageUrlProcesser.POSTER_HEIGHT), (ImageView) helper.getView(R.id.iv_poster));
                helper.getRootView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(YunyuActivity.this, NewArticleDetailActivity.class);
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
                mPresenter.getNewsByTag("2", page, pageNum);
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
        mPresenter.getNewsByTag("2", page, pageNum);
    }


    @OnClick(R.id.huaiyun40zhou)
    public void jumpToHuaiyun40zhou() {
        jumpTo(FortyWeeksActivity.class);
    }

    @OnClick(R.id.m280tian)
    public void jumpTo280Tian() {
        jumpTo(TwoHundredEightyActivity.class);
    }

    @OnClick(R.id.yunqizhuyi)
    public void jumpToYunqizhuyi() {
        jumpTo(PregnancyAttentionActivity.class);
    }

    @OnClick(R.id.taijiaoyinyue)
    public void jumpToTaijiaoYinyue() {
        jumpTo(TaijiaoYinyueActivity.class);
    }

    @OnClick(R.id.taijiaozhishi)
    public void jumpToTaijiaozhishi() {
        jumpTo(TaijiaoZhishiActivity.class);
    }

}
