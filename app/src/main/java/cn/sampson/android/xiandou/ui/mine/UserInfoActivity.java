package cn.sampson.android.xiandou.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.config.Constants;
import cn.sampson.android.xiandou.core.persistence.UserPreference;
import cn.sampson.android.xiandou.core.presenter.UpdateInfoPresenter;
import cn.sampson.android.xiandou.core.retroft.Api.UserApi;
import cn.sampson.android.xiandou.core.retroft.RetrofitWapper;
import cn.sampson.android.xiandou.core.retroft.base.BasePresenter;
import cn.sampson.android.xiandou.core.retroft.base.IView;
import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.model.Data;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.utils.ToastUtils;
import cn.sampson.android.xiandou.utils.imageloader.ImageLoader;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;
import cn.sampson.android.xiandou.widget.timepicker.picker.DateTimePicker;
import cn.sampson.android.xiandou.widget.timepicker.utils.DateUtils;

/**
 * Created by chengyang on 2017/7/7.
 */

public class UserInfoActivity extends BaseActivity implements View.OnClickListener, IView {
    
    @Bind(R.id.riv_avatar)
    RoundedImageView rivAvatar;
    @Bind(R.id.rl_avatar)
    RelativeLayout rlAvatar;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.rl_nickname)
    RelativeLayout rlNickname;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.rl_sex)
    RelativeLayout rlSex;
    @Bind(R.id.tv_location)
    TextView tvLocation;
    @Bind(R.id.rl_location)
    RelativeLayout rlLocation;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.rl_date)
    RelativeLayout rlDate;
    @Bind(R.id.tv_phone)
    TextView tvPhone;
    @Bind(R.id.rl_phone)
    RelativeLayout rlPhone;
    @Bind(R.id.tv_logout)
    TextView tvLogout;
    @Bind(R.id.sc_switch_compat)
    SwitchCompat scSwitchCompat;

    UpdateInfoPresenter mPresenter;
    String nickname, area, birthday, tel, avatar;
    boolean isMan;
    DateTimePicker dateTimePicker;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        setActionBarBack();

        initView();
    }

    private void initView() {
        mPresenter = new UpateInfoPresenterImpl(this);

        avatar = UserPreference.getAvatar();
        nickname = UserPreference.getNickname();
        area = UserPreference.getArea();
        birthday = UserPreference.getBirthday();
        tel = UserPreference.getUserTel();
        isMan = UserPreference.getIsMan();

        if (!TextUtils.isEmpty(avatar)) {
            ImageLoader.loadAvatar(UserInfoActivity.this, avatar, rivAvatar);
        }
        if (!TextUtils.isEmpty(nickname)) {
            tvNickname.setVisibility(View.VISIBLE);
            tvNickname.setText(nickname);
        }
        if (!TextUtils.isEmpty(area)) {
            tvLocation.setVisibility(View.VISIBLE);
            tvLocation.setText(area);
        }
        if (!TextUtils.isEmpty(birthday)) {
            tvDate.setVisibility(View.VISIBLE);
            tvDate.setText(birthday);
        }
        if (!TextUtils.isEmpty(tel)) {
            tvPhone.setVisibility(View.VISIBLE);
            tvPhone.setText(tel);
        }
        if (isMan) {
            scSwitchCompat.setChecked(true);
        } else {
            scSwitchCompat.setChecked(false);
        }
        scSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Map<String, String> map = new HashMap<>();
                map.put(UserApi.USERSEX, b ? getString(R.string.man) : getString(R.string.woman));
                mPresenter.upateUserInfo(map);
                UserPreference.setIsMan(b);
            }
        });

        rlAvatar.setOnClickListener(this);
        rlNickname.setOnClickListener(this);
        rlSex.setOnClickListener(this);
        rlLocation.setOnClickListener(this);
        rlDate.setOnClickListener(this);
        rlPhone.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
    }

    @Override
    protected void uploadPhoto(String photoPath) {
        super.uploadPhoto(photoPath);
        ImageLoader.getPicasso(UserInfoActivity.this).load(new File(photoPath)).fit().into(rivAvatar);
    }

    private void showDatePickDialog() {
        if (dateTimePicker == null) {
            long years50 = 50L * 365 * 1000 * 60 * 60 * 24L;
            int[] startValue = DateUtils.getDateTimeIntegerValue(System.currentTimeMillis() - years50);
            int[] endValue = DateUtils.getDateTimeIntegerValue(System.currentTimeMillis());

            dateTimePicker = new DateTimePicker(UserInfoActivity.this, DateTimePicker.YEAR_MONTH_DAY, DateTimePicker.NONE);
            dateTimePicker.setDateRangeStart(startValue[0], startValue[1], startValue[2]);
            dateTimePicker.setDateRangeEnd(endValue[0], endValue[1], endValue[2]);
            dateTimePicker.setWeightEnable(true);
            dateTimePicker.setCanLoop(true);
            dateTimePicker.setWheelModeEnable(true);
            dateTimePicker.setTitleText(getString(R.string.date));
            dateTimePicker.setSelectedTextColor(getResources().getColor(R.color.woman));
            dateTimePicker.setLineColor(getResources().getColor(R.color.man));
            dateTimePicker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
                @Override
                public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                    String date = year + "-" + month + "-" + day;
                    tvDate.setText(date);
                    Map<String, String> map = new HashMap<>();
                    map.put(UserApi.BIRTHDAY, date);
                    mPresenter.upateUserInfo(map);
                    UserPreference.setBirthday(date);
                }
            });
        }
        dateTimePicker.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_avatar:
                showPhotoPickDialog();
                break;
            case R.id.rl_nickname:
                Bundle bundleNickName = new Bundle();
                bundleNickName.putString(UserInfoSettingsActivity.CONTENT, nickname);
                bundleNickName.putInt(UserInfoSettingsActivity.TYPE, UserInfoSettingsActivity.INPUT_NICKNAME);
                jumpForResult(Constants.REQUEST_UPATE_NICKNAME, UserInfoSettingsActivity.class, bundleNickName);
                break;
            case R.id.rl_location:
                Bundle bundleAddress = new Bundle();
                bundleAddress.putString(UserInfoSettingsActivity.CONTENT, area);
                bundleAddress.putInt(UserInfoSettingsActivity.TYPE, UserInfoSettingsActivity.INPUT_ADDRESS);
                jumpForResult(Constants.REQUEST_UPATE_ADDRESS, UserInfoSettingsActivity.class, bundleAddress);
                break;
            case R.id.rl_date:
                showDatePickDialog();
                break;
            case R.id.tv_logout:
                UserPreference.clear();
                goBack();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == Constants.ACTIVITY_RESULT_SUCCESS) {
            String resultData = data.getStringExtra("data");
            switch (requestCode) {
                case Constants.REQUEST_UPATE_NICKNAME:
                    nickname = resultData;
                    tvNickname.setText(resultData);
                    tvNickname.setVisibility(View.VISIBLE);
                    UserPreference.setNickname(resultData);

                    Map<String, String> nickname = new HashMap<>();
                    nickname.put(UserApi.NICKMAME, resultData);
                    mPresenter.upateUserInfo(nickname);
                    break;
                case Constants.REQUEST_UPATE_ADDRESS:
                    area = resultData;
                    tvLocation.setText(resultData);
                    tvLocation.setVisibility(View.VISIBLE);
                    UserPreference.setArea(resultData);

                    Map<String, String> address = new HashMap<>();
                    address.put(UserApi.AREA, resultData);
                    mPresenter.upateUserInfo(address);
                    break;
            }
        }
    }

    @Override
    public void setError(int errorCode, String error, String key) {

    }

    class UpateInfoPresenterImpl extends BasePresenter<UserInfoActivity> implements UpdateInfoPresenter {

        public UpateInfoPresenterImpl(UserInfoActivity view) {
            super(view);
        }

        @Override
        public void upateUserInfo(Map<String, String> map) {
            requestData(RetrofitWapper.getInstance().getNetService(UserApi.class).updateInfo(map), UPDATE_USER_INFO);
        }

        @Override
        protected void onResult(Result result, String key) {
            switch (key) {
                case UPDATE_USER_INFO:

                    break;
            }
        }
    }
}
