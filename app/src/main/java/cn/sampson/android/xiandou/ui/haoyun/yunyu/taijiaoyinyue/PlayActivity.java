package cn.sampson.android.xiandou.ui.haoyun.yunyu.taijiaoyinyue;

import android.content.ComponentName;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.config.AppCache;
import cn.sampson.android.xiandou.ui.SplashActivity;
import cn.sampson.android.xiandou.model.Musics;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.taijiaoyinyue.receiver.RemoteControlReceiver;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.taijiaoyinyue.service.Extras;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.taijiaoyinyue.service.OnPlayerEventListener;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.taijiaoyinyue.service.PlayModeEnum;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.taijiaoyinyue.service.PlayService;
import cn.sampson.android.xiandou.utils.ScreenUtils;
import cn.sampson.android.xiandou.utils.StringUtils;
import cn.sampson.android.xiandou.utils.ToastUtils;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;
import cn.sampson.android.xiandou.widget.playmusic.AlbumCoverView;

import static android.R.attr.duration;

/**
 * 音乐播放页面
 * Created by chengyang on 2017/6/12.
 */

public class PlayActivity extends AppCompatActivity implements OnPlayerEventListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    public static final String MUSIC_POSITION = "music_position";

    @Bind(R.id.iv_play_page_bg)
    ImageView ivPlayPageBg;
    @Bind(R.id.tv_current_time)
    TextView tvCurrentTime;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.sb_progress)
    AppCompatSeekBar sbProgress;
    @Bind(R.id.tv_total_time)
    TextView tvTotalTime;
    @Bind(R.id.iv_mode)
    ImageView ivMode;
    @Bind(R.id.iv_prev)
    ImageView ivPrev;
    @Bind(R.id.iv_play)
    ImageView ivPlay;
    @Bind(R.id.iv_next)
    ImageView ivNext;
    @Bind(R.id.ll_content)
    LinearLayout llContent;
    @Bind(R.id.fl_back)
    FrameLayout flBack;
    @Bind(R.id.album_cover_view)
    AlbumCoverView albumCoverView;

    private AudioManager mAudioManager;
    private ComponentName mRemoteReceiver;

    private int mLastProgress;
    private int position;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);
        StatusBarUtil.transparencyBar(this);
//        initSystemBar();

        if (AppCache.getPlayService() == null) {
            finish();
            return;
        }
        getPlayService().updateMusicList(AppCache.getMusicList());
        getPlayService().setOnPlayEventListener(this);
        //耳机线控
        registerReceiver();

        parseIntent();
        initView();
    }

    private void initView() {
        initSystemBar();
        initPlayMode();
        setListener();
    }

    /**
     * 沉浸式状态栏
     */
    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int top = ScreenUtils.getSystemBarHeight();
            llContent.setPadding(0, top, 0, 0);
        }
    }

    private void initPlayMode() {
        int mode = Preferences.getPlayMode();
        ivMode.setImageLevel(mode);
    }

    protected void setListener() {
        ivMode.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        ivPrev.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        flBack.setOnClickListener(this);
        sbProgress.setOnSeekBarChangeListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        parseIntent();
    }

    private void parseIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(Extras.EXTRA_NOTIFICATION)) {
            setIntent(new Intent());
            onChange(getPlayService().getPlayingMusic());
        } else {
            position = intent.getIntExtra(MUSIC_POSITION, 0);
            getPlayService().play(position);
        }
    }


    private void registerReceiver() {
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mRemoteReceiver = new ComponentName(getPackageName(), RemoteControlReceiver.class.getName());
        mAudioManager.registerMediaButtonEventReceiver(mRemoteReceiver);
    }

    private void switchPlayMode() {
        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
        switch (mode) {
            case LOOP:
                mode = PlayModeEnum.SHUFFLE;
                ToastUtils.show(R.string.mode_shuffle);
                break;
            case SHUFFLE:
                mode = PlayModeEnum.SINGLE;
                ToastUtils.show(R.string.mode_one);
                break;
            case SINGLE:
                mode = PlayModeEnum.LOOP;
                ToastUtils.show(R.string.mode_loop);
                break;
        }
        Preferences.savePlayMode(mode.value());
        initPlayMode();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_back:
                onBackPressed();
                break;
            case R.id.iv_mode:
                switchPlayMode();
                break;
            case R.id.iv_play:
                play();
                break;
            case R.id.iv_next:
                next();
                break;
            case R.id.iv_prev:
                prev();
                break;
        }
    }

    private void play() {
        getPlayService().playPause();
    }

    private void next() {
        getPlayService().next();
    }

    private void prev() {
        getPlayService().prev();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (getPlayService().isPlaying() || getPlayService().isPausing()) {
            int progress = seekBar.getProgress();
            getPlayService().seekTo(progress);
            tvCurrentTime.setText(formatTime(progress));
            mLastProgress = progress;
        } else {
            seekBar.setProgress(0);
        }
    }


    //============= 音乐播放状态的回调函数 ===============
    @Override
    public void onPublish(int progress) {
        sbProgress.setProgress(progress);
        //更新当前播放时间
        if (progress - mLastProgress >= 1000) {
            tvCurrentTime.setText(formatTime(progress));
            mLastProgress = progress;
        }
    }

    @Override
    public void onBufferingUpdate(int percent) {
        sbProgress.setSecondaryProgress(sbProgress.getMax() * 100 / percent);
    }

    @Override
    public void onChange(Musics music) {
        onPlay(music);
    }

    @Override
    public void updateDuration(int duration) {
        sbProgress.setMax(duration);
        tvTotalTime.setText(formatTime(duration));
    }

    private void onPlay(Musics music) {
        if (music == null) {
            return;
        }

        tvTitle.setText(music.name);
        sbProgress.setProgress(0);
        sbProgress.setSecondaryProgress(0);
        if (music.duration > 0) {
            sbProgress.setMax(music.duration);
            tvTotalTime.setText(formatTime(duration));
        }
        mLastProgress = 0;
        tvCurrentTime.setText(R.string.play_time_start);

        if (getPlayService().isPlaying() || getPlayService().isPreparing()) {
            ivPlay.setSelected(true);
            albumCoverView.start();
        } else {
            ivPlay.setSelected(false);
            albumCoverView.pause();
        }
    }

    @Override
    public void onPlayerPause() {
        ivPlay.setSelected(false);
        albumCoverView.pause();
    }

    @Override
    public void onPlayerResume() {
        ivPlay.setSelected(true);
        albumCoverView.start();
    }

    @Override
    public void onTimer(long remain) {

    }

    @Override
    public void onMusicListUpdate() {

    }

    private String formatTime(long time) {
        return StringUtils.formatTime("mm:ss", time);
    }

    public PlayService getPlayService() {
        PlayService playService = AppCache.getPlayService();
        if (playService == null) {
            throw new NullPointerException("play service is null");
        }
        return playService;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null == AppCache.getMainActivity()) {
            startActivity(new Intent(PlayActivity.this, SplashActivity.class));
        }
    }
}
