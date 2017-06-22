package cn.sampson.android.xiandou.ui.haoyun.yunyu.taijiaozhishi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.model.MyString;
import cn.sampson.android.xiandou.ui.BaseFragment;
import cn.sampson.android.xiandou.utils.widget.adapter.baseadapter.QuickRecycleViewAdapter;
import cn.sampson.android.xiandou.utils.widget.adapter.baseadapter.ViewHelper;

/**
 * Created by chengyang on 2017/6/21.
 */

public class TaijiaoListFragment extends BaseFragment {

    public static String TYPE = "TYPE";
    @Bind(R.id.list)
    RecyclerView list;

    int type;

    TaijiaoZhishiActivity mActivity;
    QuickRecycleViewAdapter<MyString> mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_list, null);
        mActivity = (TaijiaoZhishiActivity) getActivity();
        ButterKnife.bind(this, view);
        initView();
        return view;
    }


    private void initView() {
        type = getArguments().getInt(TYPE);

        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new QuickRecycleViewAdapter<MyString>(R.layout.item_information, mActivity.getList(type)) {
            @Override
            protected void onBindData(Context context, final int position, final MyString item, int itemLayoutId, ViewHelper helper) {
                helper.setText(R.id.name, (position + 1) + "." + item.mStrings);
                helper.setRootOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString(ZhishiInfoActivity.TITLE, item.mStrings);
                        bundle.putString(ZhishiInfoActivity.CONTENT, mActivity.getContent(type, position));
                        mActivity.jumpTo(ZhishiInfoActivity.class, bundle);
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
