package com.tf.truefeeling.Activity;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
/**
 * Created by admin on 2016/4/1.
 */
public class TFApplication extends Application{

        @Override
        public void onCreate()
        {
            super.onCreate();
            setupLeanCloud(); // 初始化全局变量
        }

        private void setupLeanCloud() {
            AVOSCloud.initialize(this, "7K8Rg1kJkFjftzHtmCsPO4LP-gzGzoHsz","P952rbIbhSRmUdHnW18Mt8kx");
        }
}
