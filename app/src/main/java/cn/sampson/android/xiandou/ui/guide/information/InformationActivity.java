package cn.sampson.android.xiandou.ui.guide.information;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.guide.twohundredeighty.domain.MyString;
import cn.sampson.android.xiandou.utils.plist.PListUtils;
import cn.sampson.android.xiandou.utils.plist.PListXMLHandler;
import cn.sampson.android.xiandou.utils.plist.PListXMLParser;
import cn.sampson.android.xiandou.utils.plist.domain.Array;
import cn.sampson.android.xiandou.utils.plist.domain.Dict;
import cn.sampson.android.xiandou.utils.plist.domain.PList;
import cn.sampson.android.xiandou.utils.plist.domain.PListObject;

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

    View loadingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        ButterKnife.bind(this);

        name = getIntent().getStringExtra(NAME);
        title = getIntent().getStringExtra(TITLE);

        getSupportActionBar().setTitle(title);

        loadingView = LayoutInflater.from(InformationActivity.this).inflate(R.layout._loading_view, rlRoot, false);
        rlRoot.addView(loadingView);

        myHandler.post(new Runnable() {
            @Override
            public void run() {
                parserPlist();
                myHandler.sendEmptyMessage(1);
            }
        });
    }

    @Override
    protected void initView() {
        if (loadingView != null) {
            rlRoot.removeView(loadingView);
            loadingView = null;
        }
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
            parser.parse(PListUtils.getFiles(InformationActivity.this, name));
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
