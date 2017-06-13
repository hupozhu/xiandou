package cn.sampson.android.xiandou.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import cn.sampson.android.xiandou.BaseApp;
import cn.sampson.android.xiandou.core.AppCache;


/**
 * 设计到需要上下文对象才能获取的参数，都可以用这个工具类直接去到
 *
 * Created by chengyang on 2017/1/4.
 */

public class ContextUtil {

    /**
     * 替代findviewById方法
     */
    public static <T extends View> T find(View view, int id) {
        return (T) view.findViewById(id);
    }

    public static <T extends View> T find(Activity context, int id){
        return  (T) context.findViewById(id);
    }

    /**
     * 得到上下文
     */
    public static Context getContext(){
        return AppCache.getContext();
    }

    /**
     * 得到Resouce对象
     */
    public static Resources getResource() {
        return getContext().getResources();
    }

    /**
     * 得到String.xml中的字符串
     */
    public static String getString(int resId) {
        return getResource().getString(resId);
    }

    /**
     * 得到String.xml中的字符串,带占位符
     */
    public static String getString(int id, Object... formatArgs) {
        return getResource().getString(id, formatArgs);
    }

    /**
     * 得到String.xml中的字符串数组
     */
    public static String[] getStringArr(int resId) {
        return getResource().getStringArray(resId);
    }

    /**
     * 得到colors.xml中的颜色
     */
    public static int getColor(int colorId) {
        return getResource().getColor(colorId);
    }

    public static int getScreenWidth() {
        return getResource().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return getResource().getDisplayMetrics().heightPixels;
    }

    public static String getScreenDpi() {
        return getScreenWidth() + "*" + getScreenHeight();
    }

    /**
     * dip-->px
     */
    public static int dip2Px(int dip) {
        // px/dip = density;
        float density = getResource().getDisplayMetrics().density;
        int px = (int) (dip * density + .5f);
        return px;
    }

    /**
     * px-->dip
     */
    public static int px2Dip(int px) {
        // px/dip = density;
        float density = getResource().getDisplayMetrics().density;
        int dip = (int) (px / density + .5f);
        return dip;
    }
}
