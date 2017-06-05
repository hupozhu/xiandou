package cn.sampson.android.xiandou;

import android.app.Application;

/**
 * Created by chengyang on 2017/1/3.
 */

public class BaseApp extends Application {

    private static BaseApp app = null;

    public static BaseApp getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }
}
