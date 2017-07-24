package cn.sampson.android.xiandou.core.presenter;

/**
 * Created by chengyang on 2017/7/20.
 */

public interface MessagePresenter {

    String GET_MESSAGE_LIST = "getMessageList";

    void getMessageList(String type, int page, int num);

}
