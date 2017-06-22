package cn.sampson.android.xiandou.ui.haoyun.yunyu.info;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.config.Constants;
import cn.sampson.android.xiandou.ui.BaseFragment;
import cn.sampson.android.xiandou.model.MyString;
import cn.sampson.android.xiandou.utils.widget.adapter.baseadapter.QuickRecycleViewAdapter;
import cn.sampson.android.xiandou.utils.widget.adapter.baseadapter.ViewHelper;

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

        BannerView banner = new BannerView(getActivity(), ADSize.BANNER, Constants.APPID, Constants.BannerPosID);
        //设置广告轮播时间，为0或30~120之间的数字，单位为s,0标识不自动轮播
        banner.setRefresh(30);
        banner.setADListener(new AbstractBannerADListener() {

            @Override
            public void onNoAD(int arg0) {
                Log.i("AD_DEMO", "BannerNoAD，eCode=" + arg0);
            }

            @Override
            public void onADReceiv() {
                Log.i("AD_DEMO", "ONBannerReceive");
            }
        });
        mAdapter.addHeaderView(banner);
        /* 发起广告请求，收到广告数据后会展示数据 */
        banner.loadAD();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
