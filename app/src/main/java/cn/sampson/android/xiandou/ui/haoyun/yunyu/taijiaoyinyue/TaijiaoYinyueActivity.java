package cn.sampson.android.xiandou.ui.haoyun.yunyu.taijiaoyinyue;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;

/**
 * Created by chengyang on 2017/6/21.
 */

public class TaijiaoYinyueActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_january)
    LinearLayout mLlJanuary;
    @Bind(R.id.ll_fabruary)
    LinearLayout mLlFabruary;
    @Bind(R.id.ll_march)
    LinearLayout mLlMarch;
    @Bind(R.id.ll_aril)
    LinearLayout mLlAril;
    @Bind(R.id.ll_may)
    LinearLayout mLlMay;
    @Bind(R.id.ll_june)
    LinearLayout mLlJune;
    @Bind(R.id.ll_july)
    LinearLayout mLlJuly;
    @Bind(R.id.ll_august)
    LinearLayout mLlAugust;
    @Bind(R.id.ll_september)
    LinearLayout mLlSeptember;
    @Bind(R.id.ll_october)
    LinearLayout mLlOctober;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setActionBarBack();

        setContentView(R.layout.activity_taijiao_yinyue);
        ButterKnife.bind(this);

        mLlJanuary.setOnClickListener(this);
        mLlFabruary.setOnClickListener(this);
        mLlMarch.setOnClickListener(this);
        mLlAril.setOnClickListener(this);
        mLlMay.setOnClickListener(this);
        mLlJune.setOnClickListener(this);
        mLlJuly.setOnClickListener(this);
        mLlAugust.setOnClickListener(this);
        mLlSeptember.setOnClickListener(this);
        mLlOctober.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int month = 0;
        switch (view.getId()) {
            case R.id.ll_january:
                month = 1;
                break;
            case R.id.ll_fabruary:
                month = 2;
                break;
            case R.id.ll_march:
                month = 3;
                break;
            case R.id.ll_aril:
                month = 4;
                break;
            case R.id.ll_may:
                month = 5;
                break;
            case R.id.ll_june:
                month = 6;
                break;
            case R.id.ll_july:
                month = 7;
                break;
            case R.id.ll_august:
                month = 8;
                break;
            case R.id.ll_september:
                month = 9;
                break;
            case R.id.ll_october:
                month = 10;
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(MusicListActivity.MONTH, month);
        jumpTo(MusicListActivity.class, bundle);
    }
}
