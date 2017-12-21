package com.example.codelib.personcodelib.ui.MutiItemAdapter.ForListView.UseCase;

import com.example.codelib.personcodelib.R;
import com.example.codelib.personcodelib.ui.MutiItemAdapter.ForListView.Base.ItemViewDelegate;
import com.example.codelib.personcodelib.ui.MutiItemAdapter.ForListView.Base.MultiItemTypeAdapter;
import com.example.codelib.personcodelib.ui.MutiItemAdapter.ForListView.Base.ViewHolder;

/**
 * Created by xujiawei on 2017/12/22.
 */
public class MsgComingItemDelagate implements ItemViewDelegate<ChatMessage> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.main_chat_from_msg;
    }

    @Override
    public int getViewType() {
        return MultiItemTypeAdapter.COME_MSG_TYPE;
    }

    @Override
    public void convert(ViewHolder holder, ChatMessage chatMessage, int position) {
        holder.setText(R.id.chat_from_content, chatMessage.getContent());
        holder.setText(R.id.chat_from_name, chatMessage.getName());
        holder.setImageResource(R.id.chat_from_icon, chatMessage.getIcon());
    }
}
