package cn.sampson.android.xiandou.ui.guide.twohundredeighty.domain;

import cn.sampson.android.xiandou.utils.widget.adapter.baseadapter.SelectBase;

/**
 * Created by chengyang on 2017/6/10.
 */

public class MyString extends SelectBase {

    public String mStrings;
    public int id;

    public MyString(int id, String s) {
        this.id = id;
        mStrings = s;
    }

}
