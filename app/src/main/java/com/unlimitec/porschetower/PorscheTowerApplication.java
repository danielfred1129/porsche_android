package com.unlimitec.porschetower;

/**
 * Created by buddy on 9/5/2016.
 */

import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class PorscheTowerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

//        initImageLoader(getApplicationContext());
        initImageLoader();
    }

//    public static void initImageLoader(Context context) {
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
//        ImageLoader.getInstance().init(config);
//        ImageLoader.getInstance().clearDiskCache();
//    }
    public void initImageLoader() {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(getApplicationContext());
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        ImageLoader.getInstance().init(config.build());
    }
}
