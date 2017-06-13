package cn.sampson.android.xiandou.ui.guide.twohundredeighty;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.BaseFragment;
import cn.sampson.android.xiandou.ui.guide.twohundredeighty.domain.MyString;
import cn.sampson.android.xiandou.utils.Tip;
import cn.sampson.android.xiandou.utils.widget.adapter.baseadapter.QuickRecycleViewAdapter;
import cn.sampson.android.xiandou.utils.widget.adapter.baseadapter.ViewHelper;

/**
 * Created by chengyang on 2017/6/6.
 */

public class TwoHundredEightyFragment extends BaseFragment {

    public final static String TYPE = "type";
    @Bind(R.id.rv_days)
    RecyclerView rvDays;
    @Bind(R.id.web_info)
    WebView webInfo;
    @Bind(R.id.web_container)
    FrameLayout webContainer;

    private TwoHundredEightyActivity activity;
    private int index;
    private QuickRecycleViewAdapter<MyString> mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_read_everyday, null);
        ButterKnife.bind(this, view);
        activity = (TwoHundredEightyActivity) getActivity();
        index = getArguments().getInt(TYPE);
        initView();
        return view;
    }

    private void initView() {
        ArrayList<MyString> datas = new ArrayList<>();
        switch (index) {
            case 0:
                for (int i = 1; i <= 90; i++) {
                    MyString mString = new MyString(i, "第" + i + "天");
                    datas.add(mString);
                }
                break;
            case 1:
                for (int i = 91; i <= 150; i++) {
                    MyString mString = new MyString(i, "第" + i + "天");
                    datas.add(mString);
                }
                break;
            case 2:
                for (int i = 151; i <= 280; i++) {
                    MyString mString = new MyString(i, "第" + i + "天");
                    datas.add(mString);
                }
                break;
        }

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvDays.setLayoutManager(manager);
        mAdapter = new QuickRecycleViewAdapter<MyString>(R.layout.item_read_days, datas) {
            @Override
            protected void onBindData(Context context, final int position, final MyString item, int itemLayoutId, ViewHelper helper) {
                helper.setText(R.id.tv_days, item.mStrings);
                if (getSelectHelper().getSelectedPosition() == position) {
                    helper.getView(R.id.tv_days).setSelected(true);
                    webInfo.loadDataWithBaseURL(null, activity.getInfos().get(item.id), "text/html", "utf-8", null);
                } else {
                    helper.getView(R.id.tv_days).setSelected(false);
                }
                helper.setRootOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getSelectHelper().setSelected(position);
                    }
                });
            }
        };
        rvDays.setClipToPadding(false);
        rvDays.setAdapter(mAdapter);
        mAdapter.getSelectHelper().setSelected(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webContainer.removeView(webInfo);
        webInfo.stopLoading();
        webInfo.getSettings().setJavaScriptEnabled(false);
        webInfo.clearHistory();
        webInfo.removeAllViews();
        webInfo.destroy();
    }
}
