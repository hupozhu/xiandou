package cn.sampson.android.xiandou.ui.community;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.BaseFragment;
import cn.sampson.android.xiandou.ui.community.domain.CommunityCategory;
import cn.sampson.android.xiandou.ui.community.domain.CommunityNews;
import cn.sampson.android.xiandou.utils.UiUtils;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.QuickRecycleViewAdapter;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.ViewHelper;

/**
 * Created by chengyang on 2017/7/11.
 */

public class CommunityFragment extends BaseFragment {

    ImageView ivBanner;
    RecyclerView categoryContainer;

    @Bind(R.id.list)
    RecyclerView list;

    QuickRecycleViewAdapter<CommunityCategory> mCategoryAdapter;
    QuickRecycleViewAdapter<CommunityNews> mNewsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        mNewsAdapter = new QuickRecycleViewAdapter<CommunityNews>(R.layout.item_community_news, new ArrayList<CommunityNews>(), list) {
            @Override
            protected void onBindData(Context context, int position, CommunityNews item, int itemLayoutId, ViewHelper helper) {

            }
        };
        mNewsAdapter.setOnLoadMoreListener(new QuickRecycleViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mNewsAdapter.setLoaded(getTestNews(), 5);
            }
        });
        mNewsAdapter.addHeaderView(initHeader());
        list.setAdapter(mNewsAdapter);


        mCategoryAdapter.setRefresh(getTestCategory(), 5);
        mNewsAdapter.setRefresh(getTestNews(), 5);
    }

    private View initHeader() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.header_community, null);
        categoryContainer = UiUtils.find(view, R.id.category_container);
        categoryContainer.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mCategoryAdapter = new QuickRecycleViewAdapter<CommunityCategory>(R.layout.item_community_category, new ArrayList<CommunityCategory>()) {
            @Override
            protected void onBindData(Context context, int position, CommunityCategory item, int itemLayoutId, ViewHelper helper) {
//                helper.setText(R.id.tv_name, item.name);
            }
        };
        categoryContainer.setAdapter(mCategoryAdapter);
        return view;
    }

    private ArrayList<CommunityCategory> getTestCategory() {
        ArrayList<CommunityCategory> categories = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            categories.add(new CommunityCategory());
        }
        return categories;
    }

    private ArrayList<CommunityNews> getTestNews() {
        ArrayList<CommunityNews> news = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            news.add(new CommunityNews());
        }
        return news;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
