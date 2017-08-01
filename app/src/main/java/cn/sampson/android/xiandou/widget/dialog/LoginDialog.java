package cn.sampson.android.xiandou.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.core.event.GetUserInfoEvent;
import cn.sampson.android.xiandou.core.persistence.UserPreference;
import cn.sampson.android.xiandou.core.presenter.LoginPresenter;
import cn.sampson.android.xiandou.core.retroft.Api.UserApi;
import cn.sampson.android.xiandou.core.retroft.RetrofitWapper;
import cn.sampson.android.xiandou.core.retroft.base.BasePresenter;
import cn.sampson.android.xiandou.core.retroft.base.IView;
import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.ui.mine.domain.User;
import cn.sampson.android.xiandou.ui.mine.domain.UserToken;
import cn.sampson.android.xiandou.utils.ContextUtil;
import cn.sampson.android.xiandou.utils.ToastUtils;
import cn.sampson.android.xiandou.utils.UiUtils;
import de.greenrobot.event.EventBus;

/**
 * Created by chengyang on 2017/7/5.
 */

public class LoginDialog extends Dialog implements View.OnClickListener, IView {

    FrameLayout flClose;
    EditText etPhone;
    ImageView ivPhoneClear;
    EditText etAuthCode;
    TextView tvGetAuth;
    TextView tvLogin;
    TextView tvProtocol;
    private Window window;
    private Activity mContext;

    LoginPresenterImpl mPresenter;

    private final MyHandler mHandler = new MyHandler(this);


    private static class MyHandler extends Handler {
        private final WeakReference<LoginDialog> model;

        public MyHandler(LoginDialog activity) {
            model = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginDialog lmodel = model.get();
            if (lmodel != null) {
                switch (msg.what) {
                    case 1:
                        lmodel.countDown();
                        break;
                }
            }
        }
    }

    private int time = 59;

    public LoginDialog(Activity context) {
        super(context, R.style.LoginDialog);
        mContext = context;

        mPresenter = new LoginPresenterImpl(this);

        window = getWindow(); //得到对话框
        window.setWindowAnimations(R.style.dialogstyle); //设置窗口弹出动画

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_login, null);
        initView(view);
        setContentView(view);
        WindowManager.LayoutParams wl = window.getAttributes();
        //根据x，y坐标设置窗口需要显示的位置
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.gravity = Gravity.BOTTOM;

        window.setAttributes(wl);
        //设置触摸对话框意外的地方取消对话框
        setCanceledOnTouchOutside(false);
    }

    //设置窗口显示
    public void showDialog() {
        if (isShowing())
            return;

        show();
    }

    public void dismissDialog() {
        if (!isShowing())
            return;

        mHandler.removeMessages(1);
        dismiss();
    }

    /**
     * 显示当前对话框
     */
    public void initView(View mWindow) {
        flClose = UiUtils.find(mWindow, R.id.fl_close);
        etPhone = UiUtils.find(mWindow, R.id.et_phone);
        ivPhoneClear = UiUtils.find(mWindow, R.id.iv_phone_clear);
        etAuthCode = UiUtils.find(mWindow, R.id.et_auth_code);
        tvGetAuth = UiUtils.find(mWindow, R.id.tv_get_auth);
        tvLogin = UiUtils.find(mWindow, R.id.tv_login);
        tvProtocol = UiUtils.find(mWindow, R.id.tv_protocol);

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkInput(editable);
            }
        });
        tvGetAuth.setEnabled(false);

        flClose.setOnClickListener(this);
        ivPhoneClear.setOnClickListener(this);
        tvGetAuth.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvProtocol.setOnClickListener(this);
    }

    private void checkInput(Editable editable) {
        if (editable.length() == 11) {
            tvGetAuth.setEnabled(true);
        } else {
            tvGetAuth.setEnabled(false);
        }

        if (editable.length() > 0) {
            ivPhoneClear.setVisibility(View.VISIBLE);
        } else {
            ivPhoneClear.setVisibility(View.GONE);
        }
    }

    /**
     * 获取验证码
     */
    private void getAuthCode() {
        mHandler.sendEmptyMessage(1);

        mPresenter.getAuthCode(etPhone.getText().toString());
    }

    /**
     * 倒计时
     */
    private void countDown() {
        if (time == 0) {
            time = 59;
            tvGetAuth.setEnabled(true);
            tvGetAuth.setText(mContext.getString(R.string.get_auth_code));
            checkInput(etAuthCode.getText());
        } else {
            tvGetAuth.setEnabled(false);
            tvGetAuth.setText(String.valueOf(time));
            mHandler.sendEmptyMessageDelayed(1, 1000);
            time--;
        }
    }

    private void login() {
        if (TextUtils.isEmpty(etPhone.getText())) {
            ToastUtils.show("请填写手机号码");
            return;
        }
        if (TextUtils.isEmpty(etAuthCode.getText())) {
            ToastUtils.show("请填写验证码");
            return;
        }
        mPresenter.login(etPhone.getText().toString(), etAuthCode.getText().toString());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fl_close:
                dismissDialog();
                break;
            case R.id.iv_phone_clear:
                etPhone.setText("");
                break;
            case R.id.tv_get_auth://获取验证码
                getAuthCode();
                break;
            case R.id.tv_login://登录
                login();
                break;
            case R.id.tv_protocol://跳转到用户协议

                break;
        }
    }

    @Override
    public void setError(int errorCode, String error, String key) {
        switch (key) {
            case LoginPresenter.LOGIN:
                break;
        }
    }

    void onLoginSuccess(UserToken user) {
        UserPreference.setToken(user.token);
        UserPreference.setSign(user.sign);
        UserPreference.setUid(user.uid);
        UserPreference.setUserTel(etPhone.getText().toString());
        dismissDialog();
        EventBus.getDefault().post(new GetUserInfoEvent());
    }

    void getAuthCodeSuccess() {

    }

    class LoginPresenterImpl extends BasePresenter<LoginDialog> implements LoginPresenter {

        public LoginPresenterImpl(LoginDialog view) {
            super(view);
        }

        @Override
        public void login(String phone, String code) {
            requestData(RetrofitWapper.getInstance().getNetService(UserApi.class).login(phone, code), LOGIN);
        }

        @Override
        public void getAuthCode(String phone) {
            requestData(RetrofitWapper.getInstance().getNetService(UserApi.class).getAuthCode(phone), GET_AUTH_CODE);
        }

        @Override
        protected void onResult(Result result, String key) {
            switch (key) {
                case LOGIN:
                    onLoginSuccess((UserToken) result.data);
                    break;

                case GET_AUTH_CODE:
                    getAuthCodeSuccess();
                    break;
            }
        }
    }
}
