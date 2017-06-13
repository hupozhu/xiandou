package cn.sampson.android.xiandou.utils;

import android.content.Context;
import android.os.StatFs;


import java.io.File;

import cn.sampson.android.xiandou.BaseApp;

/**
 * Created by chengyang on 2017/1/3.
 */

public class CacheUtil {

    /**
     * 获取默认的缓存路径
     */
    public static File getDefaultCacheDir() {
        return ContextUtil.getContext().getExternalCacheDir();
    }

    /**
     * 计算缓存容量
     */
    public static long calculateDiskCacheSize(File dir) {
        long size = 5 * 1024 * 1024;
        try {
            StatFs statFs = new StatFs(dir.getAbsolutePath());
            long available = ((long) statFs.getBlockCount()) * statFs.getBlockSize();
            // Target 2% of the total space.
            size = available / 50;
        } catch (IllegalArgumentException ignored) {
        }
        // Bound inside min/max size for disk cache.
        return Math.max(Math.min(size, 50 * 1024 * 1024), 5 * 1024 * 1024);
    }

    public static File createDefaultCacheDir() {
        return ContextUtil.getContext().getExternalCacheDir();
    }

}
