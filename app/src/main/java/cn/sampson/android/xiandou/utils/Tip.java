package cn.sampson.android.xiandou.utils;

import android.util.Log;

/**
 * Created by Administrator on 2016/3/22 0022.
 */
public class Tip {

    static String AppName = "drama_log";
    static String NewWork = "drama_net";

    public static Boolean isTesting = true;

    public static void setTesting() {
        isTesting = true;
    }

    public static void i(String string) {
        if (isTesting) {
            Log.i(AppName, string);
        }
    }

    public static void w(String string) {
        if (isTesting) {
            Log.e(AppName, string);
        }
    }

    public static void d(String string) {
        if (isTesting) {
            Log.d(AppName, string);
        }
    }

    public static void a(int num) {
        if (isTesting) {
            Log.d(AppName, Integer.toString(num));
        }
    }

    public static void e(Exception exception) {
        if (isTesting) {
            Log.e(AppName, exception.toString());
        }
    }

    public static void d(String tag, String msg) {
        if (isTesting) {
            Log.d(tag, msg);
        }
    }

    public static void net(String msg) {
        if (isTesting) {
            Log.d(NewWork, msg);
        }
    }
}
