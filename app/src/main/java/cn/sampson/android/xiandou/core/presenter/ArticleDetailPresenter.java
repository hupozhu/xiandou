package cn.sampson.android.xiandou.core.presenter;

/**
 * Created by chengyang on 2017/7/18.
 */

public interface ArticleDetailPresenter {

    String GET_ARTICLE_DETAIL = "getArticleDetail";

    String GET_COMMENT_LIST = "getCommentList";

    String PUBLISH_COMMENT = "publishComment";

    String COLLECT = "collect";

    void getArticleDetail(long articleId);

    void getCommentList(long articleId, int page, int num);

    void communityComment(long articleid, String content, long re_user_id, long re_comment_id);

    void collectNews(long id);

}
