package cn.sampson.android.xiandou.core.presenter;

/**
 * Created by chengyang on 2017/7/12.
 */

public interface CommunityIndexPresenter {

    String GET_COMMUNITY_INDEX = "getCommunityIndex";

    void getCommunityIndex(int page, int num);

}
