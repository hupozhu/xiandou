package cn.sampson.android.xiandou.ui.community.domain;

import cn.sampson.android.xiandou.ui.mine.domain.User;

/**
 * Created by chengyang on 2017/7/20.
 */

public class PostsDetail {

    public long articleId;
    public String title;
    public long userId;
    public String cateTag;
    public String cover;
    public String content;
    public String images;
    public int clickNum;
    public int commentNum;
    public int recommend;
    public String created;
    public User userinfo;
    public int isCollect;
}
