package cn.sampson.android.xiandou.widget.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.utils.UiUtils;
import cn.sampson.android.xiandou.utils.imageloader.ImageLoader;
import cn.sampson.android.xiandou.widget.banner.base.BaseIndicatorBanner;


public class MainPageImageBanner extends BaseIndicatorBanner<BannerItem, MainPageImageBanner> {

    private boolean isDragging;
    private float initX;
    private float intY;
    private int mActivePointerId;
    ImageView ivBanner;

    private static final int INVALID_POINTER = -1;

    public MainPageImageBanner(Context context) {
        this(context, null, 0);
    }

    public MainPageImageBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainPageImageBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onTitleSlect(TextView tv, int position) {

    }

    @Override
    public View onCreateItemView(int position) {
        View view = View.inflate(mContext, R.layout._adapter_main_page_image_banner, null);
        ivBanner = UiUtils.find(view, R.id.iv_banner);
        final BannerItem item = mDatas.get(position);

        ImageLoader.load(mContext, item.imgAddr, ivBanner);
        return view;
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                isDragging = false;
//                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
//
//                final float initialDownY = getMotionEventY(ev, mActivePointerId);
//                final float initialDownX = getMotionEventX(ev, mActivePointerId);
//
//                if (initialDownX == -1)
//                    return false;
//
//                initX = initialDownX;
//                intY = initialDownY;
//
//            case MotionEvent.ACTION_MOVE:
//
//                if (mActivePointerId == INVALID_POINTER) {
//                    return false;
//                }
//
//                final float y = getMotionEventY(ev, mActivePointerId);
//                final float x = getMotionEventX(ev, mActivePointerId);
//
//                if (x == -1) {
//                    return false;
//                }
//
//                float yDiff;
//                float xDiff;
//
//                xDiff = initX - x;
//                yDiff = intY - y;
//
//                if (Math.abs(yDiff) < Math.abs(xDiff)) {
//                    return false;
//                }
//                isDragging = true;
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                isDragging = false;
//                mActivePointerId = INVALID_POINTER;
//                break;
//        }
//
//        return isDragging;
//    }
//
//    private float getMotionEventY(MotionEvent ev, int activePointerId) {
//        final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
//        if (index < 0) {
//            return -1;
//        }
//        return MotionEventCompat.getY(ev, index);
//    }
//
//    private float getMotionEventX(MotionEvent ev, int activePointerId) {
//        final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
//        if (index < 0) {
//            return -1;
//        }
//        return MotionEventCompat.getX(ev, index);
//    }
}
