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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.core.presenter.CommunityIndexPresenter;
import cn.sampson.android.xiandou.core.retroft.Api.CommunityApi;
import cn.sampson.android.xiandou.core.retroft.RetrofitWapper;
import cn.sampson.android.xiandou.core.retroft.base.BasePresenter;
import cn.sampson.android.xiandou.core.retroft.base.IView;
import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.model.ListItem;
import cn.sampson.android.xiandou.ui.BaseFragment;
import cn.sampson.android.xiandou.ui.community.domain.ArticleItem;
import cn.sampson.android.xiandou.ui.community.domain.CommunityCategory;
import cn.sampson.android.xiandou.ui.community.domain.CommunityIndex;
import cn.sampson.android.xiandou.utils.UiUtils;
import cn.sampson.android.xiandou.utils.imageloader.ImageLoader;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.QuickRecycleViewAdapter;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.ViewHelper;
import cn.sampson.android.xiandou.widget.banner.BannerItem;
import cn.sampson.android.xiandou.widget.banner.MainPageImageBanner;
import cn.sampson.android.xiandou.widget.banner.base.BaseBanner;

/**
 * Created by chengyang on 2017/7/11.
 */

public class CommunityFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, IView {


    @Bind(R.id.list)
    RecyclerView list;
    @Bind(R.id.view_root)
    FrameLayout viewRoot;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;

    LinearLayout llCategoryContainer;
    LinearLayout llHotContainer;
    MainPageImageBanner mBanner;

    CommunityIndexPresenter mPresenter;
    QuickRecycleViewAdapter<ArticleItem> mAdapter;
    List<BannerItem> bannerItems;

    int page = 1;
    int num = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_refresh_list, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        refresh.setOnRefreshListener(this);
        mPresenter = new CommunityIndexPresenterImpl(this, viewRoot);

        list.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new QuickRecycleViewAdapter<ArticleItem>(R.layout.item_community_news, new ArrayList<ArticleItem>()) {
            @Override
            protected void onBindData(Context context, int position, final ArticleItem item, int itemLayoutId, ViewHelper helper) {
                ImageLoader.load(context, item.cover, (ImageView) helper.getView(R.id.iv_poster));
                helper.setText(R.id.tv_title, item.title);
                helper.setText(R.id.tv_content, item.content);
                helper.setRootOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), CommunityActivity.class);
                        intent.putExtra(CommunityActivity.ARTICLE_TAG, item.cateTag);
                        startActivity(intent);
                    }
                });
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
        onRefresh();
    }

    /**
     * 初始化列表头部
     */
    private View initHeader() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.header_community, list, false);
        llCategoryContainer = UiUtils.find(view, R.id.ll_category_container);
        llHotContainer = UiUtils.find(view, R.id.ll_hot_container);
        mBanner = UiUtils.find(view, R.id.banner);
        mBanner.setOnItemClickL(new BaseBanner.OnItemClickL() {
            @Override
            public void onItemClick(int position) {
                //TODO:
            }
        });
        return view;
    }

    /**
     * 展示banner
     */
    private void showBanner(ListItem<BannerItem> banners) {
        if (banners != null && banners.total > 0) {
            this.bannerItems = banners.lists;
            mBanner.setSource(banners.lists);
            mBanner.startScroll();
        }
    }

    /**
     * 展示类别
     */
    private void showCategory(ListItem<CommunityCategory> categories) {
        if (categories != null && categories.total > 0) {
            llCategoryContainer.removeAllViews();
            boolean newLine = false;
            LinearLayout row = null;
            for (int i = 0; i < categories.lists.size(); i++) {
                if (i % 3 == 0) {
                    row = new LinearLayout(getContext());
                    row.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    newLine = true;
                }

                View item = LayoutInflater.from(getContext()).inflate(R.layout.item_community_category, row, false);
                TextView cateName = UiUtils.find(item, R.id.tv_name);
                ImageView cateImg = UiUtils.find(item, R.id.iv_logo);
                cateName.setText(categories.lists.get(i).name);
                //TODO:类目图片
                row.addView(item);

                if (newLine) {
                    newLine = false;
                    llCategoryContainer.addView(row);
                }
            }
        }
    }

    /**
     * 展示热门动态
     */
//    private void showTopHot(ListItem<ArticleItem> hots) {
//        if (hots != null && hots.total > 0) {
//            llHotContainer.removeAllViews();
//            for (int i = 0; i < hots.lists.size(); i++) {
//                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_community_hot, llHotContainer, false);
//                llHotContainer.addView(view);
//            }
//        }
//    }

    /**
     * 展示资讯
     */
    private void showHot(ListItem<ArticleItem> news) {
        if (news != null && news.total > 0) {
            setList(news.lists);
        }
    }

    private void setList(List<ArticleItem> list) {
        if (page == 1) {
            mAdapter.setRefresh(list, num);
        } else {
            mAdapter.setLoaded(list, num);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mBanner.goOnScroll();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBanner.pauseScroll();
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
    }

    @Override
    public void setError(int errorCode, String error, String key) {
        switch (key) {
            case CommunityIndexPresenter.GET_COMMUNITY_INDEX:
                setList(null);
                break;
        }
    }

    void showCommunityIndex(CommunityIndex communityIndex) {
        refresh.setRefreshing(false);
        showBanner(communityIndex.banners);
        showCategory(communityIndex.cateList);
//        showHot(communityIndex.hots);
        showHot(communityIndex.hots);
    }

    class CommunityIndexPresenterImpl extends BasePresenter<CommunityFragment> implements CommunityIndexPresenter {

        public CommunityIndexPresenterImpl(CommunityFragment view, ViewGroup rootView) {
            super(view, rootView);
        }

        @Override
        public void getCommunityIndex(int page, int num) {
            requestData(RetrofitWapper.getInstance().getNetService(CommunityApi.class).getUserInfo(page, num), GET_COMMUNITY_INDEX);
        }

        @Override
        protected void onResult(Result result, String key) {
            switch (key) {
                case GET_COMMUNITY_INDEX:
                    showCommunityIndex((CommunityIndex) result.data);
                    break;
            }
        }
    }
}
