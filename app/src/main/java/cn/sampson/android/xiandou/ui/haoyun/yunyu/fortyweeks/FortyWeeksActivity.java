package cn.sampson.android.xiandou.ui.haoyun.yunyu.fortyweeks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.BaseActivity;
import cn.sampson.android.xiandou.model.PregnancyWeek;
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
 * 怀孕40周
 * Created by Administrator on 2017/6/5.
 */

public class FortyWeeksActivity extends BaseActivity {

    private static final String TAG = "FortyWeeksActivity";

    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.fl_root)
    FrameLayout flRoot;

    private TabStateFragmentAdapter mPagerAdapter;

    private ArrayList<PregnancyWeek> mPregWeeks = new ArrayList<>();
    private Map<String, PListObject> mYinygang = new HashMap<>();

    private boolean huaiyunComplete, yinyangComplete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setActionBarBack();

        setContentView(R.layout.activity_forty_weeks);
        ButterKnife.bind(this);

        asynParserData();
    }

    @Override
    protected void onDataComplete() {
        if (huaiyunComplete && yinyangComplete) {
            removeLoadingView(flRoot);

            mPagerAdapter = new TabStateFragmentAdapter(getSupportFragmentManager(), new PregnancyWeeksFragmentWapper());
            pager.setAdapter(mPagerAdapter);
            tabs.setupWithViewPager(pager);
        }
    }

    public ArrayList<PregnancyWeek> getPregWeeks() {
        return mPregWeeks;
    }

    public Map<String, PListObject> getmYinygang() {
        return mYinygang;
    }

    private void asynParserData() {
        showLoadingView(flRoot);

        new Thread(new Runnable() {
            @Override
            public void run() {
                parserHuaiyunPlist();
                sendHandlerMessage();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                parserYingyangTuijianPlist();
                sendHandlerMessage();
            }
        }).start();
    }

    /**
     * 解析怀孕40天的plist
     */
    private void parserHuaiyunPlist() {
        PListXMLParser parser = new PListXMLParser(); // 基于SAX的实现
        PListXMLHandler handler = new PListXMLHandler();
        parser.setHandler(handler);
        try {
            parser.parse(FileUtils.getFiles(this, "huaiyun40"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
        PListObject rootElement = actualPList.getRootElement();
        try {
            parsePListRoot(rootElement);
            FortyWeeksActivity.this.huaiyunComplete = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析营养推荐的plist
     */
    private void parserYingyangTuijianPlist() {
        PListXMLParser parser = new PListXMLParser(); // 基于SAX的实现
        PListXMLHandler handler = new PListXMLHandler();
        parser.setHandler(handler);
        try {
            parser.parse(FileUtils.getFiles(this, "yingyangtuijian"));
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
        PListObject rootElement = actualPList.getRootElement();
        try {
            parseYinyangPListRoot(rootElement);
            FortyWeeksActivity.this.yinyangComplete = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseYinyangPListRoot(PListObject pListObject) throws IOException {
        mYinygang = ((Dict) pListObject).getConfigMap();
    }

    private void parsePListRoot(PListObject pListObject) throws IOException {
        Array array = (Array) pListObject;
        parseRootArray(array);

    }

    private void parseRootArray(Array noteArray) throws IOException {
        for (int k = 0; k < noteArray.size(); k++) {
            PListObject pListObject = noteArray.get(k);
            parseItemPListObject(pListObject);
        }
    }

    private void parseItemPListObject(PListObject pListObject) throws IOException {
        Dict dict = (Dict) pListObject;
        parseItemDict(dict);
    }

    private void parseItemDict(Dict dict) throws IOException {
        PregnancyWeek currentPreWeek = new PregnancyWeek();

        Map<String, PListObject> notes = dict.getConfigMap();
        //firstInfo
        Map<String, PListObject> firstInfo = ((Dict) notes.get("firstinfo")).getConfigMap();
        currentPreWeek.firstinfo = new PregnancyWeek.FirstInfo();
        currentPreWeek.firstinfo.imagedev = parseString(firstInfo.get("imagedev"));
        currentPreWeek.firstinfo.contentofdev = parseString(firstInfo.get("contentofdev"));
        //weeknumber
        currentPreWeek.weeknumber = parseString(notes.get("weeknumber"));
        //nametype
        currentPreWeek.nametype = new ArrayList<>();
        Array noteArray = (Array) notes.get("nametype");
        for (int i = 0; i < noteArray.size(); i++) {
            currentPreWeek.nametype.add(parseString(noteArray.get(i)));
        }
        //overview
        currentPreWeek.overview = parseString(notes.get("overview"));
        mPregWeeks.add(currentPreWeek);
    }

    private String parseString(PListObject pListObject) {
        return ((cn.sampson.android.xiandou.utils.plist.domain.String) pListObject).getValue();
    }

}
