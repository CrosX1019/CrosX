package com.crosx.app.ui.im;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crosx.app.BaseFragment;
import com.crosx.app.R;
import com.crosx.app.ui.sign.SignInActivity;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.helpdesk.callback.Callback;
import com.hyphenate.helpdesk.easeui.Constant;
import com.hyphenate.helpdesk.easeui.ui.ChatFragment;
import com.hyphenate.helpdesk.easeui.util.IntentBuilder;

/**
 * Created by CrosX on 2016/11/21.
 *
 * @BlinRoom
 */

public class IMFragment extends BaseFragment {

    private static String TAG = IMFragment.class.getSimpleName();

    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_im, null);
        mView.findViewById(R.id.btn_connectKeFu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new IntentBuilder(getActivity())
                        .setServiceIMNumber("crosx").setTargetClass(ChatActivity.class).build();
                startActivity(intent);
            }
        });
        mView.findViewById(R.id.btn_sign_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatClient.getInstance().logout(false, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "success");
                        startActivity(new Intent(getActivity(), SignInActivity.class));
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
        });
        return mView;
    }
}
