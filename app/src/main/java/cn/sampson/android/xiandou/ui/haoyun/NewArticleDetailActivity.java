package cn.sampson.android.xiandou.ui.haoyun;

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

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.core.presenter.ArticleDetailPresenter;
import cn.sampson.android.xiandou.core.retroft.Api.CommunityApi;
import cn.sampson.android.xiandou.core.retroft.Api.NewsApi;
import cn.sampson.android.xiandou.core.retroft.RetrofitWapper;
import cn.sampson.android.xiandou.core.retroft.base.BasePresenter;
import cn.sampson.android.xiandou.core.retroft.base.IView;
import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.ui.haoyun.domain.ArticleDetail;
import cn.sampson.android.xiandou.ui.community.domain.CommentItem;
import cn.sampson.android.xiandou.utils.UiUtils;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.QuickRecycleViewAdapter;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.ViewHelper;
import cn.sampson.android.xiandou.widget.keyboard.ArticleCommentKeyboard;
import cn.sampson.android.xiandou.widget.keyboard.FuncLayout;
import cn.sampson.android.xiandou.widget.webview.ImgObserveWebView;

/**
 * Created by chengyang on 2017/7/18.
 */

public class NewArticleDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, IView, FuncLayout.OnFuncKeyBoardListener {

    public static String ARTICLE_ID = "articleId";

    @Bind(R.id.list)
    RecyclerView list;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.keyboard)
    ArticleCommentKeyboard keyboard;

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
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        articleId = getIntent().getLongExtra(ARTICLE_ID, 0);
        mPresenter = new ArticleDetailPresenterImpl(this);
        mPresenter.getArticleDetail(articleId);
        mPresenter.getCommentList(articleId, page, num);

        keyboard.addOnFuncKeyBoardListener(this);

        refresh.setOnRefreshListener(this);
        list.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new QuickRecycleViewAdapter<CommentItem>(R.layout.item_comment, new ArrayList<CommentItem>()) {
            @Override
            protected void onBindData(Context context, int position, CommentItem item, int itemLayoutId, ViewHelper helper) {

            }
        };
        mAdapter.addHeaderView(initArticleHeader());
        list.setAdapter(mAdapter);
    }

    private View initArticleHeader() {
        View view = LayoutInflater.from(this).inflate(R.layout.header_article_detail1, list, false);
        tvNickname = UiUtils.find(view, R.id.tv_nickname);
        tvTime = UiUtils.find(view, R.id.tv_time);
        tvTitle = UiUtils.find(view, R.id.tv_title);
        webView = UiUtils.find(view, R.id.webView);
        tvCommentNum = UiUtils.find(view, R.id.tv_comment_num);
        llRoot = UiUtils.find(view, R.id.ll_root);
        return view;
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void setError(int errorCode, String error, String key) {

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
        tvNickname.setText(detail.articleAuthor);
        tvTitle.setText(detail.articleTitle);
        tvTime.setText(detail.created);
        webView.loadDataWithBaseURL(null, detail.articleContent, "text/html", "utf-8", null);
    }


    class ArticleDetailPresenterImpl extends BasePresenter<NewArticleDetailActivity> implements ArticleDetailPresenter {

        public ArticleDetailPresenterImpl(NewArticleDetailActivity view) {
            super(view);
        }

        @Override
        public void getArticleDetail(long detailId) {
            requestData(RetrofitWapper.getInstance().getNetService(NewsApi.class).getNewDetail(String.valueOf(detailId)), GET_ARTICLE_DETAIL);
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
