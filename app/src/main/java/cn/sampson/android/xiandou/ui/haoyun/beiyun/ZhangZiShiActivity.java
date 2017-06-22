package cn.sampson.android.xiandou.ui.haoyun.beiyun;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.model.Posture;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.utils.ContextUtil;
import cn.sampson.android.xiandou.utils.FileUtils;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;

/**
 * Created by chengyang on 2017/6/20.
 */

public class ZhangZiShiActivity extends BaseActivity {

    @Bind(R.id.ll_tiwei1)
    LinearLayout llTiwei1;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.female_comfort_num)
    LinearLayout femaleComfortNum;
    @Bind(R.id.male_comfort_num)
    LinearLayout maleComfortNum;
    @Bind(R.id.describe)
    TextView describe;
    @Bind(R.id.ll_tiwei2)
    LinearLayout llTiwei2;
    @Bind(R.id.ll_tiwei3)
    LinearLayout llTiwei3;
    @Bind(R.id.root)
    FrameLayout root;

    private View currentSelectedView;
    private ArrayList<Posture> postures;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setActionBarBack();
        setContentView(R.layout.activity_zhang_zi_shi);
        ButterKnife.bind(this);
        parserData();
    }

    private void parserData() {
        showLoadingView(root);
        new Thread(new Runnable() {
            @Override
            public void run() {
                parserJsonData();
                sendHandlerMessage();
            }
        }).start();
    }

    private void parserJsonData() {
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Posture>>() {
            }.getType();
            String jsonStr = FileUtils.getJsonStringFromAssestFile(this, "zishi");
            postures = gson.fromJson(jsonStr, listType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDataComplete() {
        removeLoadingView(root);

        currentSelectedView = llTiwei1.getChildAt(0);
        currentSelectedView.setSelected(true);
        showPosture(0);

        for (int i = 0; i < llTiwei1.getChildCount(); i++) {
            onViewClick(llTiwei1.getChildAt(i), i);
        }
        for (int i = 0; i < llTiwei2.getChildCount(); i++) {
            onViewClick(llTiwei2.getChildAt(i), i + 4);
        }
        for (int i = 0; i < llTiwei3.getChildCount(); i++) {
            onViewClick(llTiwei3.getChildAt(i), i + 8);
        }
    }

    private void onViewClick(View view, final int index) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentSelectedView.setSelected(false);
                currentSelectedView = view;
                currentSelectedView.setSelected(true);
                showPosture(index);
            }
        });
    }

    private void showPosture(int index) {
        Posture posture = postures.get(index);

        int size = ContextUtil.dip2Px(20);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        params.rightMargin = ContextUtil.dip2Px(7);

        tvName.setText(getString(R.string.xueming, posture.name));
        femaleComfortNum.removeAllViews();
        for (int i = 0; i < posture.female_comfort_num; i++) {
            ImageView img = new ImageView(ZhangZiShiActivity.this);
            img.setLayoutParams(params);
            img.setImageResource(R.mipmap.ic_start);
            femaleComfortNum.addView(img);
        }
        maleComfortNum.removeAllViews();
        for (int i = 0; i < posture.male_comfort_num; i++) {
            ImageView img = new ImageView(ZhangZiShiActivity.this);
            img.setLayoutParams(params);
            img.setImageResource(R.mipmap.ic_start);
            maleComfortNum.addView(img);
        }
        describe.setText(posture.description);
    }
}
