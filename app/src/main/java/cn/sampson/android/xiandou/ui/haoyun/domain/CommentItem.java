package cn.sampson.android.xiandou.ui.haoyun.domain;

import cn.sampson.android.xiandou.ui.mine.domain.User;
import cn.sampson.android.xiandou.widget.adapter.baseadapter.SelectBase;

/**
 * Created by chengyang on 2017/7/20.
 */

public class CommentItem extends SelectBase {

    public long commentId;
    public long pid;
    public long userId;
    public long reUserId;
    public String content;
    public long articleId;
    public String created;
    public String updated;
    public int status;
    public User userinfo;
    public CommentItem fathercomment;

}
