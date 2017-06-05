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
import cn.sampson.android.xiandou.ui.BaseFragment;

/**
 * Created by Administrator on 2017/6/5.
 */

public class TrainingFragment extends BaseFragment {

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
        View view = inflater.inflate(R.layout.fragment_training, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
