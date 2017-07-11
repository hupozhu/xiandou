package cn.sampson.android.xiandou.widget.timepicker.common;

import cn.sampson.android.xiandou.widget.timepicker.WheelView;
import cn.sampson.android.xiandou.widget.timepicker.listener.OnItemPickListener;

final public class OnItemPickedRunnable implements Runnable {
    final private WheelView wheelView;
    private OnItemPickListener onItemPickListener;
    public OnItemPickedRunnable(WheelView wheelView, OnItemPickListener onItemPickListener) {
        this.wheelView = wheelView;
        this.onItemPickListener = onItemPickListener;
    }

    @Override
    public final void run() {
        onItemPickListener.onItemPicked(wheelView.getCurrentPosition(),wheelView.getCurrentItem());
    }
}
