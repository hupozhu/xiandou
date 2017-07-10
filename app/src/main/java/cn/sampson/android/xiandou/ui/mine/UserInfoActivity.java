package cn.sampson.android.xiandou.ui.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.core.persistence.UserPreference;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.widget.dialog.PhotoPickDialog;

/**
 * Created by chengyang on 2017/7/7.
 */

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        setActionBarBack();

        initView();
    }

    private void initView() {
        rlAvatar.setOnClickListener(this);
        rlNickname.setOnClickListener(this);
        rlSex.setOnClickListener(this);
        rlLocation.setOnClickListener(this);
        rlDate.setOnClickListener(this);
        rlPhone.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
    }

    /**
     * 点击头像弹出选择头像对话框，拍照 或者 从相册选择
     */
    protected void showPhotoPickDialog() {
        PhotoPickDialog dialog = new PhotoPickDialog(BaseActivity.this);
        dialog.setClickListener(new PhotoPickDialog.PhotoPickListener() {
            @Override
            public void fromCamera() {
                openCamera();
            }

            @Override
            public void fromAlbum() {
                openAlbum("get_photo");
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_avatar:

                break;
            case R.id.rl_nickname:

                break;
            case R.id.rl_sex:

                break;
            case R.id.rl_location:

                break;
            case R.id.rl_date:

                break;
            case R.id.rl_phone:

                break;
            case R.id.tv_logout:
                UserPreference.clear();
                goBack();
                break;
        }
    }
}
