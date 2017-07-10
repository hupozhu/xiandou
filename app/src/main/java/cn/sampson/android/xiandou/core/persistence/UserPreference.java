package cn.sampson.android.xiandou.core.persistence;

import android.content.SharedPreferences;

import cn.sampson.android.xiandou.utils.ContextUtil;

/**
 * Created by Administrator on 2016/5/6.
 */
public class UserPreference {

    private static SharedPreferences preference = null;

    private final static String XIANDOU_USER = "xiandou_user";

    private static void instance() {
        if (preference == null) {
            preference = ContextUtil.getContext().getSharedPreferences(XIANDOU_USER, 0);
        }
    }

    /**
     * 获取用户token
     */
    public static String getToken() {
        instance();
        String s = null;
        if (preference.contains(TOKEN)) {
            s = preference.getString(TOKEN, null);
        }
        return s;
    }

    /**
     * 设置用户token
     */
    public static void setToken(String token) {
        instance();
        SharedPreferences.Editor ed = preference.edit();
        ed.putString(TOKEN, token);
        ed.commit();
    }

    /**
     * 获取用户sign
     */
    public static String getSign() {
        instance();
        String s = null;
        if (preference.contains(SIGN)) {
            s = preference.getString(SIGN, null);
        }
        return s;
    }

    /**
     * 设置用户sign
     */
    public static void setSign(String token) {
        instance();
        SharedPreferences.Editor ed = preference.edit();
        ed.putString(SIGN, token);
        ed.commit();
    }

    /**
     * 获取用户uid
     */
    public static String getUid() {
        instance();
        String s = null;
        if (preference.contains(UID)) {
            s = preference.getString(UID, null);
        }
        return s;
    }

    /**
     * 设置用户uid
     */
    public static void setUid(String token) {
        instance();
        SharedPreferences.Editor ed = preference.edit();
        ed.putString(UID, token);
        ed.commit();
    }

    /**
     * 获取用户名
     */
    public static String getUserName() {
        instance();
        String s = null;
        if (preference.contains(USERNAME)) {
            s = preference.getString(USERNAME, null);
        }
        return s;
    }

    /**
     * 设置用户名
     */
    public static void setUserName(String token) {
        instance();
        SharedPreferences.Editor ed = preference.edit();
        ed.putString(USERNAME, token);
        ed.commit();
    }

    /**
     * 获取昵称
     */
    public static String getNickname() {
        instance();
        String s = null;
        if (preference.contains(NICKNAME)) {
            s = preference.getString(NICKNAME, null);
        }
        return s;
    }

    /**
     * 设置昵称
     */
    public static void setNickname(String token) {
        instance();
        SharedPreferences.Editor ed = preference.edit();
        ed.putString(NICKNAME, token);
        ed.commit();
    }

    /**
     * 获取电话
     */
    public static String getUserTel() {
        instance();
        String s = null;
        if (preference.contains(USERTEL)) {
            s = preference.getString(USERTEL, null);
        }
        return s;
    }

    /**
     * 设置电话
     */
    public static void setUserTel(String token) {
        instance();
        SharedPreferences.Editor ed = preference.edit();
        ed.putString(USERTEL, token);
        ed.commit();
    }

    /**
     * 获取电话
     */
    public static String getUserSex() {
        instance();
        String s = null;
        if (preference.contains(USERSEX)) {
            s = preference.getString(USERSEX, null);
        }
        return s;
    }

    /**
     * 设置电话
     */
    public static void setUserSex(String token) {
        instance();
        SharedPreferences.Editor ed = preference.edit();
        ed.putString(USERSEX, token);
        ed.commit();
    }

    /**
     * 获取地址
     */
    public static String getArea() {
        instance();
        String s = null;
        if (preference.contains(AREA)) {
            s = preference.getString(AREA, null);
        }
        return s;
    }

    /**
     * 设置地址
     */
    public static void setArea(String token) {
        instance();
        SharedPreferences.Editor ed = preference.edit();
        ed.putString(AREA, token);
        ed.commit();
    }

    /**
     * 获取头像
     */
    public static String getAvatar() {
        instance();
        String s = null;
        if (preference.contains(USERPIC)) {
            s = preference.getString(USERPIC, null);
        }
        return s;
    }

    /**
     * 设置头像
     */
    public static void setAvatar(String token) {
        instance();
        SharedPreferences.Editor ed = preference.edit();
        ed.putString(USERPIC, token);
        ed.commit();
    }

    /**
     * 获取生日
     */
    public static String getBirthday() {
        instance();
        String s = null;
        if (preference.contains(BIRTHDAY)) {
            s = preference.getString(BIRTHDAY, null);
        }
        return s;
    }

    /**
     * 设置生日
     */
    public static void setBirthday(String token) {
        instance();
        SharedPreferences.Editor ed = preference.edit();
        ed.putString(BIRTHDAY, token);
        ed.commit();
    }


    public static void clear() {
        instance();
        SharedPreferences.Editor ed = preference.edit();
        ed.putString(TOKEN, "");
        ed.putString(SIGN, "");
        ed.putString(UID, "");

        ed.putString(USERNAME, "");
        ed.putString(NICKNAME, "");
        ed.putString(USERTEL, "");
        ed.putString(USERSEX, "");
        ed.putString(AREA, "");
        ed.putString(USERPIC, "");
        ed.putString(BIRTHDAY, "");
        ed.commit();
    }


    private final static String TOKEN = "token";
    private final static String SIGN = "sign";
    private final static String UID = "uid";

    private final static String USERNAME = "username";
    private final static String NICKNAME = "nickname";
    private final static String USERTEL = "userTel";
    private final static String USERSEX = "userSex";
    private final static String AREA = "area";
    private final static String USERPIC = "userPic";
    private final static String BIRTHDAY = "birthday";


}
