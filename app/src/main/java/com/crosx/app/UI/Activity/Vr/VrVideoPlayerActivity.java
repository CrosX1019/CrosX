package com.crosx.app.UI.Activity.Vr;


import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.asha.vrlib.MDVRLibrary;
import com.asha.vrlib.model.BarrelDistortionConfig;
import com.crosx.app.R;
import com.crosx.app.Utils.VR.MD360PlayerActivity;
import com.crosx.app.Utils.VR.MediaPlayerWrapper;
import com.crosx.app.Utils.VR.StringUtil;

import java.util.Timer;
import java.util.TimerTask;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by CrosX on 2016/11/22.
 *
 * @BlinRoom
 */

public class VrVideoPlayerActivity extends MD360PlayerActivity {

    //播放进度条
    private SeekBar mSeekBar;
    //缓冲进度条
    private ProgressBar mProgressBar;


    @Override
    protected MDVRLibrary createVRLibrary() {
        return null;
    }
}
