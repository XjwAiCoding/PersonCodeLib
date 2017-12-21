package com.example.codelib.personcodelib;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.codelib.personcodelib.ui.MutiItemAdapter.ForListView.UseCase.ChatAdapter;
import com.example.codelib.personcodelib.ui.MutiItemAdapter.ForListView.UseCase.ChatMessage;

public class MutiItemViewAdapterActivity extends Activity {
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muti_item_view_adapter);

        mListView = (ListView) findViewById(R.id.id_listview_list);
        mListView.setDivider(null);
        mListView.setAdapter(new ChatAdapter(this, ChatMessage.MOCK_DATAS));
    }
}
