package cn.sampson.android.xiandou.core.presenter;

/**
 * Created by Administrator on 2017/9/4 0004.
 */

public interface YuerBaojianListPresenter {

    String GET_YUER_BAOJIAN_LIST = "getYuerBaojianList";

    void getYuerBaojianList(String type ,int page ,int num);

}
