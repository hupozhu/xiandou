package cn.sampson.android.xiandou.widget;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.sampson.android.xiandou.R;


/**
 * Created by Administrator on 2016/4/13.
 */
public class TabAdapter {

    private Context mContext;
    IFragmentsWrapper mWrapper;

    public TabAdapter(Context context, IFragmentsWrapper wrapper) {
        mWrapper = wrapper;
        mContext = context;
    }

    public int getCount() {
        return mWrapper.getCount();
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout._tab_item, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_title);
        tv.setText(mWrapper.getTitle(position));
        ImageView img = (ImageView) view.findViewById(R.id.iv_title);
        img.setImageResource(mWrapper.getTitleImg(position));
        return view;
    }

}
