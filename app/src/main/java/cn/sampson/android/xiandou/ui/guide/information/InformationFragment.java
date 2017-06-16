package cn.sampson.android.xiandou.ui.guide.information;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.BaseFragment;

/**
 * Created by chengyang on 2017/6/14.
 */

public class InformationFragment extends BaseFragment {

    @Bind(R.id.tv_info)
    TextView tvInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, null);
        ButterKnife.bind(this, view);

        initView();
        return view;
    }

    private void initView() {
        String str = getArguments().getString("info");
        tvInfo.setText(str);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
