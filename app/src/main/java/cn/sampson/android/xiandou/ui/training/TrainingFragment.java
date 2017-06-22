package cn.sampson.android.xiandou.ui.training;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.ui.BaseFragment;
import cn.sampson.android.xiandou.ui.haoyun.yunyu.taijiaoyinyue.MusicListActivity;

/**
 * Created by Administrator on 2017/6/5.
 */

public class TrainingFragment extends BaseFragment implements View.OnClickListener {

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_taijiao_yinyue, null);
        ButterKnife.bind(this, view);

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

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        int month = 0;
        switch (v.getId()) {
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
        ((BaseActivity) getActivity()).jumpTo(MusicListActivity.class, bundle);
    }
}
