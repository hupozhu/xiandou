package cn.sampson.android.xiandou.core.presenter;

/**
 * Created by chengyang on 2017/7/6.
 */

public interface LoginPresenter {

    String LOGIN = "login";

    String GET_AUTH_CODE = "getAuthCode";

    void login(String phone, String code);

    void getAuthCode(String phone);

}
