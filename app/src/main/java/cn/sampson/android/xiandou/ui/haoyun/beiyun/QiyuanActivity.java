package cn.sampson.android.xiandou.ui.haoyun.beiyun;

import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;

/**
 * Created by chengyang on 2017/7/14.
 */

public class QiyuanActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setContentView(R.layout.activity_qiyuan);
        setActionBarBack();
    }
}
