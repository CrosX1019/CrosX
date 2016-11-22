package com.crosx.app.Utils.VR;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.asha.vrlib.MDVRLibrary;
import com.crosx.app.R;
import com.crosx.app.UI.Activity.Vr.VrImagePlayerActivity;
import com.crosx.app.UI.Activity.Vr.VrVideoPlayerActivity;

/**
 * Created by CrosX on 2016/11/22.
 *
 * @BlinRoom
 */

public abstract class MD360PlayerActivity extends AppCompatActivity {

    private static String TAG = MD360PlayerActivity.class.getSimpleName();

    /**
     * 播放模式
     * NORMAL:单屏普通模式
     * GLASS:VR眼镜双屏模式
     */
    private static int NORMAL = MDVRLibrary.DISPLAY_MODE_NORMAL;
    private static int GLASS = MDVRLibrary.DISPLAY_MODE_GLASS;

    /**
     * 控制模式
     * TOUCH:手势控制
     * MOTION_WITH_TOUCH:陀螺仪感应+手势控制
     */
    private static int TOUCH = MDVRLibrary.INTERACTIVE_MODE_TOUCH;
    private static int MOTION_WITH_TOUCH = MDVRLibrary.INTERACTIVE_MODE_MOTION_WITH_TOUCH;

    /**
     * 获取URL
     *
     * @param context
     * @param uri
     * @param clz
     */
    private static void start(Context context, Uri uri, Class<? extends Activity> clz) {
        Intent i = new Intent(context, clz);
        i.setData(uri);
        context.startActivity(i);
    }

    /**
     * 播放全景视频
     *
     * @param context
     * @param uri
     */
    public static void startVideo(Context context, Uri uri) {
        start(context, uri, VrVideoPlayerActivity.class);
    }

    /**
     * 播放全景图片
     *
     * @param context
     * @param uri
     */
    public static void startBitmap(Context context, Uri uri) {
        start(context, uri, VrImagePlayerActivity.class);
    }

    //全景库
    private MDVRLibrary mVRLibrary;

    //播放,控制按钮
    private CheckBox displayMode, interactiveMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_md360player);

        mVRLibrary = createVRLibrary();

        displayMode = (CheckBox) findViewById(R.id.displayMode);
        displayMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //单屏
                    mVRLibrary.switchDisplayMode(MD360PlayerActivity.this, NORMAL);
                } else {
                    mVRLibrary.switchDisplayMode(MD360PlayerActivity.this, GLASS);
                }
            }
        });


        interactiveMode = (CheckBox) findViewById(R.id.interactiveMode);
        interactiveMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //单屏
                    mVRLibrary.switchInteractiveMode(MD360PlayerActivity.this, TOUCH);
                } else {
                    mVRLibrary.switchInteractiveMode(MD360PlayerActivity.this, MOTION_WITH_TOUCH);
                }
            }
        });
    }

    abstract protected MDVRLibrary createVRLibrary();

    public MDVRLibrary getVRLibrary() {
        return mVRLibrary;
    }


    @Override
    protected void onResume() {
        super.onResume();
        mVRLibrary.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVRLibrary.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVRLibrary.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mVRLibrary.onOrientationChanged(this);
    }

    public void cancelBusy() {
        findViewById(R.id.top_control).setVisibility(View.GONE);
        findViewById(R.id.bottom_control).setVisibility(View.GONE);
    }

    public void busy() {
        findViewById(R.id.top_control).setVisibility(View.VISIBLE);
        findViewById(R.id.bottom_control).setVisibility(View.VISIBLE);
    }

    protected Uri getUri() {
        Intent i = getIntent();
        if (i == null || i.getData() == null) {
            return null;
        }
        return i.getData();
    }
}
