package com.crosx.app.application;

import android.app.Application;


/**
 * Created by CrosX on 2016/11/21.
 *
 * @BlinRoom
 */

public class CrosXApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppHelper.getInstance().init(this);
    }
}
