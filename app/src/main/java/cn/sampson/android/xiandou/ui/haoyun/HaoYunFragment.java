package cn.sampson.android.xiandou.ui.haoyun;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.BaseFragment;
import cn.sampson.android.xiandou.ui.haoyun.yuer.YuerActivity;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.YunyuActivity;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.attention.PregnancyAttentionActivity;
import cn.sampson.android.xiandou.ui.haoyun.beiyun.BeiYunActivity;

/**
 * Created by Administrator on 2017/6/5.
 */

public class HaoYunFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.iv_banner)
    ImageView mIvBanner;
    @Bind(R.id.beiyun)
    LinearLayout mBeiyun;
    @Bind(R.id.yunyu)
    LinearLayout mYunyu;
    @Bind(R.id.yuer)
    LinearLayout mYuer;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mBeiyun.setOnClickListener(this);
        mYunyu.setOnClickListener(this);
        mYuer.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.beiyun:
                Intent beiyun = new Intent(getActivity(), BeiYunActivity.class);
                getActivity().startActivity(beiyun);
                break;

            case R.id.yunyu:
                Intent yunyu = new Intent(getActivity(), YunyuActivity.class);
                getActivity().startActivity(yunyu);
                break;

            case R.id.yuer:
                Intent attention = new Intent(getActivity(), YuerActivity.class);
                getActivity().startActivity(attention);
                break;
        }
    }
}
