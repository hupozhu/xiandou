package cn.sampson.android.xiandou;

import android.app.Application;

import cn.sampson.android.xiandou.core.AppCache;
import cn.sampson.android.xiandou.utils.ContextUtil;
import cn.sampson.android.xiandou.utils.ScreenUtils;
import cn.sampson.android.xiandou.utils.ToastUtils;

/**
 * Created by chengyang on 2017/1/3.
 */

public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppCache.init(this);
        ScreenUtils.init(this);
        ToastUtils.init(this);
    }
}
