package cn.sampson.android.xiandou.core.manager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.sampson.android.xiandou.R;
import cn.sampson.android.xiandou.utils.ContextUtil;

/**
 * Created by Administrator on 2017/7/22 0022.
 */

public class TipManager {

    public static void showTip(String msg) {
        View layout = LayoutInflater.from(ContextUtil.getContext()).inflate(R.layout.dialog_tip, null);
        TextView title = (TextView) layout.findViewById(R.id.tv_msg);
        title.setText(msg);
        Toast toast = new Toast(ContextUtil.getContext().getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
