package cn.sampson.android.xiandou.ui.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.core.AppCache;
import cn.sampson.android.xiandou.core.event.GetUserInfoEvent;
import cn.sampson.android.xiandou.core.persistence.UserPreference;
import cn.sampson.android.xiandou.core.presenter.UserPresenter;
import cn.sampson.android.xiandou.core.retroft.Api.UserApi;
import cn.sampson.android.xiandou.core.retroft.RetrofitWapper;
import cn.sampson.android.xiandou.core.retroft.base.BasePresenter;
import cn.sampson.android.xiandou.core.retroft.base.IView;
import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.ui.BaseFragment;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.taijiaoyinyue.service.PlayService;
import cn.sampson.android.xiandou.ui.mine.domain.User;
import cn.sampson.android.xiandou.utils.ToastUtils;
import cn.sampson.android.xiandou.utils.imageloader.ImageLoader;
import cn.sampson.android.xiandou.widget.dialog.LoadingDialog;
import cn.sampson.android.xiandou.widget.dialog.LoginDialog;
import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2017/6/5.
 */

public class MineFragment extends BaseFragment implements View.OnClickListener, IView {


    @Bind(R.id.riv_avatar)
    RoundedImageView rivAvatar;
    @Bind(R.id.rl_user_info)
    RelativeLayout rlUserInfo;
    @Bind(R.id.ll_my_note)
    LinearLayout llMyNote;
    @Bind(R.id.ll_reply)
    LinearLayout llReply;
    @Bind(R.id.ll_collect)
    LinearLayout llCollect;
    @Bind(R.id.ll_about_us)
    LinearLayout llAboutUs;
    @Bind(R.id.ll_current)
    LinearLayout llCurrent;
    @Bind(R.id.ll_not_login)
    LinearLayout llNotLogin;
    @Bind(R.id.rl_login)
    RelativeLayout rlLogin;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;

    UserPresenter mPresenter;
    LoginDialog loginDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        loginDialog = new LoginDialog(getActivity());
        mPresenter = new UserPresenterImpl(this);
        EventBus.getDefault().register(this);

        llNotLogin.setOnClickListener(this);
        rlLogin.setOnClickListener(this);

        rivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(UserPreference.getToken())) {
                    loginDialog.showDialog();
                } else {
                    getActivity().startActivity(new Intent(getActivity(), UserInfoActivity.class));
                }
            }
        });
        checkLogin();
        mPresenter.getUserInfo();
    }

    public void onEvent(GetUserInfoEvent event) {
        LoadingDialog.showProgress(getContext());
        mPresenter.getUserInfo();
    }

    /**
     * 检查一下登录状态
     */
    private void checkLogin() {
        if (TextUtils.isEmpty(UserPreference.getToken())) {
            llNotLogin.setVisibility(View.VISIBLE);
            rlLogin.setVisibility(View.GONE);
        } else {
            llNotLogin.setVisibility(View.GONE);
            rlLogin.setVisibility(View.VISIBLE);
        }
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
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_not_login:
                LoginDialog dialog = new LoginDialog(getActivity());
                dialog.showDialog();
                break;

            case R.id.rl_login:
                //跳转到设置详情页面
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
        }
    }

    @Override
    public void setError(int errorCode, String error, String key) {
        switch (key) {
            case UserPresenter.GET_USER_INFO:
                break;
        }
    }

    public void setUserInfo(User user) {
        LoadingDialog.dismissProgress(getContext());
        checkLogin();
        tvNickname.setText(user.nickname);
        ImageLoader.loadAvatar(getContext(), user.userPic, rivAvatar);

        UserPreference.setUser(user);
    }

    public class UserPresenterImpl extends BasePresenter<MineFragment> implements UserPresenter {


        public UserPresenterImpl(MineFragment view) {
            super(view);
        }

        @Override
        public void getUserInfo() {
            requestData(RetrofitWapper.getInstance().getNetService(UserApi.class).getUserInfo(), GET_USER_INFO);
        }

        @Override
        protected void onResult(Result result, String key) {
            switch (key) {
                case GET_USER_INFO:
                    setUserInfo((User) result.data);
                    break;
            }
        }
    }
}
