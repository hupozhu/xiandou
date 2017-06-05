package cn.sampson.android.xiandou.utils.listadapter;

/**
 * Created by heaven7 on 2015/9/2.
 */
public interface ISelectable {

    int SELECT_MODE_SINGLE  = 1;
    int SELECT_MODE_MULTI   = 2;

    int INVALID_POSITION    = -1;

    void setSelected(boolean selected);
    boolean isSelected();
}
