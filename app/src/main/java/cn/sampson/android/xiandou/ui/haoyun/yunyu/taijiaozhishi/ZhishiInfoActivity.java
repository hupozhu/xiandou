package cn.sampson.android.xiandou.ui.haoyun.yunyu.taijiaozhishi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;

/**
 * Created by chengyang on 2017/6/21.
 */

public class ZhishiInfoActivity extends BaseActivity {

    public static final String TITLE = "title";
    public static final String CONTENT = "content";

    @Bind(R.id.tv_info)
    TextView tvInfo;

    String title;
    String content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setActionBarBack();

        setContentView(R.layout.activity_zhishi_info);
        ButterKnife.bind(this);

        title = getIntent().getStringExtra(TITLE);
        content = getIntent().getStringExtra(CONTENT);

        getSupportActionBar().setTitle(title);
        tvInfo.setText(content);
    }


}
