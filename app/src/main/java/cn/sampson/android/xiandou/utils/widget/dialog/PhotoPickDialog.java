package cn.sampson.android.xiandou.utils.widget.dialog;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import cn.sampson.android.xiandou.R;


/**
 * Created by Administrator on 2016/4/14.
 */
public class PhotoPickDialog implements View.OnClickListener {

    private Activity mContext;
    private AlertDialog dialog;
    private Window mWindow;
    private PhotoPickListener mListener;

    private TextView mTakePhoto;
    private TextView mPickAlbum;

    private boolean isCanceledOnTouchOutside = true;

    public PhotoPickDialog(Activity context) {
        mContext = context;
        dialog = new AlertDialog.Builder(context).create();
    }

    /**
     * 显示当前对话框
     */
    public void show() {
        dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);
        dialog.show();

        mWindow = dialog.getWindow();
        mWindow.setContentView(R.layout._dialog_photo_pick);
        mTakePhoto = (TextView) mWindow.findViewById(R.id.tv_take_photo);
        mPickAlbum = (TextView) mWindow.findViewById(R.id.tv_pick_photo);
        mTakePhoto.setOnClickListener(this);
        mPickAlbum.setOnClickListener(this);
    }

    public boolean isCanceledOnTouchOutside() {
        return isCanceledOnTouchOutside;
    }

    /**
     * 点击对话框外面是否可以取消
     */
    public void setCanceledOnTouchOutside(boolean isCanceledOnTouchOutside) {
        this.isCanceledOnTouchOutside = isCanceledOnTouchOutside;
    }

    public PhotoPickListener getClickListener() {
        return mListener;
    }

    public void setClickListener(PhotoPickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_take_photo:
                if (mListener != null)
                    mListener.fromCamera();
                dialog.dismiss();
                break;
            case R.id.tv_pick_photo:
                if (mListener != null)
                    mListener.fromAlbum();
                dialog.dismiss();
                break;
        }
    }


    public interface PhotoPickListener {
        void fromCamera();

        void fromAlbum();
    }

}
