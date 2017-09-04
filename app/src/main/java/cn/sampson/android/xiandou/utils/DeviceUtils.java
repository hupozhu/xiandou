package cn.sampson.android.xiandou.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;

import cn.sampson.android.xiandou.R;

/**
 * Created by Administrator on 2016/5/13.
 */
public class DeviceUtils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        if (wifi | internet) {
            //执行相关操作
            return true;
        } else {
            ToastUtils.show(R.string.network_error);
            return false;
        }
    }

    public static boolean isSamsung() {
        String phoneName = Build.MODEL.substring(0, 2);
        if ("SM".equalsIgnoreCase(phoneName)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取当前手机的网络状态
     */
    public static String getCurrentNetType() {
        String type = "";
        ConnectivityManager cm = (ConnectivityManager) ContextUtil.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            type = "null";
        } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            type = "WIFI";
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int subType = info.getSubtype();

            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    type = "2G";
                    break;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    type = "3G";
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    type = "4G";
                    break;
                default:
                    type = "nuKnow";
                    break;
            }
        }
        return type;
    }

    /**
     * 获取当前的版本号
     *
     * @return
     */
    public static String getAppVersionName() {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = ContextUtil.getContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ContextUtil.getContext().getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Tip.e(e);
        }
        return "V" + versionName;
    }

    /**
     * 调到系统的拨号器
     *
     * @param context：上下文
     * @param phoneNumber：电话号码
     */
    public static void callUpBySystem(Context context, String phoneNumber) {
        Uri dialUri = Uri.parse("tel:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_DIAL, dialUri);
        context.startActivity(intent);
    }
}
