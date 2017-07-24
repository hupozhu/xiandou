package cn.sampson.android.xiandou.core.presenter;

/**
 * Created by chengyang on 2017/7/18.
 */

public interface MyReplyCommentPresenter {

    String GET_MY_REPLY = "getMyReply";

    void getMyReply(String path, int page, int num);

}
