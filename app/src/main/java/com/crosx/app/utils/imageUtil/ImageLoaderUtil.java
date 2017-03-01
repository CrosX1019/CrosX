package com.crosx.app.utils.imageUtil;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.crosx.app.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * ImageLoader工具类,提供display方法加载图片
 */
public class ImageLoaderUtil {
    private static DisplayImageOptions options = new DisplayImageOptions.Builder()
            //下载过程中显示的图片
            .showImageOnLoading(R.mipmap.ic_launcher)
            //下载失败时显示的图片
            .showImageOnFail(R.mipmap.ic_launcher)
            //uri为空的时候显示的图片
            .showImageForEmptyUri(R.mipmap.ic_launcher)
            //是否进行内存缓存
            .cacheInMemory(true)
            //
            .cacheOnDisk(true)
            //
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)
            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
            .displayer(new FadeInBitmapDisplayer(200))
            //RoundedBitmapDisplayer
            .build();

    //file://

    public static void display(String url, ImageView imageView) {
        ImageLoader.getInstance().displayImage(url, imageView, options);
    }
}
