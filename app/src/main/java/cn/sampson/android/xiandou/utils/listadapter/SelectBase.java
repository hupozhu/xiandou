package cn.sampson.android.xiandou.utils.listadapter;

/**
 * Created by Administrator on 2016/4/18.
 */
public class SelectBase implements ISelectable {

    protected boolean isSelect;

    @Override
    public void setSelected(boolean selected) {
        isSelect = selected;
    }

    @Override
    public boolean isSelected() {
        return isSelect;
    }
}
