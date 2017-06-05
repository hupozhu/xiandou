package cn.sampson.android.xiandou.ui.guide;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.BaseFragment;

/**
 * Created by Administrator on 2017/6/5.
 */

public class GuideFragment extends BaseFragment {

    @Bind(R.id.iv_banner)
    ImageView mIvBanner;
    @Bind(R.id.sizhizhou)
    LinearLayout mSizhizhou;
    @Bind(R.id.erbaibashitian)
    LinearLayout mErbaibashitian;
    @Bind(R.id.yunqizhuyi)
    LinearLayout mYunqizhuyi;
    @Bind(R.id.iv_taijiao1)
    ImageView mIvTaijiao1;
    @Bind(R.id.iv_taijiao2)
    ImageView mIvTaijiao2;
    @Bind(R.id.iv_taijiao3)
    ImageView mIvTaijiao3;
    @Bind(R.id.rl_yiqianyiye)
    RelativeLayout mRlYiqianyiye;
    @Bind(R.id.rl_antusheng)
    RelativeLayout mRlAntusheng;
    @Bind(R.id.rl_geling)
    RelativeLayout mRlGeling;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
