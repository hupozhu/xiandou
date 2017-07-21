package cn.sampson.android.xiandou.ui.haoyun;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.core.presenter.HomePresenter;
import cn.sampson.android.xiandou.core.retroft.Api.NewsApi;
import cn.sampson.android.xiandou.core.retroft.RetrofitWapper;
import cn.sampson.android.xiandou.core.retroft.base.BasePresenter;
import cn.sampson.android.xiandou.core.retroft.base.IView;
import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.model.ListItem;
import cn.sampson.android.xiandou.ui.BaseFragment;
import cn.sampson.android.xiandou.ui.haoyun.beiyun.BeiYunActivity;
import cn.sampson.android.xiandou.ui.haoyun.domain.ArticleItem;
import cn.sampson.android.xiandou.ui.haoyun.domain.Index;
import cn.sampson.android.xiandou.ui.haoyun.yuer.YuerActivity;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.YunyuActivity;
import cn.sampson.android.xiandou.utils.imageloader.ImageLoader;
import cn.sampson.android.xiandou.utils.imageloader.transformation.RoundedTransformation;
import cn.sampson.android.xiandou.widget.adapter.TabStateFragmentAdapter;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.QuickRecycleViewAdapter;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.ViewHelper;
import cn.sampson.android.xiandou.widget.banner.BannerItem;
import cn.sampson.android.xiandou.widget.banner.MainPageImageBanner;

/**
 * Created by Administrator on 2017/6/5.
 */

public class HaoYunFragment extends BaseFragment implements View.OnClickListener, IView {

    @Bind(R.id.beiyun)
    LinearLayout mBeiyun;
    @Bind(R.id.yunyu)
    LinearLayout mYunyu;
    @Bind(R.id.yuer)
    LinearLayout mYuer;
    @Bind(R.id.refresh_root)
    FrameLayout refreshRoot;
    @Bind(R.id.banner)
    MainPageImageBanner banner;
    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.adv_list)
    RecyclerView advList;
    @Bind(R.id.adv_split)
    View advSplit;

    QuickRecycleViewAdapter<BannerItem> mAdpter;

    List<BannerItem> bannerItems;
    private static HaoYunFragment haoyunFragment;


    public static HaoYunFragment getInstance() {
        if (haoyunFragment == null) {
            haoyunFragment = new HaoYunFragment();
        }
        return haoyunFragment;
    }

    HomePresenter mPresenter;
    TabStateFragmentAdapter mPagerFragmentAdapter;

    private ListItem<ArticleItem> hotList;
    private ListItem<ArticleItem> newsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mBeiyun.setOnClickListener(this);
        mYunyu.setOnClickListener(this);
        mYuer.setOnClickListener(this);

        mPagerFragmentAdapter = new TabStateFragmentAdapter(getChildFragmentManager(), new IndexFragmentWrapper());
        pager.setAdapter(mPagerFragmentAdapter);
        tabs.setupWithViewPager(pager);

        advList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mAdpter = new QuickRecycleViewAdapter<BannerItem>(R.layout.item_advs, new ArrayList<BannerItem>()) {
            @Override
            protected void onBindData(Context context, int position, BannerItem item, int itemLayoutId, ViewHelper helper) {
                ImageLoader.getPicasso(context).load(item.imgAddr).transform(new RoundedTransformation(10, 0)).fit().into((ImageView) helper.getView(R.id.iv_advs));
            }
        };
        advList.setAdapter(mAdpter);

        mPresenter = new HomePresenterImpl(this, refreshRoot);
        mPresenter.getHome();
    }

    @Override
    public void onResume() {
        super.onResume();
        banner.goOnScroll();
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.pauseScroll();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.beiyun:
                Intent beiyun = new Intent(getActivity(), BeiYunActivity.class);
                getActivity().startActivity(beiyun);
                break;

            case R.id.yunyu:
                Intent yunyu = new Intent(getActivity(), YunyuActivity.class);
                getActivity().startActivity(yunyu);
                break;

            case R.id.yuer:
                Intent attention = new Intent(getActivity(), YuerActivity.class);
                getActivity().startActivity(attention);
                break;
        }
    }

    @Override
    public void setError(int errorCode, String error, String key) {

    }

    void showHomePage(Index index) {
        //显示banner
        if (index.banners != null && index.banners.total > 0) {
            this.bannerItems = index.banners.lists;
            banner.setSource(index.banners.lists);
            banner.startScroll();
        }

        //显示广告
        if (index.advs != null && index.advs.total > 0) {
            advList.setVisibility(View.VISIBLE);
            advSplit.setVisibility(View.VISIBLE);
            mAdpter.getAdapterManager().replaceAllItems(index.advs.lists);
        } else {
            advList.setVisibility(View.GONE);
            advSplit.setVisibility(View.GONE);
        }

        //显示资讯
        hotList = index.hots;
        newsList = index.news;

        for (int i = 0; i < mPagerFragmentAdapter.getCount(); i++) {
            ((ArticleListFragment) mPagerFragmentAdapter.getItem(i)).showArticle();
        }

    }

    //获取资讯列表
    public ListItem<ArticleItem> getListData(int type) {
        if (type == ArticleListFragment.TYPE_HOT) {
            return hotList;
        } else {
            return newsList;
        }
    }

    class HomePresenterImpl extends BasePresenter<HaoYunFragment> implements HomePresenter {


        public HomePresenterImpl(HaoYunFragment view, ViewGroup rootView) {
            super(view, rootView);
        }

        @Override
        public void getHome() {
            requestData(RetrofitWapper.getInstance().getNetService(NewsApi.class).getIndex(), GET_HOME);
        }

        @Override
        protected void onResult(Result result, String key) {
            switch (key) {
                case GET_HOME:
                    showHomePage((Index) result.data);
                    break;
            }
        }
    }
}
