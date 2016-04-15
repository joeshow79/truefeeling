package com.tf.truefeeling;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

import android.content.Context;

/**
 * Created by admin on 2016/4/1.
 */
public class TFApplication extends Application {
    private static TFApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        setupLeanCloud(); // 初始化全局变量
    }

    public static Context getAppContext() {
        return mContext;
    }

    private void setupLeanCloud() {
        AVOSCloud.initialize(this, "7K8Rg1kJkFjftzHtmCsPO4LP-gzGzoHsz", "P952rbIbhSRmUdHnW18Mt8kx");
    }
}
