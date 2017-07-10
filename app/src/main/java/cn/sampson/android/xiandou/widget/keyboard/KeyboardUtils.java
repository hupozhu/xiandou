package cn.sampson.android.xiandou.widget.keyboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import cn.sampson.android.xiandou.utils.Tip;


public class KeyboardUtils {
    private static final String EXTRA_DEF_KEYBOARDHEIGHT = "DEF_KEYBOARDHEIGHT";
    private static final int DEF_KEYBOARD_HEAGH_WITH_DP = 300;
    private static int sDefKeyboardHeight = -1;

    public KeyboardUtils() {
    }

    private static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static int getDisplayWidthPixels(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5F);
    }

    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5F);
    }

    public static int getFontHeight(TextView textView) {
        Paint paint = new Paint();
        paint.setTextSize(textView.getTextSize());
        FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil((double) (fm.bottom - fm.top));
    }

    public static View getRootView(Activity context) {
        return ((ViewGroup) context.findViewById(android.R.id.content)).getChildAt(0);
    }

    /**
     * 从系统参数中读取键盘默认高度，如果读取不到则设置其默认值为300px
     */
    public static int getDefKeyboardHeight(Context context) {
        if (sDefKeyboardHeight < 0) {
            sDefKeyboardHeight = dip2px(context, 300.0F);
        }

        int height = PreferenceManager.getDefaultSharedPreferences(context).getInt(EXTRA_DEF_KEYBOARDHEIGHT, 0);
        return sDefKeyboardHeight = height > 0 && sDefKeyboardHeight != height ? height : sDefKeyboardHeight;
    }

    public static void setDefKeyboardHeight(Context context, int height) {
        if (sDefKeyboardHeight != height) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(EXTRA_DEF_KEYBOARDHEIGHT, height).commit();
            sDefKeyboardHeight = height;
        }

    }

    /**
     * 判断当前activity是否全屏
     */
    public static boolean isFullScreen(Activity activity) {
        Tip.i("windowManager ===> " + (activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN));
        return (activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
    }

    public static void openSoftKeyboard(EditText et) {
        if (et != null) {
            et.setFocusable(true);
            et.setFocusableInTouchMode(true);
            et.requestFocus();
            InputMethodManager inputManager = (InputMethodManager) et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(et, 0);
        }

    }

    public static void closeSoftKeyboard(Context context) {
        if (context != null && context instanceof Activity && ((Activity) context).getCurrentFocus() != null) {
            try {
                View e = ((Activity) context).getCurrentFocus();
                InputMethodManager imm = (InputMethodManager) e.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                e.clearFocus();
                imm.hideSoftInputFromWindow(e.getWindowToken(), 0);
            } catch (Exception var3) {
                var3.printStackTrace();
            }

        }
    }

    /**
     * 调用系统服务关闭软键盘
     */
    public static void closeSoftKeyboard(View view) {
        if (view != null && view.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
