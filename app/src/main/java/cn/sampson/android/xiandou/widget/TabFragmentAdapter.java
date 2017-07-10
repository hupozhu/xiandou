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
public class TabFragmentAdapter extends FragmentPagerAdapter {

    private Context mContext;
    IFragmentsWrapper mWrapper;

    public TabFragmentAdapter(Context context, FragmentManager fm, IFragmentsWrapper wrapper) {
        super(fm);
        mWrapper = wrapper;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mWrapper.getFragment(position);
    }

    @Override
    public int getCount() {
        return mWrapper.getCount();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //方法一：如果Title为单纯的文字，直接返回。

        //方法二：如果title为图片加文字，可以使用SpannableString加入图片作返回。

        //方法三：自定义title布局，此时getPageTitle应该返回null。然后在activity中调用tab.setCustomView方法，为每一个tab设置view。
        return null;
    }

    public View getTabView(int position){
        View view = LayoutInflater.from(mContext).inflate(R.layout._tab_item, null);
        TextView tv= (TextView) view.findViewById(R.id.tv_title);
        tv.setText(mWrapper.getTitle(position));
        ImageView img = (ImageView) view.findViewById(R.id.iv_title);
        img.setImageResource(mWrapper.getTitleImg(position));
        return view;
    }

}
