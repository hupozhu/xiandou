package cn.sampson.android.xiandou.ui.guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.config.Constants;
import cn.sampson.android.xiandou.ui.BaseFragment;
import cn.sampson.android.xiandou.ui.guide.attention.PregnancyAttentionActivity;
import cn.sampson.android.xiandou.ui.guide.fortyweeks.FortyWeeksActivity;
import cn.sampson.android.xiandou.ui.guide.information.InformationActivity;
import cn.sampson.android.xiandou.ui.guide.information.StoryActivity;
import cn.sampson.android.xiandou.ui.guide.twohundredeighty.TwoHundredEightyActivity;

/**
 * Created by Administrator on 2017/6/5.
 */

public class GuideFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.iv_banner)
    ImageView mIvBanner;
    @Bind(R.id.sizhizhou)
    LinearLayout mSizhizhou;
    @Bind(R.id.erbaibashitian)
    LinearLayout mErbaibashitian;
    @Bind(R.id.yunqizhuyi)
    LinearLayout mYunqizhuyi;
    @Bind(R.id.iv_taijiao1)
    ImageView mIvTaijiao1;
    @Bind(R.id.iv_taijiao2)
    ImageView mIvTaijiao2;
    @Bind(R.id.iv_taijiao3)
    ImageView mIvTaijiao3;
    @Bind(R.id.rl_yiqianyiye)
    RelativeLayout mRlYiqianyiye;
    @Bind(R.id.rl_antusheng)
    RelativeLayout mRlAntusheng;
    @Bind(R.id.rl_geling)
    RelativeLayout mRlGeling;
    @Bind(R.id.rl_ad_container)
    RelativeLayout mRlAdContainer;
    @Bind(R.id.ad_container)
    FrameLayout adContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, null);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        BannerView banner = new BannerView(getActivity(), ADSize.BANNER, Constants.APPID, Constants.BannerPosID);
        //设置广告轮播时间，为0或30~120之间的数字，单位为s,0标识不自动轮播
        banner.setRefresh(30);
        banner.setADListener(new AbstractBannerADListener() {

            @Override
            public void onNoAD(int arg0) {
                Log.i("AD_DEMO", "BannerNoAD，eCode=" + arg0);
            }

            @Override
            public void onADReceiv() {
                Log.i("AD_DEMO", "ONBannerReceive");
            }
        });
        adContainer.addView(banner);
        /* 发起广告请求，收到广告数据后会展示数据 */
        banner.loadAD();

        mSizhizhou.setOnClickListener(this);
        mErbaibashitian.setOnClickListener(this);
        mYunqizhuyi.setOnClickListener(this);
        mIvTaijiao1.setOnClickListener(this);
        mIvTaijiao2.setOnClickListener(this);
        mIvTaijiao3.setOnClickListener(this);
        mRlYiqianyiye.setOnClickListener(this);
        mRlAntusheng.setOnClickListener(this);
        mRlGeling.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_yiqianyiye:
                Intent intentYiqianyiye = new Intent(getActivity(), StoryActivity.class);
                intentYiqianyiye.putExtra(StoryActivity.TITLE, getString(R.string.yiqianyiye));
                intentYiqianyiye.putExtra(StoryActivity.TYPE, 3);
                getActivity().startActivity(intentYiqianyiye);
                break;

            case R.id.rl_antusheng:
                Intent intentAntusheng = new Intent(getActivity(), StoryActivity.class);
                intentAntusheng.putExtra(StoryActivity.TITLE, getString(R.string.antusheng));
                intentAntusheng.putExtra(StoryActivity.TYPE, 2);
                getActivity().startActivity(intentAntusheng);
                break;

            case R.id.rl_geling:
                Intent intentGeling = new Intent(getActivity(), StoryActivity.class);
                intentGeling.putExtra(StoryActivity.TITLE, getString(R.string.gelintonghua));
                intentGeling.putExtra(StoryActivity.TYPE, 1);
                getActivity().startActivity(intentGeling);
                break;

            case R.id.sizhizhou:
                Intent intent = new Intent(getActivity(), FortyWeeksActivity.class);
                getActivity().startActivity(intent);
                break;

            case R.id.erbaibashitian:
                Intent intent280 = new Intent(getActivity(), TwoHundredEightyActivity.class);
                getActivity().startActivity(intent280);
                break;

            case R.id.yunqizhuyi:
                Intent attention = new Intent(getActivity(), PregnancyAttentionActivity.class);
                getActivity().startActivity(attention);
                break;

            case R.id.iv_taijiao1:
                Intent taijiao1 = new Intent(getActivity(), InformationActivity.class);
                taijiao1.putExtra(InformationActivity.TITLE, getString(R.string.taijiao1));
                taijiao1.putExtra(InformationActivity.NAME, "taijiao1");
                getActivity().startActivity(taijiao1);
                break;

            case R.id.iv_taijiao2:
                Intent taijiao2 = new Intent(getActivity(), InformationActivity.class);
                taijiao2.putExtra(InformationActivity.TITLE, getString(R.string.taijiao2));
                taijiao2.putExtra(InformationActivity.NAME, "taijiao2");
                getActivity().startActivity(taijiao2);
                break;

            case R.id.iv_taijiao3:
                Intent taijiao3 = new Intent(getActivity(), InformationActivity.class);
                taijiao3.putExtra(InformationActivity.TITLE, getString(R.string.taijiao3));
                taijiao3.putExtra(InformationActivity.NAME, "taijiao3");
                getActivity().startActivity(taijiao3);
                break;
        }
    }
}
