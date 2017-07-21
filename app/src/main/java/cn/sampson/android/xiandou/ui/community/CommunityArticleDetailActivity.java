package cn.sampson.android.xiandou.ui.community;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.core.presenter.ArticleDetailPresenter;
import cn.sampson.android.xiandou.core.retroft.Api.CommunityApi;
import cn.sampson.android.xiandou.core.retroft.RetrofitWapper;
import cn.sampson.android.xiandou.core.retroft.base.BasePresenter;
import cn.sampson.android.xiandou.core.retroft.base.IView;
import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.model.ListItem;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.ui.community.domain.ArticleDetail;
import cn.sampson.android.xiandou.ui.community.domain.CommentItem;
import cn.sampson.android.xiandou.utils.UiUtils;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.QuickRecycleViewAdapter;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.ViewHelper;
import cn.sampson.android.xiandou.widget.keyboard.ArticleCommentKeyboard;
import cn.sampson.android.xiandou.widget.keyboard.FuncLayout;
import cn.sampson.android.xiandou.widget.webview.ImgObserveWebView;

/**
 * Created by chengyang on 2017/7/18.
 */

public class CommunityArticleDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, IView, FuncLayout.OnFuncKeyBoardListener {

    public static String ARTICLE_ID = "articleId";

    @Bind(R.id.list)
    RecyclerView list;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.keyboard)
    ArticleCommentKeyboard keyboard;

    RoundedImageView rivAvatar;
    TextView tvTag;
    TextView tvNickname;
    TextView tvTime;
    TextView tvTitle;
    ImgObserveWebView webView;
    TextView tvCommentNum;
    LinearLayout llRoot;

    long articleId;
    ArticleDetailPresenter mPresenter;
    QuickRecycleViewAdapter<CommentItem> mAdapter;

    int page = 1;
    int num = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);
        setActionBarBack();
        initView();
    }

    private void initView() {
        articleId = getIntent().getLongExtra(ARTICLE_ID, 0);
        mPresenter = new ArticleDetailPresenterImpl(this);

        keyboard.addOnFuncKeyBoardListener(this);

        refresh.setOnRefreshListener(this);
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
                page++;
                mPresenter.getCommentList(articleId, page, num);
            }
        });
        mAdapter.addHeaderView(initArticleHeader());
        list.setAdapter(mAdapter);
        onRefresh();
    }

    private View initArticleHeader() {
        View view = LayoutInflater.from(this).inflate(R.layout.header_article_detail, list, false);
        tvNickname = UiUtils.find(view, R.id.tv_nickname);
        tvTime = UiUtils.find(view, R.id.tv_time);
        tvTitle = UiUtils.find(view, R.id.tv_title);
        webView = UiUtils.find(view, R.id.webView);
        tvCommentNum = UiUtils.find(view, R.id.tv_comment_num);
        llRoot = UiUtils.find(view, R.id.ll_root);
        rivAvatar = UiUtils.find(view, R.id.riv_avatar);
        tvTag = UiUtils.find(view, R.id.tv_tag);
        return view;
    }


    @Override
    public void onRefresh() {
        mPresenter.getArticleDetail(articleId);
        mPresenter.getCommentList(articleId, page, num);
    }

    @Override
    public void setError(int errorCode, String error, String key) {
        switch (key) {
            case ArticleDetailPresenter.GET_COMMENT_LIST:
                setList(null);
                break;
        }
    }

    @Override
    public void OnFuncPop(int height) {
        //none
    }

    @Override
    public void OnFuncClose() {
        //none
    }

    public void showDetailInfo(ArticleDetail detail) {
//        tvNickname.setText(detail.articleAuthor);
        tvTitle.setText(detail.title);
        tvTime.setText(detail.created);
        tvTag.setText(getString(R.string.category_tag, detail.cateTag));
        webView.loadDataWithBaseURL(null, detail.content, "text/html", "utf-8", null);
    }

    public void showCommentList(ListItem<CommentItem> item) {
        if (item != null && item.total > 0) {
            setList(item.lists);
            tvCommentNum.setText(getString(R.string.comment_num, String.valueOf(item.total)));
        } else {
            setList(null);
        }
    }

    public void setList(List<CommentItem> items) {
        refresh.setRefreshing(false);
        if (page == 1) {
            mAdapter.setRefresh(items, page);
        } else {
            mAdapter.setLoaded(items, page);
        }
    }


    class ArticleDetailPresenterImpl extends BasePresenter<CommunityArticleDetailActivity> implements ArticleDetailPresenter {

        public ArticleDetailPresenterImpl(CommunityArticleDetailActivity view) {
            super(view);
        }

        @Override
        public void getArticleDetail(long detailId) {
            requestData(RetrofitWapper.getInstance().getNetService(CommunityApi.class).getArticleDetail(String.valueOf(detailId)), GET_ARTICLE_DETAIL);
        }

        @Override
        public void getCommentList(long articleId, int page, int num) {
            requestData(RetrofitWapper.getInstance().getNetService(CommunityApi.class).getCommentList(String.valueOf(articleId), page, num), GET_COMMENT_LIST);
        }

        @Override
        protected void onResult(Result result, String key) {
            switch (key) {
                case GET_ARTICLE_DETAIL:
                    showDetailInfo((ArticleDetail) result.data);
                    break;

                case GET_COMMENT_LIST:
                    showCommentList((ListItem<CommentItem>) result.data);
                    break;

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        llRoot.removeView(webView);
        webView.stopLoading();
        webView.getSettings().setJavaScriptEnabled(false);
        webView.clearHistory();
        webView.removeAllViews();
        webView.destroy();
    }
}
