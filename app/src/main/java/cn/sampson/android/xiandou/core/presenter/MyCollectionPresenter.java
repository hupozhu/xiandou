package cn.sampson.android.xiandou.core.presenter;

/**
 * Created by chengyang on 2017/7/20.
 */

public interface MyCollectionPresenter {

    String GET_COLLECTION_LIST = "getCollectionList";

    void getCollectionList(String type, int page, int num);

}
