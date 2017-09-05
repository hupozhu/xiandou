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
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.core.presenter.MyCollectionPresenter;
import cn.sampson.android.xiandou.core.retroft.Api.UserApi;
import cn.sampson.android.xiandou.core.retroft.RetrofitWapper;
import cn.sampson.android.xiandou.core.retroft.base.BasePresenter;
import cn.sampson.android.xiandou.core.retroft.base.IView;
import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.model.ListItem;
import cn.sampson.android.xiandou.ui.BaseFragment;
import cn.sampson.android.xiandou.ui.community.CommunityActivity;
import cn.sampson.android.xiandou.ui.community.CommunityArticleDetailActivity;
import cn.sampson.android.xiandou.ui.community.domain.PostsItem;
import cn.sampson.android.xiandou.utils.ContextUtil;
import cn.sampson.android.xiandou.utils.imageloader.ImageLoader;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.QuickRecycleViewAdapter;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.ViewHelper;

/**
 * Created by chengyang on 2017/7/23.
 */

public class CollectPostFragment extends BaseFragment implements IView, SwipeRefreshLayout.OnRefreshListener {


    @Bind(R.id.list)
    RecyclerView list;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.view_root)
    RelativeLayout viewRoot;

    QuickRecycleViewAdapter<PostsItem> mAdapter;
    MyCollectionPresenter mPresenter;

    int page = 1;
    int num = 2;

    int imageWidth;
    int margin;

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

        imageWidth = (ContextUtil.getScreenWidth() - ContextUtil.dip2Px(100)) / 3;
        margin = ContextUtil.dip2Px(12);

        refresh.setOnRefreshListener(this);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new QuickRecycleViewAdapter<PostsItem>(R.layout.item_community, new ArrayList<PostsItem>(), list) {
            @Override
            protected void onBindData(Context context, int position, final PostsItem item, int itemLayoutId, ViewHelper helper) {
                ImageLoader.loadAvatar(context, item.userinfo.userPic, (ImageView) helper.getView(R.id.riv_avatar));
                helper.setText(R.id.tv_name, item.userinfo.nickname);
                helper.setText(R.id.article_title, item.title);
                helper.setText(R.id.tv_content, item.content);
                helper.setText(R.id.tv_time, item.addTime);
                helper.setText(R.id.tv_comment, String.valueOf(item.commentNum));

                showImages(item.images, (RelativeLayout) helper.getView(R.id.image_container));
                helper.getRootView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), CommunityArticleDetailActivity.class);
                        intent.putExtra(CommunityArticleDetailActivity.ARTICLE_ID, item.articleId);
                        startActivity(intent);
                    }
                });
            }
        };
        mAdapter.setOnLoadMoreListener(new QuickRecycleViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                mPresenter.getCollectionList("1", page, num);
            }
        });
        list.setAdapter(mAdapter);

        onRefresh();
    }

    private void showImages(List<String> images, RelativeLayout container) {
        container.removeAllViews();
        if (images == null)
            return;

        int length = images.size() > 3 ? 3 : images.size();
        for (int i = 0; i < length; i++) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(imageWidth, imageWidth);
            ImageView img = new ImageView(getContext());
            params.leftMargin = (imageWidth + margin) * i;
            img.setLayoutParams(params);
            ImageLoader.load(getContext(), images.get(i), img);
            container.addView(img);
        }
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
        mPresenter.getCollectionList("1", page, num);
    }

    void onArticleComplete(ListItem<PostsItem> list) {
        if (list != null && list.total > 0) {
            setList(list.lists);
        } else {
            setList(null);
        }
    }

    void setList(List<PostsItem> list) {
        refresh.setRefreshing(false);
        if (page == 1) {
            mAdapter.setRefresh(list, num);
        } else {
            mAdapter.setLoaded(list, num);
        }
    }

    class MyCollectionPresenterImpl extends BasePresenter<CollectPostFragment> implements MyCollectionPresenter {

        public MyCollectionPresenterImpl(CollectPostFragment view, ViewGroup rootView) {
            super(view, rootView);
        }

        @Override
        protected void onResult(Result result, String key) {
            switch (key) {
                case GET_COLLECTION_LIST:
                    onArticleComplete((ListItem<PostsItem>) result.data);
                    break;
            }
        }

        @Override
        public void getCollectionList(String type, int page, int num) {
            requestData(RetrofitWapper.getInstance().getNetService(UserApi.class).getPostsCollections(page, num), GET_COLLECTION_LIST);
        }
    }
}
