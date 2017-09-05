package cn.sampson.android.xiandou.ui.haoyun;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import cn.sampson.android.xiandou.core.presenter.HomePresenter;
import cn.sampson.android.xiandou.core.retroft.Api.CommunityApi;
import cn.sampson.android.xiandou.core.retroft.Api.NewsApi;
import cn.sampson.android.xiandou.core.retroft.RetrofitWapper;
import cn.sampson.android.xiandou.core.retroft.base.BasePresenter;
import cn.sampson.android.xiandou.core.retroft.base.IView;
import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.model.ListItem;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.ui.BaseFragment;
import cn.sampson.android.xiandou.ui.WebViewActivity;
import cn.sampson.android.xiandou.ui.community.PublishArticleActivity;
import cn.sampson.android.xiandou.ui.community.domain.CommunityCategory;
import cn.sampson.android.xiandou.ui.haoyun.beiyun.BeiYunActivity;
import cn.sampson.android.xiandou.ui.haoyun.domain.Index;
import cn.sampson.android.xiandou.ui.haoyun.domain.NewsItem;
import cn.sampson.android.xiandou.ui.haoyun.yuer.YuerActivity;
import cn.sampson.android.xiandou.ui.haoyun.yuerbaojian.YuerBaojianActivity;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.YunyuActivity;
import cn.sampson.android.xiandou.utils.ContextUtil;
import cn.sampson.android.xiandou.utils.imageloader.ImageLoader;
import cn.sampson.android.xiandou.utils.imageloader.transformation.RoundedTransformation;
import cn.sampson.android.xiandou.widget.adapter.TabStateFragmentAdapter;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.QuickRecycleViewAdapter;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.ViewHelper;
import cn.sampson.android.xiandou.widget.banner.BannerItem;
import cn.sampson.android.xiandou.widget.banner.MainPageImageBanner;
import cn.sampson.android.xiandou.widget.banner.base.BaseBanner;

/**
 * Created by Administrator on 2017/6/5.
 */

public class HaoYunFragment extends BaseFragment implements View.OnClickListener, IView, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.iv_question)
    ImageView ivQuestion;
    @Bind(R.id.tv_question)
    TextView tvQuestion;
    @Bind(R.id.rl_question)
    RelativeLayout rlQuestion;
    @Bind(R.id.iv_knowledge)
    ImageView ivKnowledge;
    @Bind(R.id.tv_knowledge)
    TextView tvKnowledge;
    @Bind(R.id.rl_health)
    RelativeLayout rlHealth;
    @Bind(R.id.banner)
    MainPageImageBanner banner;
    @Bind(R.id.adv_list)
    RecyclerView advList;
    @Bind(R.id.ll_shiguan)
    LinearLayout llShiguan;
    @Bind(R.id.ll_beiyun)
    LinearLayout llBeiyun;
    @Bind(R.id.ll_taijiao)
    LinearLayout llTaijiao;
    @Bind(R.id.ll_yuer)
    LinearLayout llYuer;
    @Bind(R.id.refresh_root)
    FrameLayout refreshRoot;

    QuickRecycleViewAdapter<BannerItem> mAdpter;

    private static HaoYunFragment haoyunFragment;

    public static HaoYunFragment getInstance() {
        if (haoyunFragment == null) {
            haoyunFragment = new HaoYunFragment();
        }
        return haoyunFragment;
    }

    HomePresenter mPresenter;

    private List<BannerItem> bannerItems;

    int advHeight, advWidth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        llBeiyun.setOnClickListener(this);
        llTaijiao.setOnClickListener(this);
        llYuer.setOnClickListener(this);
        rlHealth.setOnClickListener(this);
        rlQuestion.setOnClickListener(this);

        mPresenter = new HomePresenterImpl(this, refreshRoot);
        advWidth = (ContextUtil.getScreenWidth() - ContextUtil.dip2Px(12) * 4) / 2;
        advHeight = (int) (advWidth / 320 * 130 * 1.5);

        banner.setOnItemClickL(new BaseBanner.OnItemClickL() {
            @Override
            public void onItemClick(int position) {
                if (TextUtils.isEmpty(bannerItems.get(position).actUrl))
                    return;

                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra(WebViewActivity.URL, bannerItems.get(position).actUrl);
                startActivity(intent);
            }
        });

        advList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mAdpter = new QuickRecycleViewAdapter<BannerItem>(R.layout.item_advs, new ArrayList<BannerItem>()) {
            @Override
            protected void onBindData(Context context, final int position, final BannerItem item, int itemLayoutId, ViewHelper helper) {
                helper.getRootView().getLayoutParams().height = advHeight;
                helper.getRootView().getLayoutParams().width = advWidth;

                ImageLoader.getPicasso(context).load(item.imgAddr).transform(new RoundedTransformation(10, 0)).fit().into((ImageView) helper.getView(R.id.iv_advs));
                helper.getRootView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra(WebViewActivity.URL, item.actUrl);
                        startActivity(intent);
                    }
                });
            }
        };
        advList.setAdapter(mAdpter);

        onRefresh();
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
            case R.id.ll_beiyun:
                Intent beiyun = new Intent(getActivity(), BeiYunActivity.class);
                getActivity().startActivity(beiyun);
                break;

            case R.id.ll_taijiao:
                Intent yunyu = new Intent(getActivity(), YunyuActivity.class);
                getActivity().startActivity(yunyu);
                break;

            case R.id.ll_yuer:
                Intent attention = new Intent(getActivity(), YuerActivity.class);
                getActivity().startActivity(attention);
                break;

            case R.id.rl_health:
                if (LogicManager.loginIntercept((BaseActivity) getActivity())) {
                    return;
                }

                Intent baojian = new Intent(getActivity(), YuerBaojianActivity.class);
                getActivity().startActivity(baojian);
                break;

            case R.id.rl_question:
                Intent question = new Intent(getActivity(), PublishArticleActivity.class);
                getActivity().startActivity(question);
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
            mAdpter.getAdapterManager().replaceAllItems(index.advs.lists);
        } else {
            advList.setVisibility(View.GONE);
        }
    }

    /**
     * 处理类别
     */
    private void processCategory(ListItem<CommunityCategory> categories) {
        if (categories != null && categories.total > 0) {
            AppCache.setCommunityCategories(categories.lists);
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.getHome();
        mPresenter.getCategory();
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
        public void getCategory() {
            requestData(RetrofitWapper.getInstance().getNetService(CommunityApi.class).getCategories(), GET_CATEGORY);
        }

        @Override
        protected void onResult(Result result, String key) {
            switch (key) {
                case GET_HOME:
                    showHomePage((Index) result.data);
                    break;

                case GET_CATEGORY:
                    processCategory((ListItem<CommunityCategory>) result.data);
                    break;
            }
        }
    }
}
