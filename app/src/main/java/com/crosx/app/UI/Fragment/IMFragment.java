package com.crosx.app.UI.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crosx.app.BaseFragment;
import com.crosx.app.R;

/**
 * Created by CrosX on 2016/11/21.
 *
 * @BlinRoom
 */

public class IMFragment extends BaseFragment {

    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_im, null);
        return mView;
    }
}
