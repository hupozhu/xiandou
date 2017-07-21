package cn.sampson.android.xiandou.widget.keyboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.utils.UiUtils;


/**
 * 该类将功能布局的处理封装起来，子类不需要去管底部布局弹起的处理
 *
 * Created by chengyang on 2016/12/19.
 */

public abstract class CommonKeyboard extends AutoHeightLayout {

    protected LayoutInflater mInflater;
    protected FuncLayout mFuncLy;

    public CommonKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        //1、获取到布局加载对象
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //2、加载唤起键盘的布局
        mInflater.inflate(getKeyboardLayoutId(), this);
        //3、初始化布局中的view
        initView();
        //4、初始化功能布局
        initFuncLayout();
    }

    @Override
    public void onSoftKeyboardHeightChanged(int height) {
        mFuncLy.updateHeight(height);
    }

    @Override
    public void OnSoftPop(int height) {
        super.OnSoftPop(height);
        mFuncLy.setVisibility(true);
    }

    @Override
    public void OnSoftClose() {
        super.OnSoftClose();
        if (mFuncLy.isOnlyShowSoftKeyboard()) {
            reset();
        }
    }

    @Override
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (KeyboardUtils.isFullScreen((Activity) getContext())) {
            return false;
        }
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        if (KeyboardUtils.isFullScreen((Activity) getContext())) {
            return;
        }
        super.requestChildFocus(child, focused);
    }

    /**
     * 设置布局资源
     */
    public abstract int getKeyboardLayoutId();

    /**
     * 初始化布局view
     */
    public abstract void initView();

    /**
     * 初始化功能布局
     */
    protected void initFuncLayout() {
        mFuncLy = UiUtils.find(this, R.id.ly_kvml);
    }

    /**
     * 关闭软键盘
     */
    public void reset() {
        KeyboardUtils.closeSoftKeyboard(this);
        mFuncLy.hideAllFuncView();
    }

    /**
     * 设置键盘的监听
     */
    public void addOnFuncKeyBoardListener(FuncLayout.OnFuncKeyBoardListener l) {
        mFuncLy.addOnKeyBoardListener(l);
    }
}
