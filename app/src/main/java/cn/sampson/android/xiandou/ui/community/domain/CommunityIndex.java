package cn.sampson.android.xiandou.ui.community.domain;

import cn.sampson.android.xiandou.model.ListItem;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.SelectBase;
import cn.sampson.android.xiandou.widget.banner.BannerItem;

/**
 * Created by chengyang on 2017/7/12.
 */

public class CommunityIndex extends SelectBase {

    public ListItem<BannerItem> banners;
    public ListItem<CommunityCategory> cateList;
    public ListItem<ArticleItem> hots;
    public ListItem<ArticleItem> news;

}
