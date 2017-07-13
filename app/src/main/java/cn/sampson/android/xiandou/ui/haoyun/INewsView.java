package cn.sampson.android.xiandou.ui.haoyun;

import cn.sampson.android.xiandou.core.retroft.base.IView;
import cn.sampson.android.xiandou.model.ListItem;
import cn.sampson.android.xiandou.ui.haoyun.domain.ArticleItem;

/**
 * Created by chengyang on 2017/7/13.
 */

public interface INewsView extends IView {

    void showNews(ListItem<ArticleItem> list);

}
