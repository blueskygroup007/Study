package com.bluesky.study.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.bluesky.study.utils.PreferenceUtils;

/**
 * @author BlueSky
 * @date 2021/7/3
 * Description:Application类,获取公共context
 */
public class App extends Application {
    private static Context mContext;
    private SharedPreferences mPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        //PreferenceUtils获取的时default的,应该是没有使用AppConstant中的常量
        mContext = getApplicationContext();
        mPreferences = PreferenceUtils.getPreferences();


        //初始化LogUtils
        //全局TAG


    }


    public static Context getContext() {
        return mContext;
    }

    public SharedPreferences getPreferences() {
        return mPreferences;
    }


}
