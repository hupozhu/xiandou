package cn.sampson.android.xiandou.widget.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import cn.sampson.android.xiandou.R;

/**
 * 高度可变布局
 */
public abstract class AutoHeightLayout extends SoftKeyboardSizeWatchLayout implements SoftKeyboardSizeWatchLayout.OnResizeListener {
    private static final int ID_CHILD;
    protected int mSoftKeyboardHeight;
    protected int mMaxParentHeight;
    protected Context mContext;
    private AutoHeightLayout.OnMaxParentHeightChangeListener maxParentHeightChangeListener;

    public AutoHeightLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        //从系统参数中读取键盘的默认高度
        this.mSoftKeyboardHeight = KeyboardUtils.getDefKeyboardHeight(this.mContext);
        //在当前布局中监听键盘的回调
        this.addOnResizeListener(this);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        int childSum = this.getChildCount();
        //最多只能包含一个子类
        if (childSum > 1) {
            throw new IllegalStateException("can host only one direct child");
        } else {
            super.addView(child, index, params);
            android.widget.RelativeLayout.LayoutParams paramsChild;
            if (childSum == 0) {
                if (child.getId() < 0) {
                    child.setId(ID_CHILD);
                }
                //让子布局在底部
                paramsChild = (android.widget.RelativeLayout.LayoutParams) child.getLayoutParams();
                paramsChild.addRule(ALIGN_PARENT_BOTTOM);//ALIGN_PARENT_BOTTOM
                child.setLayoutParams(paramsChild);
            } else if (childSum == 1) {
                paramsChild = (android.widget.RelativeLayout.LayoutParams) child.getLayoutParams();
                //ABOVE 在id为ID_CHILD的子布局上
                paramsChild.addRule(ABOVE, ID_CHILD);
                child.setLayoutParams(paramsChild);
            }
        }
    }

    /**
     * 布局被加载完之后调用
     */
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.onSoftKeyboardHeightChanged(this.mSoftKeyboardHeight);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (this.mMaxParentHeight == 0) {
            this.mMaxParentHeight = h;
        }

    }

    public void updateMaxParentHeight(int maxParentHeight) {
        this.mMaxParentHeight = maxParentHeight;
        if (this.maxParentHeightChangeListener != null) {
            this.maxParentHeightChangeListener.onMaxParentHeightChange(maxParentHeight);
        }
    }

    /**
     * 当布局的高度改变之后，会重走onMeasure
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mMaxParentHeight != 0) {
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int expandSpec = MeasureSpec.makeMeasureSpec(this.mMaxParentHeight, heightMode);
            super.onMeasure(widthMeasureSpec, expandSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * 键盘弹起的回掉，如果系统中键盘默认高度和实际得到高度不一致，则改变系统默认的键盘高度，同时调用子类的 onSoftKeyboardHeightChanged
     */
    public void OnSoftPop(int height) {
        if (this.mSoftKeyboardHeight != height) {
            this.mSoftKeyboardHeight = height;
            KeyboardUtils.setDefKeyboardHeight(this.mContext, this.mSoftKeyboardHeight);
            this.onSoftKeyboardHeightChanged(this.mSoftKeyboardHeight);
        }

    }


    public void OnSoftClose() {
    }

    /**
     * 一定要看这个方法子类有没有调用
     */
    public abstract void onSoftKeyboardHeightChanged(int var1);

    public void setOnMaxParentHeightChangeListener(AutoHeightLayout.OnMaxParentHeightChangeListener listener) {
        this.maxParentHeightChangeListener = listener;
    }

    static {
        ID_CHILD = R.id.id_autolayout;
    }

    public interface OnMaxParentHeightChangeListener {
        void onMaxParentHeightChange(int var1);
    }
}
