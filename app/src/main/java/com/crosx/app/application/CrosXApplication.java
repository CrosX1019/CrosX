package com.crosx.app.application;

import android.app.Application;

import com.crosx.app.utils.imageUtil.ExecutorManager;
import com.crosx.app.utils.imageUtil.FileUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;


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
        initImageLoader();
    }

    /**
     * 初始化ImageLoader
     */
    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                //不缓存不同尺寸
                .denyCacheImageMultipleSizesInMemory()
                //下载图片线程的优先级
                .threadPriority(Thread.NORM_PRIORITY - 2)
                //设置下载线程的执行器（线程池）
                .taskExecutor(ExecutorManager.getInstance().getExecutors())
                //设置内存缓存的大小
                .memoryCacheSize((int) Runtime.getRuntime().maxMemory() / 8)
                //设置磁盘缓存的大小
                .diskCacheSize(50 * 1024 * 1024)
                //设置磁盘缓存的路径
                .diskCache(new UnlimitedDiskCache(FileUtil.getInstance(this).makeDir("imageCache")))
                //显示图片方式
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                //设置具体的图片加载器
                .imageDownloader(new BaseImageDownloader(this, 60 * 1000, 60 * 1000))
                .build();
        ImageLoader.getInstance().init(config);
    }
}
