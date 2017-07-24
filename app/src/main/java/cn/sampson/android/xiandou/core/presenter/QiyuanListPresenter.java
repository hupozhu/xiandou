package cn.sampson.android.xiandou.core.presenter;

/**
 * Created by chengyang on 2017/7/23.
 */

public interface QiyuanListPresenter {

    String GET_QIYUAN_LIST = "getQiyuanList";

    void getQiyaunList(String type, int page, int num);

}
