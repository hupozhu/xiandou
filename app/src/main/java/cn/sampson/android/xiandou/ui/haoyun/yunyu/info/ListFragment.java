package cn.sampson.android.xiandou.ui.haoyun.yunyu.info;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.model.MyString;
import cn.sampson.android.xiandou.ui.BaseFragment;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.QuickRecycleViewAdapter;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.ViewHelper;

/**
 * Created by chengyang on 2017/6/14.
 */

public class ListFragment extends BaseFragment {

    public static final String TYPE = "type";

    @Bind(R.id.list)
    RecyclerView list;

    DataContainerActivity activity;

    QuickRecycleViewAdapter<MyString> mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_list, null);
        ButterKnife.bind(this, view);
        activity = (DataContainerActivity) getActivity();
        initView();
        return view;
    }

    private void initView() {
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new QuickRecycleViewAdapter<MyString>(R.layout.item_information, activity.getList()) {
            @Override
            protected void onBindData(Context context, final int position, MyString item, int itemLayoutId, ViewHelper helper) {
                helper.setText(R.id.name, (position + 1) + "." + item.mStrings);
                helper.setRootOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        activity.jumpToInfoPage(position);
                    }
                });
            }
        };
        list.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
