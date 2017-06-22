package cn.sampson.android.xiandou.ui.haoyun.yunyu.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;

import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.db.StroyDBHelper;
import cn.sampson.android.xiandou.model.MyString;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;

/**
 * 孕期注意
 */

public class StoryActivity extends DataContainerActivity {

    public static final String TITLE = "title";
    public static final String TYPE = "type";

    @Bind(R.id.rl_root)
    RelativeLayout rlRoot;

    private int type;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setActionBarBack();

        setContentView(R.layout.activity_information);
        ButterKnife.bind(this);

        title = getIntent().getStringExtra(TITLE);
        type = getIntent().getIntExtra(TYPE, 0);

        getSupportActionBar().setTitle(title);

        showLoadingView(rlRoot);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getData();
                sendHandlerMessage();
            }
        }).start();

    }

    private void getData() {
        StroyDBHelper helper = new StroyDBHelper(StoryActivity.this);
        Map<String, String> info = helper.getInfos(type);
        Set<String> keys = info.keySet();
        int i = 0;
        for (String key : keys) {
            MyString string = new MyString(i++, key);
            listItems.add(string);
            datas.add(info.get(key));
        }
    }

    @Override
    protected void onDataComplete() {
        removeLoadingView(rlRoot);
        showFragment(new ListFragment());
    }
}
