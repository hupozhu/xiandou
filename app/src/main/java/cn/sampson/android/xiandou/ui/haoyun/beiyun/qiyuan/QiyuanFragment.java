package cn.sampson.android.xiandou.ui.haoyun.beiyun.qiyuan;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.core.presenter.QiyuanListPresenter;
import cn.sampson.android.xiandou.core.retroft.Api.UserApi;
import cn.sampson.android.xiandou.core.retroft.RetrofitWapper;
import cn.sampson.android.xiandou.core.retroft.base.BasePresenter;
import cn.sampson.android.xiandou.core.retroft.base.IView;
import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.model.ListItem;
import cn.sampson.android.xiandou.ui.BaseFragment;
import cn.sampson.android.xiandou.ui.haoyun.domain.QiyuanItem;
import cn.sampson.android.xiandou.utils.imageloader.ImageLoader;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.QuickRecycleViewAdapter;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.ViewHelper;

/**
 * Created by Administrator on 2017/7/22 0022.
 */

public class QiyuanFragment extends BaseFragment implements IView {

    public static final String TYPE = "type";

    @Bind(R.id.list)
    RecyclerView list;

    QuickRecycleViewAdapter<QiyuanItem> mAdapter;
    QiyuanActivity mActivity;
    QiyuanListPresenter mPresenter;

    int page = 1;
    int num = 10;
    int type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_list, null);
        ButterKnife.bind(this, view);

        type = getArguments().getInt(TYPE);

        mActivity = (QiyuanActivity) getActivity();
        mPresenter = new QiyuanListPresenterImpl(this);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new QuickRecycleViewAdapter<QiyuanItem>(R.layout.item_qiyuan, new ArrayList<QiyuanItem>(), list) {
            @Override
            protected void onBindData(Context context, int position, QiyuanItem item, int itemLayoutId, ViewHelper helper) {
                ImageLoader.loadAvatar(context, item.userinfo.userPic, (ImageView) helper.getView(R.id.riv_avatar));
                helper.setText(R.id.tv_content, item.content);
                helper.setText(R.id.tv_name, item.userinfo.nickname);
                helper.setText(R.id.tv_time, getString(R.string.qiyuan_time, item.created));
            }
        };
        mAdapter.setOnLoadMoreListener(new QuickRecycleViewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                mPresenter.getQiyaunList(String.valueOf(type), page, num);
            }
        });
        list.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void setError(int errorCode, String error, String key) {
        setList(null);
    }

    public void onRefresh() {
        page = 1;
        mPresenter.getQiyaunList(String.valueOf(type), page, num);
    }

    void onQiyuanListComplete(ListItem<QiyuanItem> datas) {
        if (datas != null && datas.total > 0) {
            setList(datas.lists);
        } else {
            setList(null);
        }
    }

    void setList(List<QiyuanItem> datas) {
        mActivity.onQiyuanListComplete();
        if (page == 1) {
            mAdapter.setRefresh(datas, num);
        } else {
            mAdapter.setLoaded(datas, num);
        }
    }

    class QiyuanListPresenterImpl extends BasePresenter<QiyuanFragment> implements QiyuanListPresenter {


        public QiyuanListPresenterImpl(QiyuanFragment view) {
            super(view);
        }

        @Override
        public void getQiyaunList(String type, int page, int num) {
            requestData(RetrofitWapper.getInstance().getNetService(UserApi.class).getWishes(type, page, num), GET_QIYUAN_LIST);
        }

        @Override
        protected void onResult(Result result, String key) {
            switch (key) {
                case GET_QIYUAN_LIST:
                    onQiyuanListComplete((ListItem<QiyuanItem>) result.data);
                    break;
            }
        }
    }
}
