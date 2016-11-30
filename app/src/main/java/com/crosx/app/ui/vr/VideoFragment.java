package com.crosx.app.ui.vr;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.asha.vrlib.MDVRLibrary;
import com.asha.vrlib.texture.MD360BitmapTexture;
import com.crosx.app.BaseFragment;
import com.crosx.app.R;
import com.crosx.app.utils.MD360PlayerActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import static com.squareup.picasso.MemoryPolicy.NO_CACHE;
import static com.squareup.picasso.MemoryPolicy.NO_STORE;

/**
 * Created by CrosX on 2016/11/22.
 *
 * @BlinRoom
 */

public class VideoFragment extends BaseFragment {

    private View mView;

    private Button mVrVideo;

    private String mPath = "file:///android_asset/vr.jpg";

    private MDVRLibrary mdvrLibrary;

    private GLSurfaceView glSurfaceView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_video, null);
        glSurfaceView = (GLSurfaceView) mView.findViewById(R.id.vr_ImageView);
        mdvrLibrary = MDVRLibrary.with(mActivity)
                .ifNotSupport(new MDVRLibrary.INotSupportCallback() {
                    @Override
                    public void onNotSupport(int mode) {
                        String tip = mode == MDVRLibrary.INTERACTIVE_MODE_MOTION ? "设备不支持感应模式，请滑动视频控制视角" : "设备不支持感应模式，请滑动视频控制视角";
                        mdvrLibrary.switchInteractiveMode(mActivity, MDVRLibrary.INTERACTIVE_MODE_TOUCH);
                        Toast.makeText(mActivity, tip, Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .displayMode(MDVRLibrary.DISPLAY_MODE_NORMAL)
                .interactiveMode(MDVRLibrary.INTERACTIVE_MODE_MOTION)
                .asBitmap(new MDVRLibrary.IBitmapProvider() {
                    @Override
                    public void onProvideBitmap(final MD360BitmapTexture.Callback callback) {
                        loadImage(mPath, callback);
                    }
                })
                .listenGesture(new MDVRLibrary.IGestureListener() {
                    @Override
                    public void onClick(MotionEvent motionEvent) {
                    }

                })
                .pinchEnabled(true)
                .build(glSurfaceView);
        mVrVideo = (Button) mView.findViewById(R.id.video_btn_vrVideo);
        mVrVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MD360PlayerActivity.startVideo(getActivity(), Uri.parse("http://oe7fx58st.bkt.clouddn.com/mcst_text001.mp4"));
            }
        });
        return mView;
    }

    private Target mTarget;// keep the reference for picasso.

    private void loadImage(String path, final MD360BitmapTexture.Callback callback) {
        mTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // notify if size changed
                mdvrLibrary.onTextureResize(bitmap.getWidth(), bitmap.getHeight());

                // texture
                callback.texture(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(mActivity)
                .load(path)
                .resize(3072, 2048)
                .centerInside()
                .memoryPolicy(NO_CACHE, NO_STORE)
                .into(mTarget);
    }


}
