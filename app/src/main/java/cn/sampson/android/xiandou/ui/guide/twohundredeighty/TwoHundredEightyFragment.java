package cn.sampson.android.xiandou.ui.guide.twohundredeighty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.BaseFragment;

/**
 * Created by chengyang on 2017/6/6.
 */

public class TwoHundredEightyFragment extends BaseFragment {

    public final static String TYPE = "type";
    @Bind(R.id.rv_days)
    RecyclerView rvDays;
    @Bind(R.id.web_info)
    WebView webInfo;

    private TwoHundredEightyActivity activity;
    private int index;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_read_everyday, null);
        ButterKnife.bind(this, view);
        activity = (TwoHundredEightyActivity) getActivity();
        initView();
        return view;
    }

    private void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvDays.setLayoutManager(manager);
//        rvDays.setAdapter();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
