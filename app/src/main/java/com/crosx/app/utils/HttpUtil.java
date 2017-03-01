package com.crosx.app.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by CrosX on 2016/4/27.
 * RequestQueue初始化
 */
public class HttpUtil {

    private static RequestQueue requestQueue;

    private HttpUtil() {
    }

    public synchronized static RequestQueue getRequestQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }
}
