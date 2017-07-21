package cn.sampson.android.xiandou.ui.haoyun;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import cn.sampson.android.xiandou.model.ListItem;
import cn.sampson.android.xiandou.ui.BaseFragment;
import cn.sampson.android.xiandou.ui.haoyun.domain.ArticleItem;
import cn.sampson.android.xiandou.utils.imageloader.ImageLoader;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.QuickRecycleViewAdapter;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.ViewHelper;

/**
 * Created by chengyang on 2017/7/13.
 */

public class ArticleListFragment extends BaseFragment {

    public static final String TYPE = "type";

    public static final int TYPE_HOT = 1;
    public static final int TYPE_NEW = 2;

    int type;

    HaoYunFragment mParentFragment;

    @Bind(R.id.list)
    RecyclerView list;

    QuickRecycleViewAdapter<ArticleItem> mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_list, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        type = getArguments().getInt(TYPE);
        mParentFragment = (HaoYunFragment) getParentFragment();

        list.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new QuickRecycleViewAdapter<ArticleItem>(R.layout.item_news_article, new ArrayList<ArticleItem>()) {
            @Override
            protected void onBindData(Context context, int position, final ArticleItem item, int itemLayoutId, ViewHelper helper) {
                helper.setText(R.id.tv_title, item.articleTitle);
                helper.setText(R.id.tv_content, item.articleSummary);
                ImageLoader.load(context, item.cover, (ImageView) helper.getView(R.id.iv_poster));
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
        list.setAdapter(mAdapter);
    }

    public void showArticle() {
        ListItem<ArticleItem> data = mParentFragment.getListData(type);
        if (data != null && data.total > 0) {
            mAdapter.getAdapterManager().replaceAllItems(data.lists);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
