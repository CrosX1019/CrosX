package com.crosx.app.ui.sign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.crosx.app.MainActivity;
import com.crosx.app.R;
import com.crosx.app.utils.BaseActivity;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.helpdesk.callback.Callback;

public class SignInActivity extends BaseActivity {

    private static String TAG = SignInActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in);
    }

    public void SignIn(View view) {
        if (ChatClient.getInstance().isLoggedInBefore()) {
            //已经登录，可以直接进入会话界面
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
        } else {
            //未登录，需要登录后，再进入会话界面
            ChatClient.getInstance().login("test1019", "111111", new Callback() {
                @Override
                public void onSuccess() {
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                }

                @Override
                public void onError(int i, String s) {
                    Log.e(TAG, s);
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
        }


    }
}
