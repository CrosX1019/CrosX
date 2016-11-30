package com.crosx.app.ui.vr;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.crosx.app.utils.MD360PlayerActivity;
import com.crosx.app.utils.MediaPlayerWrapper;
import com.crosx.app.utils.StringUtil;

import java.util.Timer;
import java.util.TimerTask;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by CrosX on 2016/11/22.
 *
 * @BlinRoom
 */

public class VrVideoPlayerActivity extends MD360PlayerActivity {

    //进度条
    private SeekBar videoProgressSeekBar;
    //缓冲条
    private ProgressBar bufferProgress;
    //播放&暂停按钮
    private ImageButton playOrPauseButton;
    //当前时间文字,持续时间文字
    private TextView currentTimeTextView, durationTimeTextView;
    //当前时间&持续时间
    private int currentTime, durationTime;
    //播放器包装
    private MediaPlayerWrapper mMediaPlayerWrapper = new MediaPlayerWrapper();
    //时间栈
    private TimerTask timeTask;
    //用于在异步线程中更新播放时间
    private Handler handler = new Handler();
    private Runnable updateThread = new Runnable() {//异步线程
        public void run() {
            currentTimeTextView.setText(StringUtil.transToTime(currentTime));
            Log.d("ctime", currentTime + "");
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bufferProgress = (ProgressBar) findViewById(R.id.bufferProgress);
        mMediaPlayerWrapper.init();
        mMediaPlayerWrapper.setPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
//                cancelBusy();//隐藏progressBar
                playOrPauseButton.setImageResource(R.mipmap.ic_pause_button);
                durationTime = (int) (mMediaPlayerWrapper.getPlayer().getDuration() / 1000);
                videoProgressSeekBar.setMax(durationTime);
                bufferProgress.setMax(durationTime);
                Log.d("dtime", durationTime + "");
                durationTimeTextView.setText(StringUtil.transToTime(durationTime));

                timeTask = new TimerTask() {//更新进度条
                    @Override
                    public void run() {
                        currentTime = (int) mMediaPlayerWrapper.getPlayer()
                                .getCurrentPosition() / 1000;
                        videoProgressSeekBar.setProgress(currentTime);
                        handler.post(updateThread);

                    }
                };
                new Timer().schedule(timeTask, 0, 300);
            }
        });

        mMediaPlayerWrapper.getPlayer().setOnBufferingUpdateListener(new IMediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
                bufferProgress.setProgress(i);
            }
        });

        mMediaPlayerWrapper.getPlayer().setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                Toast.makeText(VrVideoPlayerActivity.this, "播放失败：" + what + "，extra=" + extra, Toast.LENGTH_SHORT)
                        .show();
                return true;
            }
        });

        mMediaPlayerWrapper.getPlayer()
                .setOnVideoSizeChangedListener(new IMediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
                        getVRLibrary().onTextureResize(width, height);
                    }
                });

        mMediaPlayerWrapper.getPlayer().setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer mp) {
                mMediaPlayerWrapper.getPlayer().seekTo(0);
                playOrPauseButton.setImageResource(R.mipmap.ic_play_button);
            }
        });

        Uri uri = getUri();
        //加载URL视频
        if (uri != null) {
            mMediaPlayerWrapper.openRemoteFile(uri.toString());
            mMediaPlayerWrapper.prepare();
        }

        findViewById(R.id.bottom_control).setVisibility(View.VISIBLE);
        currentTimeTextView = (TextView) findViewById(R.id.text_currentTime);
        durationTimeTextView = (TextView) findViewById(R.id.text_durationTime);
        videoProgressSeekBar = (SeekBar) findViewById(R.id.seekBar_videoProgress);
        videoProgressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * @param seekBar
             * @param progress 当前进度
             * @param fromUser 是否用户触发
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mMediaPlayerWrapper.getPlayer().seekTo(progress * 1000);
                }
            }

            /**
             * 开始拖动时
             *
             * @param seekBar
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            /**
             * 停止拖动时
             *
             * @param seekBar
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        playOrPauseButton = (ImageButton) findViewById(R.id.imageButton_playOrPause);
        playOrPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayerWrapper.getPlayer().isPlaying()) {
                    mMediaPlayerWrapper.getPlayer().pause();
                    playOrPauseButton.setImageResource(R.mipmap.ic_play_button);
                } else {
                    mMediaPlayerWrapper.getPlayer().start();
                    playOrPauseButton.setImageResource(R.mipmap.ic_pause_button);
                }

            }
        });

    }

    @Override
    protected MDVRLibrary createVRLibrary() {
        return MDVRLibrary.with(this)
                .displayMode(MDVRLibrary.DISPLAY_MODE_NORMAL)
                .interactiveMode(MDVRLibrary.INTERACTIVE_MODE_MOTION_WITH_TOUCH)//默认即可触摸也可感应
                .asVideo(new MDVRLibrary.IOnSurfaceReadyCallback() {
                    @Override
                    public void onSurfaceReady(Surface surface) {
                        mMediaPlayerWrapper.getPlayer().setSurface(surface);
                    }
                })
                .ifNotSupport(new MDVRLibrary.INotSupportCallback() {
                    @Override
                    public void onNotSupport(int mode) {
                        String tip = mode == MDVRLibrary.INTERACTIVE_MODE_MOTION ? "设备不支持感应模式，请滑动视频控制视角" : "设备不支持感应模式，请滑动视频控制视角";
                        Toast.makeText(VrVideoPlayerActivity.this, tip, Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .pinchEnabled(true)
                .barrelDistortionConfig(new BarrelDistortionConfig().setDefaultEnabled(false)
                        .setScale(0.95f))
                .build(R.id.gl_view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeTask.cancel();
        mMediaPlayerWrapper.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaPlayerWrapper.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaPlayerWrapper.onResume();
    }
}
