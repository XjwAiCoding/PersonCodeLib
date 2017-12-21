package com.example.codelib.personcodelib.ui.MutiItemAdapter.ForListView.Base;


/**
 * Created by xujiawei on 17/12/22.
 */
public interface ItemViewDelegate<T>
{

    public abstract int getItemViewLayoutId();

    public abstract int getViewType();

    public abstract void convert(ViewHolder holder, T t, int position);

}
