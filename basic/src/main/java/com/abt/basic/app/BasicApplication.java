package com.abt.basic.app;

import android.app.Application;

import com.abt.basic.BuildConfig;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * @描述：     @基类application
 * @作者：     @黄卫旗
 * @创建时间： @21/05/2018
 */
public abstract class BasicApplication extends Application {

    private static BasicApplication sContext;
    public static double heading;
    public static double latitude;
    public static double longitude;
    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        init();
        initComplete();
    }

    public static final BasicApplication getAppContext(){
        return sContext;
    }

    private final void init(){
        if(BuildConfig.DEBUG){
            // DebugManage.initialize(this);
            Logger.addLogAdapter(new AndroidLogAdapter());
        }
    }

    public abstract void initComplete();

}
