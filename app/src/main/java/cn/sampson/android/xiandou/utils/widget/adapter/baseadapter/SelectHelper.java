package cn.sampson.android.xiandou.utils.widget.adapter.baseadapter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2015/9/3.
 */
public abstract class SelectHelper<T extends ISelectable> {

    private static final String TAG = "SelectHelper";
    private int mSelectMode;

    private List<Integer> mSelectedPositions;
    private List<Integer> mTempPositions;
    private int mSelectedPosition = -1;

    private List<T> mSelectDatas;

    public SelectHelper(int selectMode) {
        //如果支持多选时，初始化存放选中id的List
        if (selectMode == ISelectable.SELECT_MODE_MULTI)
            this.mSelectedPositions = new ArrayList<>();

        if (selectMode != ISelectable.SELECT_MODE_SINGLE && selectMode != ISelectable.SELECT_MODE_MULTI) {
            throw new IllegalArgumentException("invalid select mode = " + selectMode);
        }
        this.mSelectMode = selectMode;
    }

    /**
     * select the target position with notify data.if currentPosition  == position.ignore it.
     * <li></>only support select mode = {@link ISelectable#SELECT_MODE_SINGLE} ,this will auto update
     **/
    public void setSelected(int position) {
        if (mSelectMode == ISelectable.SELECT_MODE_MULTI)
            return;
        if (mSelectedPosition == position) {
            return;
        }
        if (position < 0)
            throw new IllegalArgumentException();
        if (mSelectedPosition != ISelectable.INVALID_POSITION) {
            getSelectedItemAtPosition(mSelectedPosition).setSelected(false);
            if (isRecyclable()) {
                notifyItemChanged(mSelectedPosition);
            }
        }
        mSelectedPosition = position;
        getSelectedItemAtPosition(position).setSelected(true);
        if (isRecyclable()) {
            notifyItemChanged(position);
        } else
            notifyAllChanged();
    }

    /**
     * {@link ISelectable#SELECT_MODE_MULTI} and {@link ISelectable#SELECT_MODE_SINGLE} both support
     */
    public void unselect(int position) {
        if (mSelectMode == ISelectable.SELECT_MODE_MULTI) {
            addUnselected(position);
        } else {
            setUnselected(position);
        }
    }

    /**
     * unselect the position of item ,
     * only support select mode = {@link ISelectable#SELECT_MODE_SINGLE}
     */
    public void setUnselected(int position) {
        if (mSelectMode == ISelectable.SELECT_MODE_MULTI)
            return;
        //  mSelectedPosition must == position
        if (mSelectedPosition == ISelectable.INVALID_POSITION
                || mSelectedPosition != position) {
            return;
        }
        if (position < 0)
            throw new IllegalArgumentException();

        getSelectedItemAtPosition(position).setSelected(false);
        mSelectedPosition = ISelectable.INVALID_POSITION;
        if (isRecyclable()) {
            notifyItemChanged(position);
        } else {
            notifyAllChanged();
        }
    }

    /**
     * only support select mode = {@link ISelectable#SELECT_MODE_MULTI}
     **/
    public void addUnselected(int position) {
        if (mSelectMode == ISelectable.SELECT_MODE_SINGLE)
            return;
        if (mSelectedPositions == null)
            throw new IllegalStateException("select mode must be multi");
        if (!mSelectedPositions.contains(position)) {
            return; //not selected
        }
        mSelectedPositions.remove(Integer.valueOf(position));
        getSelectedItemAtPosition(position).setSelected(false);
        if (isRecyclable()) {
            notifyItemChanged(position);
        } else
            notifyAllChanged();
    }

    /**
     * 将一个位置从选择数组中移除
     */
    public void addToUnSelectedList(Integer selectPosition) {
        if (mSelectMode == ISelectable.SELECT_MODE_SINGLE)
            return;
        if (mSelectedPositions == null)
            throw new IllegalStateException("select mode must be multi");
        if (!mSelectedPositions.contains(selectPosition)) {
            return; //not selected
        }
        mSelectedPositions.remove(selectPosition);
    }

    /**
     * 将多个位置从选择数组中移除
     */
    public void addToUnSelectedList(List<Integer> selectPosList) {
        if (mSelectMode == ISelectable.SELECT_MODE_SINGLE)
            return;
        if (mSelectedPositions == null)
            throw new IllegalStateException("select mode must be multi");
        if (!mSelectedPositions.containsAll(selectPosList)) {
            return; //not selected
        }
        mSelectedPositions.removeAll(selectPosList);
    }

    /**
     * only support select mode = {@link ISelectable#SELECT_MODE_MULTI}
     **/
    public void addSelected(int selectPosition) {
        if (mSelectMode == ISelectable.SELECT_MODE_SINGLE)
            return;
        if (mSelectedPositions == null)
            throw new IllegalStateException("select mode must be multi");
        if (mSelectedPositions.contains(selectPosition)) {
            Log.i(TAG, "the selectPosition = " + selectPosition + " is already selected!");
            return;
        }
        mSelectedPositions.add(selectPosition);
        getSelectedItemAtPosition(selectPosition).setSelected(true);
        if (isRecyclable()) {
            notifyItemChanged(selectPosition);
        } else
            notifyAllChanged();
    }

    /**
     * 将一个位置插入到选择数组中
     */
    public void addToSelectedList(Integer selectPosition) {
        if (mSelectMode == ISelectable.SELECT_MODE_SINGLE)
            return;
        if (mSelectedPositions == null)
            throw new IllegalStateException("select mode must be multi");
        if (mSelectedPositions.contains(selectPosition)) {
            Log.i(TAG, "the selectPosition = " + selectPosition + " is already selected!");
            return;
        }
        mSelectedPositions.add(selectPosition);
    }

    /**
     * 将一个多个位置插入到选择数组中
     */
    public void addToSelectedList(List<Integer> selectPosList) {
        if (mSelectMode == ISelectable.SELECT_MODE_SINGLE)
            return;
        if (mSelectedPositions == null)
            throw new IllegalStateException("select mode must be multi");
        if (mSelectedPositions.containsAll(selectPosList)) {
            Log.i(TAG, "the selectPositions had one or more selected!");
            return;
        }
        mSelectedPositions.addAll(selectPosList);
    }

    public void clearSelectedPositions() {
        if (mSelectMode == ISelectable.SELECT_MODE_MULTI) {
            mSelectedPositions.clear();
        } else {
            mSelectedPosition = ISelectable.INVALID_POSITION;
        }
    }

    public void clearAllSelected() {
        final boolean recyclable = isRecyclable();
        if (mSelectMode == ISelectable.SELECT_MODE_MULTI) {
            int pos;
            final List<Integer> mSelectedPositions = this.mSelectedPositions;
            for (int i = 0, size = mSelectedPositions.size(); i < size; i++) {
                pos = mSelectedPositions.get(i);
                getSelectedItemAtPosition(pos).setSelected(false);
                if (recyclable) {
                    notifyItemChanged(pos);
                }
            }
            mSelectedPositions.clear();
            if (!recyclable) {
                notifyAllChanged();
            }
        } else {
            if (mSelectedPosition != ISelectable.INVALID_POSITION) {
                int preSelectPos = mSelectedPosition;
                mSelectedPosition = ISelectable.INVALID_POSITION;
                getSelectedItemAtPosition(preSelectPos).setSelected(false);
                if (recyclable) {
                    notifyItemChanged(preSelectPos);
                } else {
                    notifyAllChanged();
                }
            }
        }
    }

    public void toogleSelected(int position) {
        if (position < 0) {
            throw new IllegalArgumentException(" position can't be negative !");
        }
        if (mSelectMode == ISelectable.SELECT_MODE_MULTI) {
            if (mSelectedPositions.contains(position)) {
                addUnselected(position);
            } else {
                addSelected(position);
            }
        } else {
            if (mSelectedPosition == position) {
                setUnselected(position);
            } else {
                setSelected(position);
            }
        }
    }

    public T getSelectedItem() {
        if (mSelectedPosition == ISelectable.INVALID_POSITION)
            return null;
        return getSelectedItemAtPosition(mSelectedPosition);
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    public List<Integer> getSelectedPositions() {
        if (mTempPositions == null) {
            mTempPositions = new ArrayList<>();
        }
        mTempPositions.clear();
        mTempPositions.addAll(mSelectedPositions);
        return mTempPositions;
    }

    public List<T> getSelectedItems() {
        if (mSelectedPositions == null || mSelectedPositions.size() == 0)
            return null;
        if (mSelectDatas == null) {
            mSelectDatas = new ArrayList<>();
        }
        final List<T> mSelectDatas = this.mSelectDatas;
        final List<Integer> mSelectedPositions = this.mSelectedPositions;

        mSelectDatas.clear();
        for (int i = 0, size = mSelectedPositions.size(); i < size; i++) {
            mSelectDatas.add(getSelectedItemAtPosition(mSelectedPositions.get(i)));
        }
        return mSelectDatas;
    }

    /*public*/ void initSelectPositions(List<T> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        for (int i = 0, size = list.size(); i < size; i++) {
            if (list.get(i).isSelected()) {
                initSelectPosition(i);
            }
        }
    }

    public void updateSelectPositions(List<T> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        for (int i = 0, size = list.size(); i < size; i++) {
            if (list.get(i).isSelected()) {
                initSelectPosition(i);
            }
        }
    }

    /**
     * this will only called once.
     */
    private void initSelectPosition(int position) {
        if (mSelectMode == ISelectable.SELECT_MODE_SINGLE) {
            if (mSelectedPosition == ISelectable.INVALID_POSITION) {
                mSelectedPosition = position;
            }
        } else if (mSelectMode == ISelectable.SELECT_MODE_MULTI) {
            if (!mSelectedPositions.contains(position))
                mSelectedPositions.add(position);
        } else {
            //can't reach here
            throw new RuntimeException();
        }
    }

    /**
     * indicate it use BaseAdapter/BaseExpandableListAdapter or QuickRecycleViewAdapter
     */
    protected boolean isRecyclable() {
        return false;
    }

    /**
     * update the datas of adapter ,eg: notifyDataSetChanged
     */
    protected abstract void notifyAllChanged();

    /**
     * only used for  RecycleViewAdapter
     */
    protected abstract void notifyItemChanged(int itemPosition);

    protected abstract T getSelectedItemAtPosition(int position);

}
