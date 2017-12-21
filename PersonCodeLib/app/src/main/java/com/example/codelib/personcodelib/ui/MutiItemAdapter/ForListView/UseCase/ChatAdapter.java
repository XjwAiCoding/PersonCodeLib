package com.example.codelib.personcodelib.ui.MutiItemAdapter.ForListView.UseCase;

import android.content.Context;

import com.example.codelib.personcodelib.ui.MutiItemAdapter.ForListView.Base.MultiItemTypeAdapter;

import java.util.List;

/**
 * Created by xujiawei on 2017/12/22.
 */

public class ChatAdapter extends MultiItemTypeAdapter {

    public ChatAdapter(Context context, List dataList) {
        super(context, dataList);

        addItemViewDelegate(new MsgSendItemDelagate());
        addItemViewDelegate(new MsgComingItemDelagate());
    }
}
