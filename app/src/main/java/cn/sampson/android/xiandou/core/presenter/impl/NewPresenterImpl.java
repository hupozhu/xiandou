package cn.sampson.android.xiandou.core.presenter.impl;

import cn.sampson.android.xiandou.core.presenter.NewsPresenter;
import cn.sampson.android.xiandou.core.retroft.Api.NewsApi;
import cn.sampson.android.xiandou.core.retroft.RetrofitWapper;
import cn.sampson.android.xiandou.core.retroft.base.BasePresenter;
import cn.sampson.android.xiandou.core.retroft.base.Result;
import cn.sampson.android.xiandou.model.ListItem;
import cn.sampson.android.xiandou.ui.haoyun.INewsView;
import cn.sampson.android.xiandou.ui.haoyun.domain.ArticleItem;

/**
 * Created by chengyang on 2017/7/13.
 */

public class NewPresenterImpl extends BasePresenter<INewsView> implements NewsPresenter {

    public NewPresenterImpl(INewsView view) {
        super(view);
    }

    @Override
    public void getNewsByTag(String tag, int page, int limit) {
        requestData(RetrofitWapper.getInstance().getNetService(NewsApi.class).getNewsByTag(tag, page, limit), GET_NEWS_BY_TAG);
    }

    @Override
    protected void onResult(Result result, String key) {
        switch (key) {
            case GET_NEWS_BY_TAG:
                view.showNews((ListItem<ArticleItem>) result.data);
                break;
        }
    }
}
