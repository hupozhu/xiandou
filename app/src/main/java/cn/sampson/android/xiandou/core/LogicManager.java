package cn.sampson.android.xiandou.core;

import android.text.TextUtils;

import cn.sampson.android.xiandou.core.persistence.UserPreference;
import cn.sampson.android.xiandou.ui.BaseActivity;


/**
 * Created by Administrator on 2016/5/25.
 */
public class LogicManager {


    public static boolean loginIntercept(BaseActivity context) {
        if (TextUtils.isEmpty(UserPreference.getToken())) {//用户还没有登录
            return true;
        }
        return false;
    }

    public static boolean isManagerAccount(BaseActivity context) {
        return false;
    }

}
