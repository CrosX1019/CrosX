package com.crosx.app.ui.im;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.crosx.app.BaseActivity;
import com.crosx.app.R;
import com.hyphenate.helpdesk.easeui.ui.ChatFragment;

/**
 * Created by CrosX on 2016/11/29.
 *
 * @BlinRoom
 */

public class ChatActivity extends BaseActivity {

    private ChatFragment chatFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatFragment = new ChatFragment();
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.chat_container, chatFragment).commit();

    }
}
