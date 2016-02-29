package com.graduation.jasonzhu.mymoney.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by gemha on 2016/2/17.
 */
public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
