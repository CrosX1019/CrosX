package com.crosx.app.ui;

import android.content.Intent;
import android.os.Bundle;

import com.crosx.app.MainActivity;
import com.crosx.app.R;
import com.crosx.app.ui.sign.SignInActivity;
import com.crosx.app.BaseActivity;
import com.crosx.app.utils.MySharedPrefUtil;
import com.hyphenate.chat.ChatClient;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        getHomeActivity();
    }

    private void getHomeActivity() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                boolean isLogin = (boolean) MySharedPrefUtil.getData(mActivity, "isLogin", false);
                if (isLogin && ChatClient.getInstance().isLoggedInBefore()) {
                    startActivity(new Intent(mActivity, MainActivity.class));
                } else {
                    startActivity(new Intent(mActivity, SignInActivity.class));
                }

            }
        };
        timer.schedule(task, 1000);
    }

}
