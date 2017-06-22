package cn.sampson.android.xiandou.ui.haoyun.yunyu.attention;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.info.InformationActivity;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;

/**
 * Created by chengyang on 2017/6/14.
 */

public class PregnancyAttentionActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_yunqizhuyi)
    LinearLayout llYunqizhuyi;
    @Bind(R.id.ll_renshengfanying)
    LinearLayout llRenshengfanying;
    @Bind(R.id.ll_yunqijiancha)
    LinearLayout llYunqijiancha;
    @Bind(R.id.ll_yunqixingshenghuo)
    LinearLayout llYunqixingshenghuo;
    @Bind(R.id.ll_yunqijibin)
    LinearLayout llYunqijibin;
    @Bind(R.id.ll_yunqiyinyang)
    LinearLayout llYunqiyinyang;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setActionBarBack();

        setContentView(R.layout.activity_pregnancy_attention);
        ButterKnife.bind(this);

        setListener();
    }

    private void setListener() {
        llYunqizhuyi.setOnClickListener(this);
        llRenshengfanying.setOnClickListener(this);
        llYunqijiancha.setOnClickListener(this);
        llYunqixingshenghuo.setOnClickListener(this);
        llYunqijibin.setOnClickListener(this);
        llYunqiyinyang.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.ll_yunqizhuyi:
                bundle.putString(InformationActivity.NAME, "gongzuoyunmama");
                bundle.putString(InformationActivity.TITLE, getString(R.string.pregnancy_work));
                break;
            case R.id.ll_renshengfanying:
                bundle.putString(InformationActivity.NAME, "renshengfanying");
                bundle.putString(InformationActivity.TITLE, getString(R.string.renchenfanying));
                break;
            case R.id.ll_yunqijiancha:
                bundle.putString(InformationActivity.NAME, "yunqijiancha");
                bundle.putString(InformationActivity.TITLE, getString(R.string.yunqijiancha));
                break;
            case R.id.ll_yunqixingshenghuo:
                bundle.putString(InformationActivity.NAME, "yunqishenghuo");
                bundle.putString(InformationActivity.TITLE, getString(R.string.yunqixingshenghuo));
                break;
            case R.id.ll_yunqijibin:
                bundle.putString(InformationActivity.NAME, "yunqijibin");
                bundle.putString(InformationActivity.TITLE, getString(R.string.yunqijibing));
                break;
            case R.id.ll_yunqiyinyang:
                bundle.putString(InformationActivity.NAME, "yunqiyinyang");
                bundle.putString(InformationActivity.TITLE, getString(R.string.yunqiyingyang));
                break;
        }
        jumpTo(InformationActivity.class, bundle);
    }
}
