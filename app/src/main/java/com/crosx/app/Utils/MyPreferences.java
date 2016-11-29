package com.crosx.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by CrosX on 2016/11/21.
 *
 * @BlinRoom
 */

public class MyPreferences {

    private static final String TAG = MyPreferences.class.getSimpleName();

    private static MyPreferences instance = null;
    private static String PREFERENCE_NAME = "info";


    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    public static MyPreferences getInstance() {
        return instance;
    }

    public static void init(Context context) {
        instance = new MyPreferences();
        instance.pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        instance.editor = instance.pref.edit();
    }

}
