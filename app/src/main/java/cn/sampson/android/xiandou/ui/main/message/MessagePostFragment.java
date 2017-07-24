package cn.sampson.android.xiandou.ui.main.message;

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
import cn.sampson.android.xiandou.core.presenter.MessagePresenter;
import cn.sampson.android.xiandou.core.retroft.Api.UserApi;
import cn.sampson.android.xiandou.core.retroft.RetrofitWapper;
import cn.sampson.android.xiandou.core.retroft.base.BasePresenter;
import cn.sampson.android.xiandou.core.retroft.base.IView;
import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.model.ListItem;
import cn.sampson.android.xiandou.ui.BaseFragment;
import cn.sampson.android.xiandou.ui.community.domain.CommentItem;
import cn.sampson.android.xiandou.ui.haoyun.NewArticleDetailActivity;
import cn.sampson.android.xiandou.utils.ContextUtil;
import cn.sampson.android.xiandou.utils.imageloader.ImageLoader;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.QuickRecycleViewAdapter;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.ViewHelper;
import cn.sampson.android.xiandou.widget.babushkatext.BabushkaText;

/**
 * Created by chengyang on 2017/7/23.
 */

public class MessagePostFragment extends BaseFragment implements IView, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.list)
    RecyclerView list;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.view_root)
    FrameLayout viewRoot;

    QuickRecycleViewAdapter<CommentItem> mAdapter;
    MessagePresenter mPresenter;

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
        mPresenter = new MessagePresenterImpl(this, viewRoot);

        refresh.setOnRefreshListener(this);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new QuickRecycleViewAdapter<CommentItem>(R.layout.item_comment, new ArrayList<CommentItem>(), list) {
            @Override
            protected void onBindData(Context context, int position, final CommentItem item, int itemLayoutId, ViewHelper helper) {
                ImageLoader.loadAvatar(context, item.userinfo.userPic, (ImageView) helper.getView(R.id.riv_avatar));
                helper.setText(R.id.tv_nickname, item.userinfo.nickname);
                helper.setText(R.id.tv_time, item.created);
                helper.setText(R.id.tv_content, item.content);

                if (item.fathercomment != null) {
                    helper.setVisibility(R.id.ll_container_replay, true);
                    final BabushkaText text = helper.getView(R.id.btv_father_comment);
                    text.reset();
                    text.addPiece(new BabushkaText.Piece.Builder(item.fathercomment.userinfo.nickname).textColor(ContextUtil.getColor(R.color.blue)).build());
                    text.addPiece(new BabushkaText.Piece.Builder(" : " + item.fathercomment.content).textColor(ContextUtil.getColor(R.color.gray7)).build());
                    text.display();
                } else {
                    helper.setVisibility(R.id.ll_container_replay, false);
                }
                helper.setVisibility(R.id.fl_reply, false);

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
                mPresenter.getMessageList("1", page, num);
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
        mPresenter.getMessageList("1", page, num);
    }

    void onReplyComplete(ListItem<CommentItem> list) {
        if (list != null && list.total > 0) {
            setList(list.lists);
        } else {
            setList(null);
        }
    }

    void setList(List<CommentItem> list) {
        refresh.setRefreshing(false);
        if (page == 1) {
            mAdapter.setRefresh(list, num);
        } else {
            mAdapter.setLoaded(list, num);
        }
    }

    class MessagePresenterImpl extends BasePresenter<MessagePostFragment> implements MessagePresenter {

        public MessagePresenterImpl(MessagePostFragment view, ViewGroup rootView) {
            super(view, rootView);
        }

        @Override
        protected void onResult(Result result, String key) {
            switch (key) {
                case GET_MESSAGE_LIST:
                    onReplyComplete((ListItem<CommentItem>) result.data);
                    break;
            }
        }


        @Override
        public void getMessageList(String path, int page, int num) {
            requestData(RetrofitWapper.getInstance().getNetService(UserApi.class).getRecomments(path, page, num), GET_MESSAGE_LIST);
        }
    }
}
