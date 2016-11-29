package com.crosx.app.UI.Fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.crosx.app.BaseFragment;
import com.crosx.app.R;
import com.crosx.app.Utils.VR.MD360PlayerActivity;

/**
 * Created by CrosX on 2016/11/22.
 *
 * @BlinRoom
 */

public class VideoFragment extends BaseFragment {

    private View mView;

    private Button mVrVideo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_video, null);
        mVrVideo = (Button) mView.findViewById(R.id.video_btn_vrVideo);
        mVrVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MD360PlayerActivity.startVideo(getActivity(), Uri.parse("http://oe7fx58st.bkt.clouddn.com/mcst_text001.mp4"));
            }
        });
        return mView;
    }
}
