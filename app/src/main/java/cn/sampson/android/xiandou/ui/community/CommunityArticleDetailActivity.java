package cn.sampson.android.xiandou.ui.community;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.core.manager.TipManager;
import cn.sampson.android.xiandou.core.presenter.ArticleDetailPresenter;
import cn.sampson.android.xiandou.core.retroft.Api.CommunityApi;
import cn.sampson.android.xiandou.core.retroft.Api.NewsApi;
import cn.sampson.android.xiandou.core.retroft.Api.UserApi;
import cn.sampson.android.xiandou.core.retroft.RetrofitWapper;
import cn.sampson.android.xiandou.core.retroft.base.BasePresenter;
import cn.sampson.android.xiandou.core.retroft.base.IView;
import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.model.ListItem;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.ui.community.domain.ArticleDetail;
import cn.sampson.android.xiandou.ui.community.domain.CommentItem;
import cn.sampson.android.xiandou.utils.ContextUtil;
import cn.sampson.android.xiandou.utils.ToastUtils;
import cn.sampson.android.xiandou.utils.UiUtils;
import cn.sampson.android.xiandou.utils.imageloader.ImageLoader;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.QuickRecycleViewAdapter;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.ViewHelper;
import cn.sampson.android.xiandou.widget.babushkatext.BabushkaText;
import cn.sampson.android.xiandou.widget.keyboard.FuncLayout;
import cn.sampson.android.xiandou.widget.webview.ImgObserveWebView;

/**
 * Created by chengyang on 2017/7/18.
 */

public class CommunityArticleDetailActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, IView {

    public static String ARTICLE_ID = "articleId";

    @Bind(R.id.list)
    RecyclerView list;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.et_input)
    EditText etInput;
    @Bind(R.id.tv_comment)
    TextView tvComment;
    @Bind(R.id.keyboard_root)
    LinearLayout keyboardRoot;
    @Bind(R.id.root_refresh)
    RelativeLayout rootRefresh;

    private Menu menu;

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

    long reUserId;
    long reCommentId;

    int page = 1;
    int num = 10;

    //键盘弹出时list布局重新Layout的监听
    private boolean needListenKeyboardEvent;
    //回复点击位置
    private int commentClickedSizeInWindow;
    private boolean isCollect;

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

        tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        etInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (!etInput.isFocused()) {
                    etInput.setFocusable(true);
                    etInput.setFocusableInTouchMode(true);
                }
                return false;
            }
        });

        refresh.setOnRefreshListener(this);
        list.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new QuickRecycleViewAdapter<CommentItem>(R.layout.item_comment, new ArrayList<CommentItem>(), list) {
            @Override
            protected void onBindData(Context context, int position, final CommentItem item, int itemLayoutId, ViewHelper helper) {
                ImageLoader.loadAvatar(context, item.userinfo.userPic, (ImageView) helper.getView(R.id.riv_avatar));
                helper.setText(R.id.tv_nickname, item.userinfo.nickname);
                helper.setText(R.id.tv_time, item.created);
                helper.setText(R.id.tv_content, item.content);
                final View root = helper.getRootView();
                helper.setOnClickListener(R.id.fl_reply, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //键盘布局显示之后，可以监听键盘动作了
                        needListenKeyboardEvent = true;

                        int location[] = new int[2];
                        root.getLocationInWindow(location);
                        commentClickedSizeInWindow = location[1] + root.getHeight();

                        if (!etInput.isFocused()) {
                            etInput.setFocusable(true);
                            etInput.setFocusableInTouchMode(true);
                        }
                        etInput.requestFocus();
                        showKeyBoard(etInput);

                        reCommentId = item.commentId;
                        reUserId = item.userId;
                    }
                });

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

        //对话框弹起不遮盖要回复的view
        rootRefresh.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!needListenKeyboardEvent) {
                    reCommentId = reUserId = 0;
                    return;
                }

                Rect rect = new Rect();
                //获取root在窗体的可视区域
                rootRefresh.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = rootRefresh.getRootView().getHeight() - rect.bottom;

                if (rootInvisibleHeight > 100) {//点击item，且键盘弹起
                    needListenKeyboardEvent = false;
                    //获取scrollToView在窗体的坐标

                    if (commentClickedSizeInWindow > 0) {
                        //计算root滚动高度，使scrollToView在可见区域
                        int scrollHeight = commentClickedSizeInWindow + keyboardRoot.getHeight() - rect.bottom;
                        if (scrollHeight > 0) {
                            list.smoothScrollBy(0, scrollHeight);
                        }
                    }
                }
            }
        });

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

    /**
     * 输入栏弹出状态下，点击输入框外的部分，收起键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                if (isTouchedInView(tvComment, ev)) {//如果点击到发布按钮，在隐藏键盘的同时还需要想要发表按钮监听
                    publishComment();
                }

                //如果输入框处于显示状态，且当前点击不在输入框内
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                etInput.clearFocus();
                etInput.setText("");
                return false;
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            return !isTouchedInView(v, event);
        }
        return false;
    }

    public boolean isTouchedInView(View v, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int[] leftTop = {0, 0};
        //获取输入框当前的location位置
        v.getLocationInWindow(leftTop);
        int left = leftTop[0];
        int top = leftTop[1];
        int bottom = top + v.getHeight();
        int right = left + v.getWidth();
        if (x > left && x < right && y > top && y < bottom) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onRefresh() {
        page = 1;
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

    public void publishComment() {
        String input = etInput.getText().toString();
        if (TextUtils.isEmpty(input)) {
            ToastUtils.show(R.string.input_not_null);
            return;
        }

        mPresenter.communityComment(articleId, input, reUserId, reCommentId);
    }

    /**
     * 显示资讯详情
     */
    public void showDetailInfo(ArticleDetail detail) {
        tvNickname.setText(detail.userinfo.nickname);
        ImageLoader.loadAvatar(CommunityArticleDetailActivity.this, detail.userinfo.userPic, rivAvatar);
        tvTitle.setText(detail.title);
        tvTime.setText(detail.created);
        tvTag.setText(getString(R.string.category_tag, detail.cateTag));
        webView.loadDataWithBaseURL(null, getHtmlContent(detail.content), "text/html", "utf-8", null);
        showCollection(detail.isCollect == 1);
    }

    public static String getHtmlContent(String html) {
        Document doc_Dis = Jsoup.parse(html);
        Elements ele_Img = doc_Dis.getElementsByTag("img");
        if (ele_Img.size() != 0) {
            for (Element e_Img : ele_Img) {
                e_Img.attr("style", "width:100%; height:auto;");
            }
        }
        return doc_Dis.toString();
    }

    private void showCollection(boolean isCollect) {
        this.isCollect = isCollect;
        if (!isCollect) {
            menu.getItem(0).setIcon(R.mipmap.ic_collect_nor);
        } else {
            menu.getItem(0).setIcon(R.mipmap.ic_collect_selected);
        }
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

    /**
     * 评论完成
     */
    void onCommentComplete(String msg) {
        reCommentId = reUserId = 0;
        etInput.setText("");
        page = 1;
        mPresenter.getCommentList(articleId, page, num);

        TipManager.showTip(msg);
    }

    /**
     * 收藏/取消收藏 完成
     */
    void onCollectComplete(String msg) {
        showCollection(!this.isCollect);
        TipManager.showTip(msg);
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
        public void communityComment(long articleid, String content, long re_user_id, long re_comment_id) {
            Map<String, Object> map = new HashMap<>();
            if (re_comment_id > 0 && re_user_id > 0) {
                map.put(CommunityApi.RE_COMMENT_ID, re_comment_id);
                map.put(CommunityApi.RE_USER_ID, re_user_id);
            }
            requestData(RetrofitWapper.getInstance().getNetService(CommunityApi.class).commentCommunity(articleid, content, map), PUBLISH_COMMENT);
        }

        @Override
        public void collectNews(long id) {
            requestData(RetrofitWapper.getInstance().getNetService(UserApi.class).collect(1, id), COLLECT);
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

                case PUBLISH_COMMENT:
                    onCommentComplete((String) result.data);
                    break;

                case COLLECT:
                    onCollectComplete((String) result.data);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_collect, menu);
        return true;
    }

    @Override
    protected void onToolbarCollect() {
        mPresenter.collectNews(articleId);
    }
}
