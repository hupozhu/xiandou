package cn.sampson.android.xiandou.ui.haoyun.beiyun;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.BaseFragment;
import cn.sampson.android.xiandou.ui.haoyun.domain.QiyuanItem;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.QuickRecycleViewAdapter;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.ViewHelper;

/**
 * Created by Administrator on 2017/7/22 0022.
 */

public class QiyuanFragment extends BaseFragment {

    public static final String TYPE = "type";

    @Bind(R.id.list)
    RecyclerView list;

    QuickRecycleViewAdapter<QiyuanItem> mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_list, null);
        ButterKnife.bind(this, view);

        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new QuickRecycleViewAdapter<QiyuanItem>(R.layout.item_qiyuan, new ArrayList<QiyuanItem>(), list) {
            @Override
            protected void onBindData(Context context, int position, QiyuanItem item, int itemLayoutId, ViewHelper helper) {
                
            }
        };
        list.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
