package cn.sampson.android.xiandou.ui.haoyun.yunyu.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.model.MyString;
import cn.sampson.android.xiandou.utils.FileUtils;
import cn.sampson.android.xiandou.utils.plist.PListXMLHandler;
import cn.sampson.android.xiandou.utils.plist.PListXMLParser;
import cn.sampson.android.xiandou.utils.plist.domain.Array;
import cn.sampson.android.xiandou.utils.plist.domain.Dict;
import cn.sampson.android.xiandou.utils.plist.domain.PList;
import cn.sampson.android.xiandou.utils.plist.domain.PListObject;
import cn.sampson.android.xiandou.utils.systembar.StatusBarUtil;

/**
 * 孕期注意
 * Created by chengyang on 2017/6/14.
 */

public class InformationActivity extends DataContainerActivity {

    public static final String NAME = "name";
    public static final String TITLE = "title";

    @Bind(R.id.rl_root)
    RelativeLayout rlRoot;

    private String name;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.StatusBarLightMode(this);
        setActionBarBack();

        setContentView(R.layout.activity_information);
        ButterKnife.bind(this);

        name = getIntent().getStringExtra(NAME);
        title = getIntent().getStringExtra(TITLE);

        getSupportActionBar().setTitle(title);
        showLoadingView(rlRoot);
        new Thread(new Runnable() {
            @Override
            public void run() {
                parserPlist();
                sendHandlerMessage();
            }
        }).start();
    }

    @Override
    protected void onDataComplete() {
        removeLoadingView(rlRoot);
        showFragment(new ListFragment());
    }

    /**
     * 解析plist
     */
    private void parserPlist() {
        PListXMLParser parser = new PListXMLParser(); // 基于SAX的实现
        PListXMLHandler handler = new PListXMLHandler();
        parser.setHandler(handler);
        try {
            // hsk.plist是你要解析的文件，该文件需放在assets文件夹下
            parser.parse(FileUtils.getFiles(InformationActivity.this, name));
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        PList actualPList = ((PListXMLHandler) parser.getHandler()).getPlist();
        PListObject rootElement = actualPList.getRootElement();
        try {
            Array array = (Array) rootElement;
            parseRootArray(array);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseRootArray(Array array) throws IOException {
        for (int k = 0; k < array.size(); k++) {
            Dict item = (Dict) array.get(k);
            MyString myString = new MyString(k, item.getConfiguration("titlename").getValue());
            listItems.add(myString);
            datas.add(new String(item.getConfiguration("titleinfo").getValue()));
        }
    }


}
