package cn.sampson.android.xiandou.ui.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.core.AppCache;
import cn.sampson.android.xiandou.ui.BaseFragment;
import cn.sampson.android.xiandou.ui.training.music.service.PlayService;
import cn.sampson.android.xiandou.utils.ToastUtils;

/**
 * Created by Administrator on 2017/6/5.
 */

public class SettingsFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.rl_play_settings)
    RelativeLayout mRlPlaySettings;
    @Bind(R.id.rl_current_version)
    RelativeLayout mRlCurrentVersion;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, null);
        ButterKnife.bind(this, view);
        mRlCurrentVersion.setOnClickListener(this);
        mRlPlaySettings.setOnClickListener(this);
        return view;
    }

    private void showTimerDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.menu_timer)
                .setItems(getContext().getResources().getStringArray(R.array.timer_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int[] times = getContext().getResources().getIntArray(R.array.timer_int);
                        startTimer(times[which]);
                    }
                })
                .show();
    }

    private void startTimer(int minute) {
        PlayService service = AppCache.getPlayService();
        if (null != service) {
            service.startQuitTimer(minute * 60 * 1000);
            if (minute > 0) {
                ToastUtils.show(getContext().getString(R.string.timer_set, String.valueOf(minute)));
            } else {
                ToastUtils.show(R.string.timer_cancel);
            }
        } else {
            ToastUtils.show("请播放音频后再设置！");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_play_settings:
                showTimerDialog();
                break;
            case R.id.rl_current_version:
                break;
        }
    }
}
