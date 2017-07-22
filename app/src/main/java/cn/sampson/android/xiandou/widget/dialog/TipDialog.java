package cn.sampson.android.xiandou.widget.dialog;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.ui.SplashActivity;
import cn.sampson.android.xiandou.ui.main.MainActivity;


/**
 * Created by Administrator on 2016/4/14.
 */
public class TipDialog {

    private Activity mContext;
    private AlertDialog dialog;
    private Window mWindow;

    private TextView mTipMessage;

    private boolean isCanceledOnTouchOutside = false;

    public TipDialog(Activity context) {
        mContext = context;
        dialog = new AlertDialog.Builder(context).create();

        mWindow = dialog.getWindow();
        mWindow.setContentView(R.layout.dialog_tip);
    }

    /**
     * 显示当前对话框
     */
    public void show() {
        dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        dialog.show();

        mWindow = dialog.getWindow();
        mWindow.setContentView(R.layout._dialog_photo_pick);
        mTipMessage = (TextView) mWindow.findViewById(R.id.tv_msg);
    }

    public void setTipMessage(String msg) {
        mTipMessage.setText(msg);
    }

    void dismiss() {
        dialog.dismiss();
    }

    private final MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<TipDialog> mActivity;

        public MyHandler(TipDialog activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            TipDialog activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 0:


                        break;
                }
            }
        }
    }
}
