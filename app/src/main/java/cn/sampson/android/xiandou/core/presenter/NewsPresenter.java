package cn.sampson.android.xiandou.core.presenter;

/**
 * 资讯
 */

public interface NewsPresenter {

    String GET_NEWS_BY_TAG = "getNewsByTag";

    void getNewsByTag(String tag, int page, int limit);

}
