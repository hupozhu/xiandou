package cn.sampson.android.xiandou.ui.haoyun.yunyu.taijiaoyinyue.service;

import cn.sampson.android.xiandou.model.Musics;

/**
 * 播放进度监听器
 * Created by hzwangchenyan on 2015/12/17.
 */
public interface OnPlayerEventListener {
    /**
     * 更新进度
     */
    void onPublish(int progress);

    /**
     * 缓冲百分比
     */
    void onBufferingUpdate(int percent);

    /**
     * 切换歌曲
     */
    void onChange(Musics music);

    /**
     * 暂停播放
     */
    void onPlayerPause();

    /**
     * 继续播放
     */
    void onPlayerResume();

    /**
     * 更新定时停止播放时间
     */
    void onTimer(long remain);

    /**
     * 更新音乐时长
     */
    void updateDuration(int duration);

    void onMusicListUpdate();
}