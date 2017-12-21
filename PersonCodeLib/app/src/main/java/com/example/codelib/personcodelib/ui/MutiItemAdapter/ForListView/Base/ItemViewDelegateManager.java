package com.example.codelib.personcodelib.ui.MutiItemAdapter.ForListView.Base;

import android.util.SparseArray;

/**
 * Created by xujiawei on 17/12/22.
 */
public class ItemViewDelegateManager<T> {
    private SparseArray<ItemViewDelegate<T>> mDelegateList = new SparseArray();

    public int getItemViewDelegateCount() {
        return mDelegateList.size();
    }

    public ItemViewDelegateManager<T> addDelegate(ItemViewDelegate<T> delegate) {
        if (delegate != null) {
            mDelegateList.put(delegate.getViewType(), delegate);
        }
        return this;
    }

    public void convert(ViewHolder holder, T item, int position, int viewType) {
        if (mDelegateList != null && mDelegateList.size() != 0) {
            ItemViewDelegate<T> delegate = mDelegateList.get(viewType);
            if (delegate.getViewType() == viewType) {
                delegate.convert(holder, item, position);
                return;
            }
        }
        throw new IllegalArgumentException("No ItemViewDelegateManager added that matches position=" + position + " in data source");
    }


    public int getItemViewLayoutId(int viewType) {
        if (mDelegateList != null && mDelegateList.size() != 0) {
            return mDelegateList.get(viewType).getItemViewLayoutId();
        }
        throw new IllegalArgumentException("No ItemViewDelegate is exited");
    }

    public int getItemViewType(ItemViewDelegate itemViewDelegate) {
        if (itemViewDelegate != null) {
            return itemViewDelegate.getViewType();
        }
        throw new IllegalArgumentException("ItemViewDelegate is null");
    }

    public ItemViewDelegate getItemViewDelegate(int viewType) {
        if (mDelegateList != null && mDelegateList.size() != 0){
            return mDelegateList.get(viewType);
        }
        throw new IllegalArgumentException("No ItemViewDelegate is founded");
    }

}
