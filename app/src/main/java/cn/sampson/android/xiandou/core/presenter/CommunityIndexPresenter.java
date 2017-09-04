package cn.sampson.android.xiandou.core.presenter;

/**
 * Created by chengyang on 2017/7/12.
 */

public interface CommunityIndexPresenter {

    String GET_COMMUNITY_INDEX = "getCommunityIndex";

    String GET_CATEGORY = "getCategory";

    void getCommunityIndex(int page, int num);

    void setGetCategory();

}
