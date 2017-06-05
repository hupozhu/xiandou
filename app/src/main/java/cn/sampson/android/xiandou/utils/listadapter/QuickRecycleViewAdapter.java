/*
 * Copyright (C) 2015
 *            heaven7(donshine723@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.sampson.android.xiandou.utils.listadapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

import cn.sampson.android.xiandou.R;

/**
 * Created by heaven7 on 2015/8/26.
 *
 * @param <T> the data
 */
public abstract class QuickRecycleViewAdapter<T extends ISelectable> extends RecyclerView.Adapter<QuickRecycleViewAdapter.ViewHolder>
        implements AdapterManager.IAdapterManagerCallback, AdapterManager.IHeaderFooterManager {

    private int mLayoutId = 0;
    private HeaderFooterEmptyHelper mHeaderFooterHelper;
    private AdapterManager<T> mAdapterManager;
    private LinearLayoutManager linearLayoutManager;

    private int lastVisibleItem, totalItemCount;
    //loadmore前多少个item开始加载
    private int visibleThreshold = 1;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private int bottomLoading = 0;
    private int bottomPosition;

    /**
     * 默认支持单项选中，list的每个item必须继承ISelectable
     */
    public QuickRecycleViewAdapter(int layoutId, List<T> mDatas) {
        this(layoutId, mDatas, ISelectable.SELECT_MODE_SINGLE, null);
    }

    /**
     * create QuickRecycleViewAdapter with the layout id. if layoutId==0, the method
     * {@link #getItemLayoutId(int, ISelectable)} will be called.
     *
     * @param layoutId   the layout id you want to inflate, or 0 if you want multi item.
     * @param mDatas
     * @param selectMode select mode
     */
    public QuickRecycleViewAdapter(int layoutId, List<T> mDatas, int selectMode, RecyclerView recyclerView) {
        if (layoutId < 0) {
            throw new IllegalArgumentException("layoutId can't be negative ");
        }
        this.mLayoutId = layoutId;
        mAdapterManager = createAdapterManager(mDatas, selectMode);

        if (recyclerView != null && recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (linearLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        if (bottomLoading <= 0 || mAdapterManager.getItemSize() <= 0) //如果没有数据就返回
                            return;

                        //判断是否下拉到最下面，自动加载更多
                        totalItemCount = linearLayoutManager.getItemCount();
                        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                        int footerCount = mHeaderFooterHelper == null ? 0 : mHeaderFooterHelper.getFooterViewSize();
                        if (!loading && totalItemCount - footerCount <= (lastVisibleItem + bottomLoading)) {
                            // End has been reached
                            // Do something
                            if (onLoadMoreListener != null) {
                                onLoadMoreListener.onLoadMore();
                            }
                            loading = true;
                        }
                    }
                });
            }
        }
        onFinalInit();
    }

    public QuickRecycleViewAdapter(int layoutId, List<T> mDatas, RecyclerView recyclerView) {
        this(layoutId, mDatas, ISelectable.SELECT_MODE_SINGLE, recyclerView);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded(List<T> data, int pageNum) {
        loading = false;
        if (data != null && data.size() > 0) {
            if (data.size() < pageNum) {
                setNoMore();
            }
            mAdapterManager.addItems(data);
        } else {
            setNoMore();
        }
    }

    public void setRefresh(List<T> data, int pageNum) {
        if (data != null && data.size() > 0) {
            if (data.size() < pageNum) {
                setNoMore();
            } else {
                resetBottom();
            }
            mAdapterManager.replaceAllItems(data);
        } else {
            mAdapterManager.clearItems();
            setNoMore();
        }
    }

    //如果数据已经加载完全就隐藏加载更多
    private void setNoMore() {
        if (bottomLoading == 0) return;
        bottomLoading = 0;
        notifyItemRemoved(bottomPosition);
    }

    //如果下拉刷新了，显示加载更多
    private void resetBottom() {
        bottomLoading = 1;
    }

    private AdapterManager<T> createAdapterManager(List<T> mDatas, int selectMode) {
        return new AdapterManager<T>(mDatas, selectMode, this) {
            @Override
            protected void notifyDataSetChangedImpl() {
                QuickRecycleViewAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected boolean isRecyclable() {
                return true;
            }

            @Override
            public void notifyItemInsertedImpl(int position) {
                QuickRecycleViewAdapter.this.notifyItemInserted(position);
            }

            @Override
            public void notifyItemChangedImpl(int position) {
                QuickRecycleViewAdapter.this.notifyItemChanged(position);
            }

            @Override
            public void notifyItemMovedImpl(int fromPosition, int toPosition) {
                QuickRecycleViewAdapter.this.notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void notifyItemRemovedImpl(int position) {
                QuickRecycleViewAdapter.this.notifyItemRemoved(position);
            }

            @Override
            public void notifyItemRangeChangedImpl(int positionStart, int itemCount) {
                QuickRecycleViewAdapter.this.notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void notifyItemRangeInsertedImpl(int positionStart, int itemCount) {
                QuickRecycleViewAdapter.this.notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void notifyItemRangeRemovedImpl(int positionStart, int itemCount) {
                QuickRecycleViewAdapter.this.notifyItemRangeRemoved(positionStart, itemCount);
            }

            @Override
            protected void beforeNotifyDataChanged() {
                QuickRecycleViewAdapter.this.beforeNotifyDataChanged();
            }

            @Override
            protected void afterNotifyDataChanged() {
                QuickRecycleViewAdapter.this.afterNotifyDataChanged();
            }

            @Override
            public IHeaderFooterManager getHeaderFooterManager() {
                return QuickRecycleViewAdapter.this;
            }
        };
    }

    /**
     * called before {@link #notifyDataSetChanged()}
     */
    protected void beforeNotifyDataChanged() {

    }

    /**
     * this is callled after data {@link #notifyDataSetChanged()}
     */
    protected void afterNotifyDataChanged() {

    }

    /**
     * the init operation of the last, called in constructor
     */
    protected void onFinalInit() {

    }

    //=================== start header footer view ======================= //
    @Override
    public void addHeaderView(View v) {
        if (mHeaderFooterHelper == null)
            mHeaderFooterHelper = new HeaderFooterEmptyHelper();
        int headerSize = getHeaderSize();
        mHeaderFooterHelper.addHeaderView(v);
        notifyItemInserted(headerSize);
    }

    @Override
    public void addHeaderView(int index, View v) {
        if (mHeaderFooterHelper == null)
            mHeaderFooterHelper = new HeaderFooterEmptyHelper();
        int headerSize = getHeaderSize();
        if (index <= headerSize) {
            mHeaderFooterHelper.addHeaderView(index, v);
            notifyDataSetChanged();
        }
    }

    @Override
    public void removeHeaderView(View v) {
        if (mHeaderFooterHelper != null && v != null) {
            int index = mHeaderFooterHelper.removeHeaderView(v);
            if (index != -1) {
                notifyItemRemoved(index);
            }
        }
    }

    /**
     * @param viewId
     * @param isIgnoreHeaderFooter 有空白页面时覆盖header和footer，一般为true
     *                             为false是，无item但要显示header或者footer
     */
    @Override
    public void setEmptyPage(int viewId, boolean isIgnoreHeaderFooter) {
        if (mHeaderFooterHelper == null) {
            mHeaderFooterHelper = new HeaderFooterEmptyHelper();
        }
        mHeaderFooterHelper.setEmptyPage(viewId, isIgnoreHeaderFooter);
    }

    @Override
    public void addFooterView(View v) {
        if (mHeaderFooterHelper == null)
            mHeaderFooterHelper = new HeaderFooterEmptyHelper();
        int itemCount = getItemCount();
        mHeaderFooterHelper.addFooterView(v);
        notifyItemInserted(itemCount);
    }

    @Override
    public void removeFooterView(View v) {
        if (mHeaderFooterHelper != null) {
            int index = mHeaderFooterHelper.removeFooterView(v);
            if (index != -1) {
                notifyItemRemoved(index + getHeaderSize() + mAdapterManager.getItemSize() + bottomLoading);
            }
        }
    }

    @Override
    public int getHeaderSize() {
        return mHeaderFooterHelper == null ? 0 : mHeaderFooterHelper.getHeaderViewSize();
    }

    @Override
    public int getFooterSize() {
        return mHeaderFooterHelper == null ? 0 : mHeaderFooterHelper.getFooterViewSize();
    }
    // =================== end header footer view ======================= //

    public SelectHelper<T> getSelectHelper() {
        return getAdapterManager().getSelectHelper();
    }

    public final T getItem(int position) {
        return mAdapterManager.getItems().get(position);
    }

    /**
     * select the target position
     * only support select mode = {@link ISelectable#SELECT_MODE_MULTI}
     **/
    public void addSelected(int selectPosition) {
        getSelectHelper().addSelected(selectPosition);
    }

    /**
     * un select the target position  .
     * <li>only support select mode = {@link ISelectable#SELECT_MODE_MULTI}
     */
    public void addUnselected(int position) {
        getSelectHelper().addUnselected(position);
    }

    /**
     * un select the all selected position.
     * mode single or multi all supoorted
     */
    public void clearAllSelected() {
        getSelectHelper().clearAllSelected();
    }

    /**
     * select the target position with notify data.if currentPosition  == position.ignore it.
     * <li></>only support select mode = {@link ISelectable#SELECT_MODE_SINGLE} ,this will auto update
     **/
    public void setSelected(int position) {
        getSelectHelper().setSelected(position);
    }

    /**
     * un select the target position
     * <li>only support select mode = {@link ISelectable#SELECT_MODE_SINGLE}
     */
    public void setUnselected(int position) {
        getSelectHelper().setUnselected(position);
    }

    /**
     * clear selected positions  . this just clear record. bu not notify item change
     * <li> support select mode = {@link ISelectable#SELECT_MODE_SINGLE} or {@link ISelectable#SELECT_MODE_MULTI}
     */
    public void clearSelectedPositions() {
        getSelectHelper().clearSelectedPositions();
    }

    public T getSelectedData() {
        return getSelectHelper().getSelectedItem();
    }

    public List<T> getSelectedItems() {
        return getSelectHelper().getSelectedItems();
    }

    public int getSelectedPosition() {
        return getSelectHelper().getSelectedPosition();
    }

    //====================== begin items ========================//

    @Override
    public AdapterManager<T> getAdapterManager() {
        return mAdapterManager;
    }

    //====================== end items ========================//

    @Override
    public final int getItemViewType(int position) {
        if (mHeaderFooterHelper != null) {
            if ((mHeaderFooterHelper.getEmptyPage() > 0 && mHeaderFooterHelper.isIgnoreHeaderFooter() && mAdapterManager.getItemSize() == 0) || (mHeaderFooterHelper.getEmptyPage() > 0 && !mHeaderFooterHelper.isIgnoreHeaderFooter() && mHeaderFooterHelper.getHeaderViewSize() + mHeaderFooterHelper.getFooterViewSize() + mAdapterManager.getItemSize() + bottomLoading == 0))
                return mHeaderFooterHelper.getEmptyPage();
            //in header or footer
            if (mHeaderFooterHelper.isInHeader(position) || mHeaderFooterHelper.isInFooter(position, mAdapterManager.getItemSize() + bottomLoading))
                return position;

            position -= mHeaderFooterHelper.getHeaderViewSize();
        }
        int layoutId;
        if (bottomLoading > 0 && position == mAdapterManager.getItemSize()) {
            bottomPosition = position;
            return R.layout._recycle_bottom_loading;
        } else {
            layoutId = getItemLayoutId(position, getItem(position));
        }

        if (mHeaderFooterHelper != null)
            mHeaderFooterHelper.recordLayoutId(layoutId);
        return layoutId;
    }

    /**
     * 创建holder对象
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public final ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //isLayoutIdInRecord用于判断id是否存在，用于item的复用
        if (mHeaderFooterHelper == null || mHeaderFooterHelper.isLayoutIdInRecord(viewType)) {
            if (bottomLoading > 0 && bottomPosition > 0 && viewType == bottomPosition) {
                //加载更多
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout._recycle_bottom_loading, parent, false));
            } else {
                //item
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false), viewType);
            }
        } else {
            if (mHeaderFooterHelper.getEmptyPage() > 0 && mHeaderFooterHelper.isIgnoreHeaderFooter() && mAdapterManager.getItemSize() == 0) {
                //空页面
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(mHeaderFooterHelper.getEmptyPage(), parent, false));
            } else if (bottomLoading > 0 && bottomPosition > 0 && viewType == R.layout._recycle_bottom_loading) {
                //加载更多
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout._recycle_bottom_loading, parent, false));
            } else {
                if (mHeaderFooterHelper.getEmptyPage() > 0 && !mHeaderFooterHelper.isIgnoreHeaderFooter() && mAdapterManager.getItemSize() + mHeaderFooterHelper.getHeaderViewSize() + mHeaderFooterHelper.getFooterViewSize() + bottomLoading == 0) {
                    //有头部情况下的空白页面
                    return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(mHeaderFooterHelper.getEmptyPage(), parent, false));
                } else {
                    //item
                    return new ViewHolder(mHeaderFooterHelper.findView(viewType, mAdapterManager.getItemSize() + bottomLoading));
                }
            }
        }
    }

    /**
     * 有多少个item
     *
     * @return
     */
    @Override
    public final int getItemCount() {
        if (mHeaderFooterHelper == null) {
            return mAdapterManager.getItemSize() + bottomLoading;
        } else {
            if (mHeaderFooterHelper.getEmptyPage() > 0 && mHeaderFooterHelper.isIgnoreHeaderFooter()) {//有空白页面，并且item不包含footer和header
                return mAdapterManager.getItemSize() > 0 ? mAdapterManager.getItemSize() + mHeaderFooterHelper.getHeaderViewSize() + mHeaderFooterHelper.getFooterViewSize() + bottomLoading : 1;
            } else {//没有空白页,或者有空白页面，但包涵footer和header
                int count = mAdapterManager.getItemSize() + mHeaderFooterHelper.getHeaderViewSize() + mHeaderFooterHelper.getFooterViewSize() + bottomLoading;
                if (mHeaderFooterHelper.getEmptyPage() > 0 && !mHeaderFooterHelper.isIgnoreHeaderFooter()) {
                    return count > 0 ? count : 1;
                } else {
                    return count;
                }
            }
        }
    }

    /**
     * holder对象绑定，为其赋值
     *
     * @param holder
     * @param position
     */
    @Override
    public final void onBindViewHolder(ViewHolder holder, int position) {
        if (mHeaderFooterHelper != null) {
            //如果是空白页面则返回
            if ((mHeaderFooterHelper.getEmptyPage() > 0 && mHeaderFooterHelper.isIgnoreHeaderFooter() && mAdapterManager.getItemSize() == 0) || (mHeaderFooterHelper.getEmptyPage() > 0 && !mHeaderFooterHelper.isIgnoreHeaderFooter() && mHeaderFooterHelper.getHeaderViewSize() + mHeaderFooterHelper.getFooterViewSize() + mAdapterManager.getItemSize() + bottomLoading == 0)) {
                return;
            }
            //如果当前item已经存在则返回，让header和footer类容保持不变
            if (mHeaderFooterHelper.isInHeader(position) || mHeaderFooterHelper.isInFooter(position, mAdapterManager.getItemSize() + bottomLoading)) {
                return;
            }
            position -= mHeaderFooterHelper.getHeaderViewSize();
        }
        if (bottomLoading > 0 && mAdapterManager.getItemSize() == position)
            return;
        //not in header or footer populate it
        onBindData(holder.getContext(), position, getItem(position), holder.mLayoutId, holder.mViewHelper);
    }

    /**
     * if you use multi item ,override this
     * <p/>
     * 多种布局时复写这个方法
     */

    protected int getItemLayoutId(int position, T t) {
        return mLayoutId;
    }

    protected abstract void onBindData(Context context, int position, T item, int itemLayoutId, ViewHelper helper);

    /*public*/ static class ViewHolder extends RecyclerView.ViewHolder {

        public final ViewHelper mViewHelper;
        /**
         * if is in header or footer ,mLayoutId = 0
         */
        public final int mLayoutId;

        public ViewHolder(View itemView, int layoutId) {
            super(itemView);
            this.mLayoutId = layoutId;
            this.mViewHelper = new ViewHelper(itemView);
        }

        public ViewHolder(View itemView) {
            this(itemView, 0);
        }

        public Context getContext() {
            return mViewHelper.getContext();
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
