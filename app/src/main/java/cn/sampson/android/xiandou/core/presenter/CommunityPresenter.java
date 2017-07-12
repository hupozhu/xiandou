package cn.sampson.android.xiandou.core.presenter;

/**
 * Created by chengyang on 2017/7/12.
 */

public interface CommunityPresenter {

    String GET_ARTICLE = "getArticle";

    void getArticle(String tag, int page, int num);

}
