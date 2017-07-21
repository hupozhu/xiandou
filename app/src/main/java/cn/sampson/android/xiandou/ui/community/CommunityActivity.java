package cn.sampson.android.xiandou.ui.community;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.core.presenter.CommunityPresenter;
import cn.sampson.android.xiandou.core.retroft.Api.CommunityApi;
import cn.sampson.android.xiandou.core.retroft.RetrofitWapper;
import cn.sampson.android.xiandou.core.retroft.base.BasePresenter;
import cn.sampson.android.xiandou.core.retroft.base.IView;
import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.model.ListItem;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.ui.community.domain.ArticleItem;
import cn.sampson.android.xiandou.utils.imageloader.ImageLoader;
import cn.sampson.android.xiandou.utils.plist.domain.Array;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.QuickRecycleViewAdapter;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.ViewHelper;

/**
 * Created by chengyang on 2017/7/12.
 */

public class CommunityActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, IView {

    public static final String ARTICLE_TAG = "article_tag";
    public static final String COMMUNITY_NAME = "community_name";

    @Bind(R.id.list)
    RecyclerView list;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.view_root)
    FrameLayout viewRoot;

    CommunityPresenter mPresenter;
    QuickRecycleViewAdapter<ArticleItem> mAdapter;

    int page = 1;
    int num = 10;
    String tag;
    String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setContentView(R.layout.common_refresh_list);
        setActionBarBack();
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tag = getIntent().getStringExtra(ARTICLE_TAG);
        name = getIntent().getStringExtra(COMMUNITY_NAME);
        getSupportActionBar().setTitle(name);

        refresh.setOnRefreshListener(this);
        mPresenter = new CommunityPresenterImpl(this, viewRoot);

        list.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new QuickRecycleViewAdapter<ArticleItem>(R.layout.item_community, new ArrayList<ArticleItem>(), list) {
            @Override
            protected void onBindData(Context context, int position, final ArticleItem item, int itemLayoutId, ViewHelper helper) {
//                ImageLoader.loadAvatar(context, item.userinfo.userPic, (ImageView) helper.getView(R.id.riv_avatar));
//                helper.setText(R.id.tv_name, item.userinfo.nickname);
                helper.setText(R.id.article_title, item.title);
                helper.setText(R.id.tv_content, item.content);
                helper.setText(R.id.tv_time, item.addTime);
                helper.setText(R.id.tv_comment, String.valueOf(item.commentNum));
                showImages((RelativeLayout) helper.getView(R.id.image_container));
                helper.getRootView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putLong(CommunityArticleDetailActivity.ARTICLE_ID, item.articleId);
                        jumpTo(CommunityArticleDetailActivity.class, bundle);
                    }
                });
            }
        };
        mAdapter.setOnLoadMoreListener(new QuickRecycleViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                mPresenter.getArticle(tag, page, num);
            }
        });
        list.setAdapter(mAdapter);
        onRefresh();
    }

    private void showImages(RelativeLayout container) {

    }

    @Override
    public void onRefresh() {
        page = 1;
        mPresenter.getArticle(tag, page, num);
    }

    @Override
    public void setError(int errorCode, String error, String key) {
        switch (key) {
            case CommunityPresenter.GET_ARTICLE:
                setList(null);
                break;
        }
    }

    private void setList(List<ArticleItem> list) {
        refresh.setRefreshing(false);
        if (page == 1) {
            mAdapter.setRefresh(list, num);
        } else {
            mAdapter.setLoaded(list, num);
        }
    }

    void onArticleComplete(ListItem<ArticleItem> datas) {
        setList(datas.lists);
    }

    class CommunityPresenterImpl extends BasePresenter<CommunityActivity> implements CommunityPresenter {

        public CommunityPresenterImpl(CommunityActivity view, ViewGroup rootView) {
            super(view, rootView);
        }

        @Override
        public void getArticle(String tag, int page, int num) {
            requestData(RetrofitWapper.getInstance().getNetService(CommunityApi.class).getArticleList(tag, page, num), GET_ARTICLE);
        }

        @Override
        protected void onResult(Result result, String key) {
            switch (key) {
                case GET_ARTICLE:
                    onArticleComplete((ListItem<ArticleItem>) result.data);
                    break;
            }
        }
    }
}
