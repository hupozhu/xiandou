package cn.sampson.android.xiandou.ui.guide.fortyweeks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.BaseFragment;
import cn.sampson.android.xiandou.ui.guide.fortyweeks.doman.PregnancyWeek;
import cn.sampson.android.xiandou.utils.Tip;
import cn.sampson.android.xiandou.utils.imageloader.ImageLoader;
import cn.sampson.android.xiandou.utils.plist.domain.Array;
import cn.sampson.android.xiandou.utils.plist.domain.Dict;
import cn.sampson.android.xiandou.utils.plist.domain.PListObject;
import okhttp3.internal.Util;

/**
 * Created by chengyang on 2017/6/6.
 */

public class PregnancyWeeksFragment extends BaseFragment {

    public final static String TYPE = "type";
    @Bind(R.id.ll_container)
    LinearLayout llContainer;

    FortyWeeksActivity activity;

    private int index;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_content_page, null);
        ButterKnife.bind(this, view);
        activity = (FortyWeeksActivity) getActivity();
        initView();
        return view;
    }

    private void initView() {
        index = getArguments().getInt(TYPE);

        PregnancyWeek mPreWeek = activity.getPregWeeks().get(index);
        Dict yinyangDict = (Dict) activity.getmYinygang().get(mPreWeek.weeknumber);

        for (int i = 0; i < mPreWeek.nametype.size(); i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.common_title_content, llContainer, false);
            TextView tvTitle = (TextView) view.findViewById(R.id.title);
            TextView tvContent = (TextView) view.findViewById(R.id.content);

            String title = mPreWeek.nametype.get(i);
            tvTitle.setText(title);
            if (i == 0) {//简介
                tvContent.setText(mPreWeek.firstinfo.contentofdev);
            } else if (i == 1) {//概述
                //示例图
                View viewImage = LayoutInflater.from(getContext()).inflate(R.layout.common_title_image, llContainer, false);
                TextView imageTitle = (TextView) viewImage.findViewById(R.id.title);
                ImageView imageContent = (ImageView) viewImage.findViewById(R.id.content);
                imageTitle.setText(getString(R.string.example_image));
                String imageResourceName = mPreWeek.firstinfo.imagedev.split("\\.")[0];
                imageContent.setImageResource(getResources().getIdentifier(imageResourceName, "mipmap", getContext().getPackageName()));
                llContainer.addView(viewImage);

                tvContent.setText(mPreWeek.overview);
            } else {
                PListObject pListObject = yinyangDict.getConfigMap().get(title);

                if (pListObject instanceof cn.sampson.android.xiandou.utils.plist.domain.String) {
                    tvContent.setText(((cn.sampson.android.xiandou.utils.plist.domain.String) pListObject).getValue());
                }
                if (pListObject instanceof Dict) {
                    tvContent.setVisibility(View.GONE);
                    View yinshit = LayoutInflater.from(getContext()).inflate(R.layout.layout_yinyang_tuijian, null);
                    TextView tuijian = (TextView) yinshit.findViewById(R.id.tv_shipintuijian);
                    TextView jianjie = (TextView) yinshit.findViewById(R.id.tv_shipinjianjie);
                    TextView zuofa = (TextView) yinshit.findViewById(R.id.tv_zuofa);
                    TextView cailiao = (TextView) yinshit.findViewById(R.id.tv_cailiao);
                    Map<String, PListObject> notes = ((Dict) pListObject).getConfigMap();
                    tuijian.setText(getString(R.string.shipin_tuijian, ((cn.sampson.android.xiandou.utils.plist.domain.String) notes.get("vegetablesname")).getValue()));
                    jianjie.setText(((cn.sampson.android.xiandou.utils.plist.domain.String) notes.get("cateringarrangements")).getValue());

                    Array array = (Array) notes.get("vegetablesinfo");
                    cailiao.setText((((Dict) array.get(0)).getConfiguration("titleinfo")).getValue());
                    zuofa.setText((((Dict) array.get(1)).getConfiguration("titleinfo")).getValue());
                    ((ViewGroup) view.getRootView()).addView(yinshit);
                }
            }

            llContainer.addView(view);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
