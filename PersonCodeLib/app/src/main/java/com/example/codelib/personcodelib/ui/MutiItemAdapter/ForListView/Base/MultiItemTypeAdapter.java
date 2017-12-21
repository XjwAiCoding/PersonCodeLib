package com.example.codelib.personcodelib.ui.MutiItemAdapter.ForListView.Base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class MultiItemTypeAdapter<T> extends BaseAdapter {
    /**
     * 定义item ViewType
     */
    public static final int COME_MSG_TYPE = 0;
    public static final int SEND_MSG_TYPE = 1;

    protected Context mContext;
    protected List<T> mDataList;
    private ItemViewDelegateManager mItemViewDelegateManager;

    public MultiItemTypeAdapter(Context context, List<T> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
        mItemViewDelegateManager = new ItemViewDelegateManager();
    }

    /** 此处添加不同类型的item */
    public MultiItemTypeAdapter addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    private boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    @Override
    public int getViewTypeCount() {
        if (useItemViewDelegateManager()) {
            return mItemViewDelegateManager.getItemViewDelegateCount();
        }
        return super.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        /** 此处逻辑需根据需求去自定义 */
        int viewType;
        if (position == 0) {
            viewType = COME_MSG_TYPE;
        } else {
            viewType = SEND_MSG_TYPE;
        }
        return viewType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        ItemViewDelegate itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        ViewHolder viewHolder;
        if (convertView == null) {
            View itemView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
            viewHolder = new ViewHolder(mContext, itemView, parent, position);
            viewHolder.mLayoutId = layoutId;
            onViewHolderCreated(viewHolder, viewHolder.getConvertView());
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.mPosition = position;
        }
        convert(viewHolder, getItem(position), position, viewType);
        return viewHolder.getConvertView();
    }

    protected void convert(ViewHolder viewHolder, T item, int position, int viewType) {
        mItemViewDelegateManager.convert(viewHolder, item, position, viewType);
    }

    public void onViewHolderCreated(ViewHolder holder, View itemView) {
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public T getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
