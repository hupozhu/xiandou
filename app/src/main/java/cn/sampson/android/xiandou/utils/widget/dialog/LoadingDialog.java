package cn.sampson.android.xiandou.utils.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;


import java.util.HashMap;

import cn.sampson.android.xiandou.R;

/**
 * Created by Administrator on 2016/3/23 0023.
 */
public class LoadingDialog extends Dialog {

    private static HashMap<String, LoadingDialog> dialogMap;

    private LoadingDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(android.R.color.transparent)));
        getWindow().setGravity(Gravity.CENTER);
        setContentView(R.layout._dialog_loading);
    }

    private static synchronized HashMap<String, LoadingDialog> getDialogMap() {
        if (dialogMap == null) {
            dialogMap = new HashMap<>();
        }
        return dialogMap;
    }

    public static LoadingDialog createHud(Context ctx) {
        if (ctx == null) {
            return null;
        }
        if (ctx instanceof Activity) {
            if (((Activity) ctx).getParent() != null) {
                ctx = ((Activity) ctx).getParent();
            }
            Window window = ((Activity) ctx).getWindow();
            if (window != null) {
                Object tag = window.getDecorView().getTag();
                if (tag != null && (tag instanceof LoadingDialog)) {
                    return (LoadingDialog) tag;
                } else {
                    LoadingDialog dialog = new LoadingDialog(ctx);
                    window.getDecorView().setTag(dialog);
                    return dialog;
                }
            }
        } else {
            LoadingDialog dialog = getDialogMap().get(ctx.getClass().getName());
            if (dialog == null) {
                dialog = new LoadingDialog(ctx);
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                getDialogMap().put(ctx.getClass().getName(), dialog);
            }
            return dialog;
        }
        return null;
    }

    public static void showProgress(Context ctx) {
        showProgress(ctx, "", true);
    }

    public static void showProgress(Context ctx, int resId) {
        if (ctx == null) {
            return;
        }
        showProgress(ctx, ctx.getString(resId));
    }

    public static void showProgress(Context ctx, String msg) {
        showProgress(ctx, msg, true);
    }

    public static void showProgress(Context ctx, int resId, boolean cancelable) {
        if (ctx == null) {
            return;
        }
        showProgress(ctx, ctx.getString(resId), cancelable);
    }

    public static void showProgress(Context ctx, String msg, boolean cancelable) {
        LoadingDialog dialog = createHud(ctx);
        if (dialog != null) {
            dialog.setCancelable(cancelable);
            dialog.setCanceledOnTouchOutside(cancelable);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if ((ctx instanceof Activity) && ((Activity) ctx).isFinishing()) {
                return;
            }

            dialog.show();
        }
    }

    public static void dismissProgress(Context ctx) {
        LoadingDialog dialog = createHud(ctx);
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public static boolean isShowing(Context ctx) {
        LoadingDialog dialog = createHud(ctx);
        if (dialog != null) {
            return dialog.isShowing();
        }
        return false;
    }

}
