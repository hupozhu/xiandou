package cn.sampson.android.xiandou.ui.community;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.config.AppCache;
import cn.sampson.android.xiandou.core.manager.LogicManager;
import cn.sampson.android.xiandou.core.presenter.CommunityIndexPresenter;
import cn.sampson.android.xiandou.core.retroft.Api.CommunityApi;
import cn.sampson.android.xiandou.core.retroft.RetrofitWapper;
import cn.sampson.android.xiandou.core.retroft.base.BasePresenter;
import cn.sampson.android.xiandou.core.retroft.base.IView;
import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.model.ListItem;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.ui.BaseFragment;
import cn.sampson.android.xiandou.ui.community.domain.CommunityCategory;
import cn.sampson.android.xiandou.ui.community.domain.Discovery;
import cn.sampson.android.xiandou.ui.community.domain.PostsItem;
import cn.sampson.android.xiandou.utils.UiUtils;
import cn.sampson.android.xiandou.utils.imageloader.ImageLoader;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.QuickRecycleViewAdapter;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.ViewHelper;

/**
 * Created by chengyang on 2017/7/11.
 */

public class CommunityFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, IView {


    @Bind(R.id.list)
    RecyclerView list;
    @Bind(R.id.view_root)
    RelativeLayout viewRoot;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.iv_publish)
    ImageView ivPublish;

    CommunityIndexPresenter mPresenter;
    QuickRecycleViewAdapter<PostsItem> mAdapter;

    LinearLayout mTopContainer;

    int page = 1;
    int num = 10;

    private static CommunityFragment communityFragment;

    public static CommunityFragment getInstance() {
        if (communityFragment == null) {
            communityFragment = new CommunityFragment();
        }
        return communityFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        refresh.setOnRefreshListener(this);
        mPresenter = new CommunityIndexPresenterImpl(this, viewRoot);

        list.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new QuickRecycleViewAdapter<PostsItem>(R.layout.item_community_news, new ArrayList<PostsItem>(), list) {
            @Override
            protected void onBindData(Context context, int position, final PostsItem item, int itemLayoutId, ViewHelper helper) {
                helper.setText(R.id.tv_title, item.title);
                helper.setText(R.id.tv_tag, "#" + item.cateName + "#");
                helper.setText(R.id.tv_content, item.content);
                ImageLoader.loadAvatar(context, item.userinfo.userPic, (ImageView) helper.getView(R.id.riv_avatar));
                helper.setText(R.id.tv_username, item.userinfo.nickname);
                helper.setText(R.id.tv_comment_num, String.valueOf(item.commentNum));
                RelativeLayout layout = helper.getView(R.id.image_container);
                if (item.images.size() > 0) {
                    layout.setVisibility(View.VISIBLE);
                    showImages(item.images, layout);
                } else {
                    layout.setVisibility(View.GONE);
                }
            }
        };
        mAdapter.setOnLoadMoreListener(new QuickRecycleViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                mPresenter.getCommunityIndex(page, num);
            }
        });
        mAdapter.addHeaderView(initHeader());
        list.setAdapter(mAdapter);

        ivPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!LogicManager.loginIntercept((BaseActivity) getActivity())) {
                    Intent intent = new Intent(getActivity(), PublishArticleActivity.class);
                    startActivity(intent);
                }
            }
        });
        onRefresh();
    }

    int imageWidth;
    int margin;

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

    /**
     * 初始化列表头部
     */
    private View initHeader() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.header_community, list, false);
        mTopContainer = (LinearLayout) view;
        return view;
    }

    /**
     * 处理类别
     */
    private void processCategory(ListItem<CommunityCategory> categories) {
        if (categories != null && categories.total > 0) {
            AppCache.setCommunityCategories(categories.lists);
        }
    }

    /**
     * 展示资讯
     */
    private void showHot(ListItem<PostsItem> top) {
        mTopContainer.removeAllViews();

        if (top != null && top.total > 0) {
            for (PostsItem item : top.lists) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_discovery_top, mTopContainer, false);
                TextView title = UiUtils.find(view, R.id.tv_title);
                title.setText(item.title);
                mTopContainer.addView(view);
            }
        }
    }

    /**
     * 展示社区帖子
     */
    private void showPoster(ListItem<PostsItem> news) {
        if (news != null && news.total > 0) {
            setList(news.lists);
        }
    }

    private void setList(List<PostsItem> list) {
        if (page == 1) {
            mAdapter.setRefresh(list, num);
        } else {
            mAdapter.setLoaded(list, num);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {
        page = 1;
        mPresenter.getCommunityIndex(page, num);
        mPresenter.setGetCategory();
    }

    @Override
    public void setError(int errorCode, String error, String key) {
        switch (key) {
            case CommunityIndexPresenter.GET_COMMUNITY_INDEX:
                setList(null);
                break;
        }
    }

    void showCommunityIndex(Discovery discovery) {
        refresh.setRefreshing(false);
        showHot(discovery.top);
        showPoster(discovery.news);
    }

    class CommunityIndexPresenterImpl extends BasePresenter<CommunityFragment> implements CommunityIndexPresenter {

        public CommunityIndexPresenterImpl(CommunityFragment view, ViewGroup rootView) {
            super(view, rootView);
        }

        @Override
        public void getCommunityIndex(int page, int num) {
            requestData(RetrofitWapper.getInstance().getNetService(CommunityApi.class).getDiscovery(page, num), GET_COMMUNITY_INDEX);
        }

        @Override
        public void setGetCategory() {
            requestData(RetrofitWapper.getInstance().getNetService(CommunityApi.class).getCategories(), GET_CATEGORY);
        }

        @Override
        protected void onResult(Result result, String key) {
            switch (key) {
                case GET_COMMUNITY_INDEX:
                    showCommunityIndex((Discovery) result.data);
                    break;

                case GET_CATEGORY:
                    processCategory((ListItem<CommunityCategory>) result.data);
                    break;
            }
        }
    }
}
