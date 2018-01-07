package com.example.contactsmanager.activity;

import android.app.Application;
import android.content.Context;

/**
 * Created by Chsen on 2018/1/6.
 *
 * 提供获取全局Context的方法
 */

public class MyApplication extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
