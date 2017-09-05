package cn.sampson.android.xiandou.core.presenter;

/**
 * Created by chengyang on 2017/7/13.
 */

public interface HomePresenter {

    String GET_HOME = "getHome";

    String GET_CATEGORY = "getCategory";

    void getHome();

    void getCategory();

}
