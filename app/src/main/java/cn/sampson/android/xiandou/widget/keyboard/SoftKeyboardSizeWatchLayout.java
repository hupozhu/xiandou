package cn.sampson.android.xiandou.widget.keyboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 检测键盘状态的基础类，通过监听视图树的改变，来判断键盘是否弹起
 */
public class SoftKeyboardSizeWatchLayout extends RelativeLayout {
    private Context mContext;
    private boolean mIsSoftKeyboardPop = false;
    private int mScreenHeight = 0;
    private int mOldh = -1;
    private int mNowh = -1;
    private List<OnResizeListener> mListenerList;

    public SoftKeyboardSizeWatchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        //监听布局树的改变
        this.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                Rect r = new Rect();
                //获取页面可视窗口矩形（除去键盘外的范围）
                ((Activity)SoftKeyboardSizeWatchLayout.this.mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(r);

                //如果ScreenHeight还未初始化，将其初始化为屏幕高度
                if(SoftKeyboardSizeWatchLayout.this.mScreenHeight == 0) {
                    SoftKeyboardSizeWatchLayout.this.mScreenHeight = r.bottom;
                }

                //键盘状态改变时，获取到键盘的高度
                SoftKeyboardSizeWatchLayout.this.mNowh = SoftKeyboardSizeWatchLayout.this.mScreenHeight - r.bottom;

                //如果高度改变，且原来高度不为初始值
                if(SoftKeyboardSizeWatchLayout.this.mOldh != -1 && SoftKeyboardSizeWatchLayout.this.mNowh != SoftKeyboardSizeWatchLayout.this.mOldh) {
                    Iterator i$;
                    SoftKeyboardSizeWatchLayout.OnResizeListener l;
                    //键盘弹起
                    if(SoftKeyboardSizeWatchLayout.this.mNowh > 0) {
                        //改变键盘是否弹起状态
                        SoftKeyboardSizeWatchLayout.this.mIsSoftKeyboardPop = true;
                        //如果如果设置有监听，则回掉
                        if(SoftKeyboardSizeWatchLayout.this.mListenerList != null) {
                            i$ = SoftKeyboardSizeWatchLayout.this.mListenerList.iterator();

                            while(i$.hasNext()) {
                                l = (SoftKeyboardSizeWatchLayout.OnResizeListener)i$.next();
                                l.OnSoftPop(SoftKeyboardSizeWatchLayout.this.mNowh);
                            }
                        }
                    } else {//键盘收起，监听的回掉
                        SoftKeyboardSizeWatchLayout.this.mIsSoftKeyboardPop = false;
                        if(SoftKeyboardSizeWatchLayout.this.mListenerList != null) {
                            i$ = SoftKeyboardSizeWatchLayout.this.mListenerList.iterator();

                            while(i$.hasNext()) {
                                l = (SoftKeyboardSizeWatchLayout.OnResizeListener)i$.next();
                                l.OnSoftClose();
                            }
                        }
                    }
                }

                SoftKeyboardSizeWatchLayout.this.mOldh = SoftKeyboardSizeWatchLayout.this.mNowh;
            }
        });
    }

    public boolean isSoftKeyboardPop() {
        return this.mIsSoftKeyboardPop;
    }

    public void addOnResizeListener(SoftKeyboardSizeWatchLayout.OnResizeListener l) {
        if(this.mListenerList == null) {
            this.mListenerList = new ArrayList();
        }

        this.mListenerList.add(l);
    }

    public interface OnResizeListener {
        void OnSoftPop(int var1);

        void OnSoftClose();
    }
}
