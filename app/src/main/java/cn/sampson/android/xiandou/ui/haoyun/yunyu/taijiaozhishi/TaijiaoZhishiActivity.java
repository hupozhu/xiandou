package cn.sampson.android.xiandou.ui.haoyun.yunyu.taijiaozhishi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.model.MyString;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.utils.FileUtils;
import cn.sampson.android.xiandou.utils.plist.PListXMLHandler;
import cn.sampson.android.xiandou.utils.plist.PListXMLParser;
import cn.sampson.android.xiandou.utils.plist.domain.Array;
import cn.sampson.android.xiandou.utils.plist.domain.Dict;
import cn.sampson.android.xiandou.utils.plist.domain.PList;
import cn.sampson.android.xiandou.utils.plist.domain.PListObject;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;
import cn.sampson.android.xiandou.utils.widget.adapter.TabStateFragmentAdapter;

/**
 * Created by chengyang on 2017/6/21.
 */

public class TaijiaoZhishiActivity extends BaseActivity {

    private static final String ZAOQI = "taijiao1";
    private static final String ZHONGQI = "taijiao2";
    private static final String MOQI = "taijiao3";

    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.fl_root)
    FrameLayout flRoot;

    List<MyString> zaoqiList = new ArrayList<>();
    List<String> zaoqidata = new ArrayList<>();
    List<MyString> zhongqiList = new ArrayList<>();
    List<String> zhongqidata = new ArrayList<>();
    List<MyString> moqiList = new ArrayList<>();
    List<String> moqidata = new ArrayList<>();

    private boolean zaoqiComplete;
    private boolean zhongqiComplete;
    private boolean moqiComplete;

    private TabStateFragmentAdapter mPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setActionBarBack();

        setContentView(R.layout.activity_taijiao_zhishi);
        ButterKnife.bind(this);

        parserTaijiaoData();
    }

    private void parserTaijiaoData() {
        showLoadingView(flRoot);

        new Thread(new Runnable() {
            @Override
            public void run() {
                parserPlist(ZAOQI);
                zaoqiComplete = true;
                sendHandlerMessage();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                parserPlist(ZHONGQI);
                zhongqiComplete = true;
                sendHandlerMessage();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                parserPlist(MOQI);
                moqiComplete = true;
                sendHandlerMessage();
            }
        }).start();
    }

    @Override
    protected void onDataComplete() {
        if (zaoqiComplete && zhongqiComplete && moqiComplete) {
            initView();
            removeLoadingView(flRoot);
            zaoqiComplete = zhongqiComplete = moqiComplete = false;
        }
    }

    /**
     * 解析plist
     */
    private void parserPlist(String name) {
        PListXMLParser parser = new PListXMLParser(); // 基于SAX的实现
        PListXMLHandler handler = new PListXMLHandler();
        parser.setHandler(handler);
        try {
            // hsk.plist是你要解析的文件，该文件需放在assets文件夹下
            parser.parse(FileUtils.getFiles(TaijiaoZhishiActivity.this, name));
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
        PListObject rootElement = actualPList.getRootElement();
        try {
            Array array = (Array) rootElement;
            parseRootArray(name, array);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseRootArray(String name, Array array) throws IOException {
        switch (name) {
            case ZAOQI:
                for (int k = 0; k < array.size(); k++) {
                    Dict item = (Dict) array.get(k);
                    MyString myString = new MyString(k, item.getConfiguration("titlename").getValue());
                    zaoqiList.add(myString);
                    zaoqidata.add(new String(item.getConfiguration("titleinfo").getValue()));
                }
                break;
            case ZHONGQI:
                for (int k = 0; k < array.size(); k++) {
                    Dict item = (Dict) array.get(k);
                    MyString myString = new MyString(k, item.getConfiguration("titlename").getValue());
                    zhongqiList.add(myString);
                    zhongqidata.add(new String(item.getConfiguration("titleinfo").getValue()));
                }
                break;
            case MOQI:
                for (int k = 0; k < array.size(); k++) {
                    Dict item = (Dict) array.get(k);
                    MyString myString = new MyString(k, item.getConfiguration("titlename").getValue());
                    moqiList.add(myString);
                    moqidata.add(new String(item.getConfiguration("titleinfo").getValue()));
                }
                break;
        }
    }

    private void initView() {
        mPagerAdapter = new TabStateFragmentAdapter(getSupportFragmentManager(), new TaijiaoFragmentWapper());
        pager.setAdapter(mPagerAdapter);
        tabs.setupWithViewPager(pager);
    }

    public List<MyString> getList(int type) {
        switch (type) {
            case 0:
                return zaoqiList;
            case 1:
                return zhongqiList;
            case 2:
                return moqiList;
        }
        return null;
    }

    public String getContent(int type, int position) {
        switch (type) {
            case 0:
                return zaoqidata.get(position);
            case 1:
                return zhongqidata.get(position);
            case 2:
                return moqidata.get(position);
        }
        return null;
    }

}
