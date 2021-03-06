package com.crosx.app;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.crosx.app.utils.HttpUtil;
import com.hyphenate.helpdesk.easeui.UIProvider;
import com.readystatesoftware.systembartint.SystemBarTintManager;


/**
 * Created by CrosX on 2016/11/21.
 *
 * @BlinRoom
 */

public class BaseActivity extends AppCompatActivity {

    // Activity 在活动界面中的全局变量，用来代替this，在基类中定义是为了省去每个集成此类的 Activity 都定义一次
    public BaseActivity mActivity;

    private RequestQueue mRequestQueue;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;

        mRequestQueue = HttpUtil.getRequestQueue(this);

        //顶部状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setTintColor(R.color.base);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // onresume时，取消notification显示
        UIProvider.getInstance().getNotifier().reset();
    }


    /**
     * 通过xml查找相应的ID，通用方法
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T $(@IdRes int id) {
        return (T) findViewById(id);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁请求队列
        if (null != mRequestQueue
                && mRequestQueue.getSequenceNumber() > 0) {
            mRequestQueue.cancelAll(this);
        }
    }


    protected void addRequest(Request request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                3 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        mRequestQueue.add(request);
    }

}
