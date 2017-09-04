package cn.sampson.android.xiandou.ui.haoyun.yuerbaojian;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.core.presenter.YuerBaojianListPresenter;
import cn.sampson.android.xiandou.core.retroft.Api.NewsApi;
import cn.sampson.android.xiandou.core.retroft.RetrofitWapper;
import cn.sampson.android.xiandou.core.retroft.base.BasePresenter;
import cn.sampson.android.xiandou.core.retroft.base.IView;
import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.model.ListItem;
import cn.sampson.android.xiandou.ui.haoyun.domain.NewsItem;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.QuickRecycleViewAdapter;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.ViewHelper;

/**
 * Created by Administrator on 2017/9/4 0004.
 */

public class YuerbaojianFragment extends Fragment implements IView {

    public static final String TYPE = "type";

    @Bind(R.id.list)
    RecyclerView list;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.view_root)
    RelativeLayout viewRoot;

    QuickRecycleViewAdapter<NewsItem> adapter;
    YuerBaojianListPresenter mPresenter;

    String type;
    int page =1,num=10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_refresh_list, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        type = getArguments().getString(TYPE);
        mPresenter = new YuerBaojianPresenterImpl(this,viewRoot);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getYuerBaojianList(type,page,num);
            }
        });

        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new QuickRecycleViewAdapter<NewsItem>(R.layout.item_baojian,new ArrayList<NewsItem>(),list) {
            @Override
            protected void onBindData(Context context, int position, NewsItem item, int itemLayoutId, ViewHelper helper) {
                helper.setText(R.id.tv_item , (position+1) + "、" + " " + item.articleTitle);
            }
        };
        adapter.setOnLoadMoreListener(new QuickRecycleViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                mPresenter.getYuerBaojianList(type,page,num);
            }
        });
        list.setAdapter(adapter);

        mPresenter.getYuerBaojianList(type,page,num);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void setError(int errorCode, String error, String key) {
        refresh.setRefreshing(false);
    }

    void showBaojian(ListItem<NewsItem> data){
        refresh.setRefreshing(false);
        if (data != null && data.total > 0) {
            setList(data.lists);
        }
    }

    private void setList(List<NewsItem> data) {
        if (page == 1) {
            adapter.setRefresh(data, num);
        } else {
            adapter.setLoaded(data, num);
        }
    }

    class YuerBaojianPresenterImpl extends BasePresenter<YuerbaojianFragment> implements YuerBaojianListPresenter{

        public YuerBaojianPresenterImpl(YuerbaojianFragment view) {
            super(view);
        }

        public YuerBaojianPresenterImpl(YuerbaojianFragment view, ViewGroup rootView) {
            super(view, rootView);
        }

        @Override
        public void getYuerBaojianList(String tag,int page,int num) {
            requestData(RetrofitWapper.getInstance().getNetService(NewsApi.class).getNewsByTag(tag, page, num), GET_YUER_BAOJIAN_LIST);
        }

        @Override
        protected void onResult(Result result, String key) {
            switch (key){
                case GET_YUER_BAOJIAN_LIST:
                    showBaojian((ListItem<NewsItem>) result.data);
                    break;
            }
        }
    }
}
