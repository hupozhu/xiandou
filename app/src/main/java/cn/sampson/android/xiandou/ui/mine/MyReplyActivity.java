package cn.sampson.android.xiandou.ui.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.core.presenter.MyReplyCommentPresenter;
import cn.sampson.android.xiandou.core.retroft.Api.UserApi;
import cn.sampson.android.xiandou.core.retroft.RetrofitWapper;
import cn.sampson.android.xiandou.core.retroft.base.BasePresenter;
import cn.sampson.android.xiandou.core.retroft.base.IView;
import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.model.ListItem;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.ui.community.domain.CommentItem;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.QuickRecycleViewAdapter;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.ViewHelper;

/**
 * Created by chengyang on 2017/7/18.
 */

public class MyReplyActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, IView {

    @Bind(R.id.list)
    RecyclerView list;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.view_root)
    FrameLayout viewRoot;

    MyReplyCommentPresenter mPresenter;
    QuickRecycleViewAdapter<CommentItem> mAdapter;

    int page = 1;
    int num = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_refresh_list);
        StatusBarUtil.StatusBarLightMode(this);
        ButterKnife.bind(this);
        setActionBarBack();

        initView();
    }

    private void initView() {
        refresh.setOnRefreshListener(this);
        mPresenter = new MyReplyCommentPresenterImpl(this, viewRoot);

        list.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new QuickRecycleViewAdapter<CommentItem>(R.layout.item_comment, new ArrayList<CommentItem>(), list) {
            @Override
            protected void onBindData(Context context, int position, CommentItem item, int itemLayoutId, ViewHelper helper) {
                helper.setText(R.id.tv_content, item.content);
            }
        };
        mAdapter.setOnLoadMoreListener(new QuickRecycleViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
            }
        });
        list.setAdapter(mAdapter);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        mPresenter.getMyReply();
    }

    @Override
    public void setError(int errorCode, String error, String key) {
        switch (key) {
            case MyReplyCommentPresenter.GET_MY_REPLY:
                setList(null);
                break;
        }
    }

    void showReply(ListItem<CommentItem> lists) {
        if (lists != null && lists.total > 0) {
            setList(lists.lists);
        } else {
            setList(null);
        }
    }

    private void setList(List<CommentItem> lists) {
        refresh.setRefreshing(false);
        if (page == 1) {
            mAdapter.setRefresh(lists, page);
        } else {
            mAdapter.setLoaded(lists, page);
        }
    }

    class MyReplyCommentPresenterImpl extends BasePresenter<MyReplyActivity> implements MyReplyCommentPresenter {

        public MyReplyCommentPresenterImpl(MyReplyActivity view, ViewGroup rootView) {
            super(view, rootView);
        }

        @Override
        public void getMyReply() {
            requestData(RetrofitWapper.getInstance().getNetService(UserApi.class).getComments(), GET_MY_REPLY);
        }

        @Override
        protected void onResult(Result result, String key) {
            switch (key) {
                case GET_MY_REPLY:
                    showReply((ListItem<CommentItem>) result.data);
                    break;
            }
        }
    }
}
