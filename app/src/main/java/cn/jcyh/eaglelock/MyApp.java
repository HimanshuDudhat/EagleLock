package cn.jcyh.eaglelock;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import cn.jcyh.eaglelock.util.Util;

/**
 * Created by jogger on 2018/4/26.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Util.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
