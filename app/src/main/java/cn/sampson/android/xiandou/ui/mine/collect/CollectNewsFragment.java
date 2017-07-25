package cn.sampson.android.xiandou.ui.mine.collect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.core.manager.ImageUrlProcesser;
import cn.sampson.android.xiandou.core.presenter.MyCollectionPresenter;
import cn.sampson.android.xiandou.core.retroft.Api.UserApi;
import cn.sampson.android.xiandou.core.retroft.RetrofitWapper;
import cn.sampson.android.xiandou.core.retroft.base.BasePresenter;
import cn.sampson.android.xiandou.core.retroft.base.IView;
import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.model.ListItem;
import cn.sampson.android.xiandou.ui.BaseFragment;
import cn.sampson.android.xiandou.ui.haoyun.NewArticleDetailActivity;
import cn.sampson.android.xiandou.ui.haoyun.domain.NewsItem;
import cn.sampson.android.xiandou.utils.imageloader.ImageLoader;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.QuickRecycleViewAdapter;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.ViewHelper;

/**
 * Created by chengyang on 2017/7/23.
 */

public class CollectNewsFragment extends BaseFragment implements IView, SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.list)
    RecyclerView list;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.view_root)
    FrameLayout viewRoot;

    QuickRecycleViewAdapter<NewsItem> mAdapter;
    MyCollectionPresenter mPresenter;

    int page = 1;
    int num = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_refresh_list, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mPresenter = new MyCollectionPresenterImpl(this, viewRoot);

        refresh.setOnRefreshListener(this);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new QuickRecycleViewAdapter<NewsItem>(R.layout.item_news_article, new ArrayList<NewsItem>(), list) {
            @Override
            protected void onBindData(Context context, int position, final NewsItem item, int itemLayoutId, ViewHelper helper) {
                helper.setText(R.id.tv_title, item.articleTitle);
                helper.setText(R.id.tv_content, item.articleSummary);
                ImageLoader.load(context, ImageUrlProcesser.reSetImageUrlWH(item.cover, ImageUrlProcesser.POSTER_WIDTH, ImageUrlProcesser.POSTER_HEIGHT), (ImageView) helper.getView(R.id.iv_poster));
                helper.getRootView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), NewArticleDetailActivity.class);
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
                mPresenter.getCollectionList("2", page, num);
            }
        });
        list.setAdapter(mAdapter);

        onRefresh();
    }

    @Override
    public void setError(int errorCode, String error, String key) {
        setList(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {
        page = 1;
        mPresenter.getCollectionList("2", page, num);
    }

    void onArticleComplete(ListItem<NewsItem> list) {
        if (list != null && list.total > 0) {
            setList(list.lists);
        } else {
            setList(null);
        }
    }

    void setList(List<NewsItem> list) {
        refresh.setRefreshing(false);
        if (page == 1) {
            mAdapter.setRefresh(list, num);
        } else {
            mAdapter.setLoaded(list, num);
        }
    }

    class MyCollectionPresenterImpl extends BasePresenter<CollectNewsFragment> implements MyCollectionPresenter {

        public MyCollectionPresenterImpl(CollectNewsFragment view, ViewGroup rootView) {
            super(view, rootView);
        }

        @Override
        protected void onResult(Result result, String key) {
            switch (key) {
                case GET_COLLECTION_LIST:
                    onArticleComplete((ListItem<NewsItem>) result.data);
                    break;
            }
        }

        @Override
        public void getCollectionList(String type, int page, int num) {
            requestData(RetrofitWapper.getInstance().getNetService(UserApi.class).getNewsCollections(page, num), GET_COLLECTION_LIST);
        }
    }
}
