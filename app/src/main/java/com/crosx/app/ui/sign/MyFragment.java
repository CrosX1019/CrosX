package com.crosx.app.ui.sign;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crosx.app.BaseFragment;
import com.crosx.app.R;
import com.crosx.app.utils.MySharedPrefUtil;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.helpdesk.callback.Callback;

/**
 * Created by CrosX on 2016/11/30.
 *
 * @BlinRoom
 */

public class MyFragment extends BaseFragment {

    private static String TAG = MyFragment.class.getSimpleName();

    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my, null);
        mView.findViewById(R.id.btn_signOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatClient.getInstance().logout(true, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "success");
                        MySharedPrefUtil.saveData(mActivity, "isLogin", false);
                        startActivity(new Intent(mActivity, SignInActivity.class));
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.e(TAG, "error:" + s + ";code:" + i);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });
        return mView;
    }
}
