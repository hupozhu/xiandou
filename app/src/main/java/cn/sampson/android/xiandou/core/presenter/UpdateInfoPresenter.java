package cn.sampson.android.xiandou.core.presenter;

import java.util.Map;

/**
 * Created by chengyang on 2017/7/11.
 */

public interface UpdateInfoPresenter {

    String UPDATE_USER_INFO = "updateUserInfo";

    void upateUserInfo(Map<String, String> map);
}
