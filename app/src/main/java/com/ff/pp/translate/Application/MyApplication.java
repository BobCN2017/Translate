package com.ff.pp.translate.Application;

import android.app.Application;
import android.content.Context;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by PP on 2017/3/27.
 */

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private static Context mContext;
    private static MyApplication mInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        // 讯飞初始化；
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=59301797");
        mContext = getApplicationContext();
        mInstance = this;

    }

    public static Context getContext() {
        return mContext;
    }

    public static MyApplication getInstance() {
        return mInstance;
    }


}
