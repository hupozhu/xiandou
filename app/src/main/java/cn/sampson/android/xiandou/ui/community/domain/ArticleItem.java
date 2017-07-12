package cn.sampson.android.xiandou.ui.community.domain;

import cn.sampson.android.xiandou.ui.mine.domain.User;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.SelectBase;

/**
 * Created by chengyang on 2017/7/12.
 */

public class ArticleItem extends SelectBase {

    public long articleId;
    public String title;
    public long userId;
    public String cateTag;
    public String cover;
    public String content;
//    public String images;
    public int clickNum;
    public int commentNum;
    public int recommend;
    public String addTime;

    public User userinfo;
}
