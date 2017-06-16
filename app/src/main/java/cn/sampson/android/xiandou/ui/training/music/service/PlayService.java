package cn.sampson.android.xiandou.ui.training.music.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import cn.sampson.android.xiandou.config.AppConfig;
import cn.sampson.android.xiandou.core.AppCache;
import cn.sampson.android.xiandou.ui.training.model.Musics;
import cn.sampson.android.xiandou.ui.training.music.Preferences;
import cn.sampson.android.xiandou.ui.training.music.receiver.Actions;
import cn.sampson.android.xiandou.ui.training.music.receiver.NoisyAudioStreamReceiver;
import cn.sampson.android.xiandou.utils.StringUtils;

/**
 * Created by chengyang on 2017/6/13.
 */

public class PlayService extends Service implements MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener {

    private static final String TAG = "Service";
    private static final long TIME_UPDATE = 100L;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;//准备中
    private static final int STATE_PLAYING = 2;//播放中
    private static final int STATE_PAUSE = 3;//暂停中

    //歌曲列表
    private List<Musics> mMusicList;
    //媒体播放器
    private MediaPlayer mediaPlayer = new MediaPlayer();
    //来电/耳机拔出时暂停播放
    private IntentFilter mNoisyFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    private NoisyAudioStreamReceiver mNoisyReceiver = new NoisyAudioStreamReceiver();
    private Handler mHandler = new Handler();
    //声音管理
    private AudioManager mAudioManager;
    //播放监听
    private OnPlayerEventListener mListener;
    //正在播放的歌曲
    private Musics mPlayingMusic;
    //正在播放歌曲在列表中的序号
    private int mPlayingPosition;
    //定时器倒计时
    private long quitTimerRemain;
    //播放状态
    private int playState = STATE_IDLE;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: " + getClass().getSimpleName());

        Preferences.init(this);
        //初始化声音服务
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mediaPlayer.setOnCompletionListener(this);
        Notifier.init(this);
    }

    /**
     * 开启服务的方法
     * <p>
     * 每次调用start方法走onStartCommand
     */
    public static void startCommand(Context context, String action) {
        Intent intent = new Intent(context, PlayService.class);
        intent.setAction(action);
        context.startService(intent);
    }

    public void updateMusicList(List<Musics> musics) {
        this.mMusicList = musics;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case Actions.ACTION_MEDIA_PLAY_PAUSE:
                    playPause();
                    break;
                case Actions.ACTION_MEDIA_NEXT:
                    next();
                    break;
                case Actions.ACTION_MEDIA_PREVIOUS:
                    prev();
                    break;
            }
        }
        return START_NOT_STICKY;
    }

    /**
     * 下一首
     */
    public void next() {
        if (mMusicList.isEmpty()) {
            return;
        }

        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
        switch (mode) {
            case SHUFFLE:
                mPlayingPosition = new Random().nextInt(mMusicList.size());
                play(mPlayingPosition);
                break;
            case SINGLE:
                play(mPlayingPosition);
                break;
            case LOOP:
            default:
                play(mPlayingPosition + 1);
                break;
        }
    }

    /**
     * 上一首
     */
    public void prev() {
        if (mMusicList.isEmpty()) {
            return;
        }

        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
        switch (mode) {
            case SHUFFLE:
                mPlayingPosition = new Random().nextInt(mMusicList.size());
                play(mPlayingPosition);
                break;
            case SINGLE:
                play(mPlayingPosition);
                break;
            case LOOP:
            default:
                play(mPlayingPosition - 1);
                break;
        }
    }

    /**
     * 跳转到指定的时间位置
     *
     * @param msec 时间
     */
    public void seekTo(int msec) {
        if (isPlaying() || isPausing()) {
            mediaPlayer.seekTo(msec);
            if (mListener != null) {
                mListener.onPublish(msec);
            }
        }
    }

    /**
     * 暂停/播放切换
     */
    public void playPause() {
        if (isPreparing()) {
            return;
        }

        if (isPlaying()) {
            pause();
        } else if (isPausing()) {
            resume();
        } else {
            play(getPlayingPosition());
        }
    }

    /**
     * 获取正在播放的本地歌曲的序号
     */
    public int getPlayingPosition() {
        return mPlayingPosition;
    }

    public void play(int position) {
        if (mMusicList.isEmpty()) {
            return;
        }

        if (position < 0) {
            position = mMusicList.size() - 1;
        } else if (position >= mMusicList.size()) {
            position = 0;
        }

        mPlayingPosition = position;
        Musics music = mMusicList.get(mPlayingPosition);
        play(music);
    }

    public void play(Musics music) {
        mPlayingMusic = music;
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(AppConfig.MUSIC_PATH + StringUtils.getUTF8String(music.name) + ".mp3");
            mediaPlayer.prepareAsync();
            playState = STATE_PREPARING;
            mediaPlayer.setOnPreparedListener(mPreparedListener);
            mediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            if (mListener != null) {
                mListener.onChange(music);
            }
            Notifier.showPlay(music);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停
     */
    private void pause() {
        if (!isPlaying()) {
            return;
        }

        mediaPlayer.pause();
        playState = STATE_PAUSE;
        mHandler.removeCallbacks(mPublishRunnable);
        Notifier.showPause(mPlayingMusic);
        mAudioManager.abandonAudioFocus(this);
        unregisterReceiver(mNoisyReceiver);
        if (mListener != null) {
            mListener.onPlayerPause();
        }
    }

    /**
     * 继续播放
     */
    private void resume() {
        if (!isPausing()) {
            return;
        }

        if (start()) {
            if (mListener != null) {
                mListener.onPlayerResume();
            }
        }
    }

    /**
     * 开始播放
     */
    private boolean start() {
        mediaPlayer.start();
        if (mediaPlayer.isPlaying()) {
            playState = STATE_PLAYING;
            mHandler.post(mPublishRunnable);
            Notifier.showPlay(mPlayingMusic);
            mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            registerReceiver(mNoisyReceiver, mNoisyFilter);
        }
        return mediaPlayer.isPlaying();
    }

    public boolean isPreparing() {
        return playState == STATE_PREPARING;
    }

    public boolean isPlaying() {
        return playState == STATE_PLAYING;
    }

    public boolean isPausing() {
        return playState == STATE_PAUSE;
    }

    /**
     * 获取正在播放的歌曲[本地|网络]
     */
    public Musics getPlayingMusic() {
        return mPlayingMusic;
    }

    private Runnable mPublishRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying() && mListener != null) {
                mListener.onPublish(mediaPlayer.getCurrentPosition());
            }
            mHandler.postDelayed(this, TIME_UPDATE);
        }
    };

    //准备完成后开始播放
    private MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            int duration = mp.getDuration();
            mPlayingMusic.duration = duration;
            if (mListener != null) {
                mListener.updateDuration(duration);
            }
            start();
        }
    };

    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            if (mListener != null) {
                mListener.onBufferingUpdate(percent);
            }
        }
    };

    public OnPlayerEventListener getOnPlayEventListener() {
        return mListener;
    }

    public void setOnPlayEventListener(OnPlayerEventListener listener) {
        mListener = listener;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PlayBinder();
    }

    // ============  MediaPlayer.OnCompletionListener
    @Override
    public void onCompletion(MediaPlayer mp) {
        next();
    }

    // ============  AudioManager.OnAudioFocusChangeListener
    @Override
    public void onAudioFocusChange(int i) {
        switch (i) {
            case AudioManager.AUDIOFOCUS_LOSS:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                pause();
                break;
        }
    }

    public void startQuitTimer(long milli) {
        stopQuitTimer();
        if (milli > 0) {
            quitTimerRemain = milli + DateUtils.SECOND_IN_MILLIS;
            mHandler.post(mQuitRunnable);
        } else {
            quitTimerRemain = 0;
            if (mListener != null) {
                mListener.onTimer(quitTimerRemain);
            }
        }
    }

    private void stopQuitTimer() {
        mHandler.removeCallbacks(mQuitRunnable);
    }

    private Runnable mQuitRunnable = new Runnable() {
        @Override
        public void run() {
            quitTimerRemain -= DateUtils.SECOND_IN_MILLIS;
            if (quitTimerRemain > 0) {
                if (mListener != null) {
                    mListener.onTimer(quitTimerRemain);
                }
                mHandler.postDelayed(this, DateUtils.SECOND_IN_MILLIS);
            } else {
                stop();
            }
        }
    };

    public void stop() {
        pause();
        stopQuitTimer();
        mediaPlayer.reset();
        mediaPlayer.release();
        mediaPlayer = null;
        Notifier.cancelAll();
        AppCache.setPlayService(null);
        stopSelf();
    }

    public class PlayBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }

    @Override
    public void onDestroy() {
        AppCache.setPlayService(null);
        super.onDestroy();
        Log.i(TAG, "onDestroy: " + getClass().getSimpleName());
    }
}
