package com.crosx.app.ui.sign;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.crosx.app.MainActivity;
import com.crosx.app.R;
import com.crosx.app.utils.MySharedPrefUtil;
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
            ChatClient.getInstance().login("qqq123", "aaaaaa", new Callback() {
                //未登录，需要登录后，再进入会话界面
                @Override
                public void onSuccess() {
                    MySharedPrefUtil.saveData(mActivity, "isLogin", true);
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

    /* 验证手机格式*/
    public boolean isMobileNum(String s) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(s)) return false;
        else return s.matches(telRegex);
    }
}
