package cn.sampson.android.xiandou.core.presenter;

/**
 * Created by chengyang on 2017/8/28.
 */

public interface CommunityPublishPresenter {

    String PUBLISH_ARTICLE = "publishArticle";

    void publishArticle(String title, String content, String tag);

}
