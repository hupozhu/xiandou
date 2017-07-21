package cn.sampson.android.xiandou.core.presenter;

/**
 * Created by chengyang on 2017/7/18.
 */

public interface ArticleDetailPresenter {

    String GET_ARTICLE_DETAIL = "getArticleDetail";

    String GET_COMMENT_LIST = "getCommentList";

    void getArticleDetail(long articleId);

    void getCommentList(long articleId, int page, int num);

}
