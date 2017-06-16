package cn.sampson.android.xiandou.ui.guide.information;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.guide.information.db.StroyDBHelper;
import cn.sampson.android.xiandou.ui.guide.twohundredeighty.domain.MyString;

/**
 * 孕期注意
 * Created by chengyang on 2017/6/14.
 */

public class StoryActivity extends DataContainerActivity {

    public static final String TITLE = "title";
    public static final String TYPE = "type";

    @Bind(R.id.rl_root)
    RelativeLayout rlRoot;

    private int type;
    private String title;

    View loadingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        ButterKnife.bind(this);

        title = getIntent().getStringExtra(TITLE);
        type = getIntent().getIntExtra(TYPE, 0);

        getSupportActionBar().setTitle(title);

        loadingView = LayoutInflater.from(StoryActivity.this).inflate(R.layout._loading_view, rlRoot, false);
        rlRoot.addView(loadingView);

        myHandler.post(new Runnable() {
            @Override
            public void run() {
                getData();
                myHandler.sendEmptyMessage(1);
            }
        });
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
    protected void initView() {
        if (loadingView != null) {
            rlRoot.removeView(loadingView);
            loadingView = null;
        }

        showFragment(new ListFragment());
    }
}
