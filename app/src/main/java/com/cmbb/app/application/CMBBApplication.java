package com.cmbb.app.application;

import android.app.Application;
import android.content.pm.PackageManager;

import com.cmbb.app.action.common.Logger;
import com.cmbb.app.action.environment.Environments;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Storm on 2015/11/16.
 */
public class CMBBApplication extends Application {
    private static CMBBApplication instance;

    public static CMBBApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        new Thread() {
            @Override
            public void run() {
                super.run();
                initImageLoader();
                initJPush();
            }
        }.start();
    }

    private void initJPush() {
        JPushInterface.setDebugMode(Environments.printLog); // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this); // 初始化 JPush
        String regId = JPushInterface.getRegistrationID(this);
        Logger.i("CMBBApplication", "RegistId: " + regId);
    }

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this)
        .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                .imageDownloader(
                        new BaseImageDownloader(this, 10 * 1000, 30 * 1000))
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
    }

    /**
     * 信鸽注册ID
     *
     * @return
     */
    public String getMessageRegistId() {
        return "12345678901234566789";
    }

    /**
     * APP版本号
     *
     * @return
     */
    public int getVersionCode() {
        int code = 0;
        try {
            code = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;

            Logger.i("Application", "版本号:" + code);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } finally {
            return code;
        }
    }

    public int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    public int getScreenHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }
}
