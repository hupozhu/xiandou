package cn.sampson.android.xiandou.config;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cn.sampson.android.xiandou.ui.community.domain.CommunityCategory;
import cn.sampson.android.xiandou.ui.main.MainActivity;
import cn.sampson.android.xiandou.model.Musics;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.taijiaoyinyue.service.PlayService;

/**
 * Created by chengyang on 2017/6/13.
 */

public class AppCache {

    private Context mContext;

    //七牛Token
    public String QiNiuToken;

    //正在播放歌曲列表
    private final List<Musics> mMusicList = new ArrayList<>();

    //正在播放歌曲的位置
    private int playingPosition;

    //后台播放音乐服务
    private PlayService mPlayService;

    private MainActivity mainActivity;

    //社区分类列表
    private List<CommunityCategory> communityCategories;

    public static int cropNum = 0;

    //============== instance ================
    private AppCache() {
    }

    private static class SingletonHolder {
        private static AppCache sAppCache = new AppCache();
    }

    private static AppCache getInstance() {
        return SingletonHolder.sAppCache;
    }

    public static void init(Application application) {
        getInstance().onInit(application);
    }

    private void onInit(Application application) {
        mContext = application.getApplicationContext();
    }

    // ================= data ===================
    public static Context getContext() {
        return getInstance().mContext;
    }

    public static String getQiniuToken() {
        return getInstance().QiNiuToken;
    }

    public static void setQiniuToken(String token) {
        getInstance().QiNiuToken = token;
    }

    public static List<Musics> getMusicList() {
        return getInstance().mMusicList;
    }

    public static void setPlayService(PlayService service) {
        getInstance().mPlayService = service;
    }

    public static List<CommunityCategory> getCommunityCategories() {
        return getInstance().communityCategories;
    }

    public static void setCommunityCategories(List<CommunityCategory> categories) {
        getInstance().communityCategories = categories;
    }

    public static PlayService getPlayService() {
        return getInstance().mPlayService;
    }

    public static void setPlayingPosition(int position) {
        getInstance().playingPosition = position;
    }

    public static int getPlayingPosition() {
        return getInstance().playingPosition;
    }

    public static void setMainActiivty(MainActivity activity) {
        getInstance().mainActivity = activity;
    }

    public static MainActivity getMainActivity() {
        return getInstance().mainActivity;
    }

}
