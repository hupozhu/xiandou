package cn.sampson.android.xiandou.ui.haoyun.yunyu.taijiaoyinyue;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.config.Constants;
import cn.sampson.android.xiandou.core.AppCache;
import cn.sampson.android.xiandou.core.retroft.Api.FetalTrainingApi;
import cn.sampson.android.xiandou.core.retroft.RetrofitWapper;
import cn.sampson.android.xiandou.core.retroft.base.BasePresenter;
import cn.sampson.android.xiandou.core.retroft.base.IView;
import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.model.Musics;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.taijiaoyinyue.service.PlayService;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;
import cn.sampson.android.xiandou.utils.widget.adapter.baseadapter.QuickRecycleViewAdapter;
import cn.sampson.android.xiandou.utils.widget.adapter.baseadapter.ViewHelper;

/**
 * Created by chengyang on 2017/6/12.
 */

public class MusicListActivity extends BaseActivity implements IView {

    public static final String MONTH = "month";

    @Bind(R.id.list)
    RecyclerView list;
    @Bind(R.id.fl_root)
    FrameLayout flRoot;

    private int month;
    private MusicListPresenter mPresenter;
    private QuickRecycleViewAdapter<Musics> mAdapter;

    private ServiceConnection mPlayServiceConnection;
    private Handler myHandle = new Handler(Looper.getMainLooper());


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setActionBarBack();

        setContentView(R.layout.activity_music_list);
        ButterKnife.bind(this);
        checkServiceAlive();

        month = getIntent().getIntExtra(MONTH, 0);
        if (month <= 0)
            return;
        getSupportActionBar().setTitle(month + "月");
        initView();
    }

    /**
     * 判断服务是否存在
     */
    private void checkServiceAlive() {
        if (AppCache.getPlayService() == null) {
            startService();
        }
    }

    /**
     * 开启服务
     */
    private void startService() {
        Intent intent = new Intent(this, PlayService.class);
        startService(intent);
        myHandle.postDelayed(new Runnable() {
            @Override
            public void run() {
                bindService();
            }
        }, 500);
    }

    /**
     * 绑定服务获取服务对象
     */
    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(this, PlayService.class);
        mPlayServiceConnection = new PlayServiceConnection();
        bindService(intent, mPlayServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void initView() {
        mPresenter = new MusicListPresenterImpl(this, flRoot);
        mPresenter.getMusicList(month);
        list.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new QuickRecycleViewAdapter<Musics>(R.layout.item_music_info, new ArrayList<Musics>()) {
            @Override
            protected void onBindData(Context context, final int position, Musics item, int itemLayoutId, ViewHelper helper) {
                helper.setText(R.id.music_name, item.name);
                helper.getRootView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(PlayActivity.MUSIC_POSITION, position);
                        jumpTo(PlayActivity.class, bundle);
                    }
                });
            }
        };
        list.setAdapter(mAdapter);

        BannerView banner = new BannerView(this, ADSize.BANNER, Constants.APPID, Constants.BannerPosID);
        //设置广告轮播时间，为0或30~120之间的数字，单位为s,0标识不自动轮播
        banner.setRefresh(30);
        banner.setADListener(new AbstractBannerADListener() {

            @Override
            public void onNoAD(int arg0) {
                Log.i("AD_DEMO", "BannerNoAD，eCode=" + arg0);
            }

            @Override
            public void onADReceiv() {
                Log.i("AD_DEMO", "ONBannerReceive");
            }
        });
        mAdapter.addHeaderView(banner);
        /* 发起广告请求，收到广告数据后会展示数据 */
        banner.loadAD();
    }

    public void setMusicList(ArrayList<Musics> datas) {
        AppCache.getMusicList().clear();
        AppCache.getMusicList().addAll(datas);
        mAdapter.setRefresh(datas, datas.size());
    }

    @Override
    public void setError(int errorCode, String error, String key) {

    }


    class MusicListPresenterImpl extends BasePresenter<MusicListActivity> implements MusicListPresenter {

        public MusicListPresenterImpl(MusicListActivity view, ViewGroup rootView) {
            super(view, rootView);
        }

        @Override
        protected void onResult(Result result, String key) {
            switch (key) {
                case GET_MUSIC_LIST:
                    setMusicList((ArrayList<Musics>) result.data);
                    break;
            }
        }

        @Override
        public void getMusicList(int month) {
            requestData(RetrofitWapper.getInstance().getNetService(FetalTrainingApi.class).getMusicList(month), GET_MUSIC_LIST);
        }
    }

    private class PlayServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            final PlayService playService = ((PlayService.PlayBinder) service).getService();
            AppCache.setPlayService(playService);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出时，如果音乐没有播放则停掉音乐播放服务
        if (!AppCache.getPlayService().isPlaying()) {
            if (mPlayServiceConnection != null) {
                unbindService(mPlayServiceConnection);
            }
            AppCache.getPlayService().stop();
        }
    }
}
