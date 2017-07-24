package cn.sampson.android.xiandou.ui.community.domain;

import java.util.List;

import cn.sampson.android.xiandou.ui.mine.domain.User;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.SelectBase;

/**
 * Created by chengyang on 2017/7/12.
 */

public class PostsItem extends SelectBase {

    public long articleId;
    public String title;
    public long userId;
    public String cateTag;
    public String cover;
    public String content;
    public List<String> images;
    public int clickNum;
    public int commentNum;
    public int recommend;
    public String addTime;

    public User userinfo;
}
